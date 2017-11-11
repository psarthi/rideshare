package com.digitusrevolution.rideshare.user.domain.core;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.engine.jdbc.connections.internal.UserSuppliedConnectionProviderImpl;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.exception.EmailExistException;
import com.digitusrevolution.rideshare.common.exception.LoginFailedException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.dto.RideDTO;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.UserBasicInformationDTO;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.CountryDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;

public class UserDO implements DomainObjectPKInteger<User>{

	private User user;
	private UserEntity userEntity;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());
	private UserMapper userMapper;
	private final UserDAO userDAO;

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
		userMapper = new UserMapper();
		userDAO = new UserDAO();
	}

	public void setUser(User user) {
		this.user = user;
		userEntity = userMapper.getEntity(user,true);
	}

	private void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		user = userMapper.getDomainModel(userEntity,false);
	}

	@Override
	public int create(User user){
		setUser(user);
		int id = userDAO.create(userEntity);
		return id;
	}

	@Override
	public User get(int id){
		userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setUserEntity(userEntity);
		return user;
	}

	@Override
	public User getAllData(int id){		
		get(id);
		fetchChild();
		return user;
	}

	@Override
	public List<User> getAll(){
		List<User> users = new ArrayList<>();
		List<UserEntity> userEntities = userDAO.getAll();
		for (UserEntity userEntity : userEntities) {
			setUserEntity(userEntity);
			users.add(user);
		}
		return users;
	}

	@Override
	public void update(User user){
		if (user.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+user.getId());
		}
		setUser(user);
		userDAO.update(userEntity);
	}

	@Override
	public void delete(int id){
		user = get(id);
		setUser(user);
		userDAO.delete(userEntity);
	}

	@Override
	public void fetchChild(){
		user = userMapper.getDomainModelChild(user, userEntity);
	}



	public boolean isEmailExist(String userEmail){
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}

	public Collection<Role> getRoles(int id){
		getAllData(id);
		logger.debug("Role size: "+user.getRoles().size());
		return user.getRoles();
	}

	public void addVehicle(Vehicle vehicle){		
		if (user.getVehicles().size()==0){
			RoleDO roleDO = new RoleDO();
			Role role = roleDO.get(RoleName.Driver.toString());
			user.getRoles().add(role);
		}
		user.getVehicles().add(vehicle);
		update(user);
	}

	public int registerUser(User user){
		int id = 0;
		boolean status = isEmailExist(user.getEmail());
		if (status){
			throw new EmailExistException("Email id already exist :"+user.getEmail());					
		} else {
			CountryDO countryDO = new CountryDO();
			Country country = countryDO.get(user.getCountry().getName());
			user.setCountry(country);
			id = create(user);
		}
		return id;
	}

	/*
	 * Purpose - Add account to the user
	 * 
	 */
	public void addAccount(int userId, Account account){
		//Always use getAllData instead of get whenever you are trying to update, so that you don't miss any fields where relationship is owned by this entity
		//Otherwise while updating, that field relationship would be deleted
		User user = getAllData(userId);
		user.getAccounts().add(account);
		update(user);		
	}

	/*
	 * Purpose - Return all potential new friends who have not submitted friend request and registered user in the system
	 * 
	 * High level logic -
	 * 
	 * - Get all the registered user based on email ids or mobile numbers
	 * - Check if user is already a friend
	 * - If not, check if he/she has submitted friend request
	 * - If not, then add it to potential friends list
	 * - Return all potential friends
	 * 
	 */
	public List<User> findAllPotentialFriendsBasedOnEmailOrMobile(int userId, List<String> emailIds, List<String> mobileNumbers){
		List<UserEntity> registeredUserEntities = userDAO.findAllRegisteredUserBasedOnEmailOrMobile(userId, emailIds, mobileNumbers);
		Collection<User> registeredUsers = new LinkedList<>();
		registeredUsers = userMapper.getDomainModels(registeredUsers, registeredUserEntities, false);
		//We are only using once to fetch the user, so this will not get overwritten, otherwise we have to be careful when 
		//using multiple fetch in the same for user itself, as it will overwrite previous ones while setting user/entity in get function
		user = getAllData(userId);
		List<User> potentialFriends = new LinkedList<>();
		for (User registeredUser : registeredUsers) {
			if (!isUserFriend(user, registeredUser)){
				if (!isFriendRequestSubmitted(user, registeredUser.getId())){
					potentialFriends.add(registeredUser);
				} else {
					logger.debug("Friend request has already been submitted for id:"+registeredUser.getId());
				}
			} else {
				logger.debug("User is already friend with id:"+registeredUser.getId());
			}
		}

		return potentialFriends;
	}

	/*
	 * Purpose - Send friend request to all selected users who are not friend or submitted friend request earlier
	 * 
	 * High level logic -
	 * 
	 * - Check if user is already friend
	 * - Check if user has already submitting friend request
	 * - If nothing is true, then add friend request to user with current time in UTC 
	 *   and Approval status as pending
	 * 
	 */
	public void sendFriendRequest(int userId, List<User> friends){
		user = getAllData(userId);
		for (User friend : friends) {
			ZonedDateTime dateTime = DateTimeUtil.getCurrentTimeInUTC();
			FriendRequest friendRequest = new FriendRequest();
			friendRequest.setCreatedDateTime(dateTime);
			friendRequest.setFriend(friend);
			friendRequest.setStatus(ApprovalStatus.Pending);
			//No need to check if user is already friend as for every friend also, friend request was also submitted,
			//so this will avoid another check  
			if (!isFriendRequestSubmitted(user, friend.getId())){
				user.getFriendRequests().add(friendRequest);				
			} else {
				logger.debug("Friend request has already been submitted for id:"+friend.getId());
			}
		}
		//Update the details in DB
		update(user);
	}

	private boolean isUserFriend(User user, User friend){
		Collection<User> friends = user.getFriends();
		for (User userFriend : friends) {
			if (userFriend.getId() == friend.getId()){
				return true;
			}
		}
		return false;
	}

	private boolean isFriendRequestSubmitted(User user, int friendUserId){
		FriendRequest friendRequest = user.getFriendRequest(friendUserId);
		if (friendRequest!=null){
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Purpose - Accept Friend request
	 * 
	 * High level logic -
	 * 
	 * - Check if its status is Pending or Rejected
	 * - If yes, then only accept is possible
	 * - Else throw exception
	 * 
	 */
	public void acceptFriendRequest(int userId, int friendUserId){
		user = getAllData(userId);
		FriendRequest friendRequest = user.getFriendRequest(friendUserId);
		if (friendRequest.getStatus().equals(ApprovalStatus.Pending) || friendRequest.getStatus().equals(ApprovalStatus.Rejected)){
			friendRequest.setStatus(ApprovalStatus.Approved);	
			user.getFriends().add(friendRequest.getFriend());
		} else {
			throw new WebApplicationException("Friend request can't be approved as its not in valid state. Current status:"+friendRequest.getStatus()); 
		}
		//Update the details in DB
		update(user);
	}

	/*
	 * Purpose - Rejected friend request
	 * 
	 * High level logic -
	 * 
	 * - Check if its status is Pending, then only you can reject
	 * - If yes, update the status as Rejected
	 * 
	 */
	public void rejectFriendRequest(int userId, int friendUserId){
		user = getAllData(userId);
		FriendRequest friendRequest = user.getFriendRequest(friendUserId);
		if (friendRequest.getStatus().equals(ApprovalStatus.Pending)){
			friendRequest.setStatus(ApprovalStatus.Rejected);
			update(user);
		} else {
			throw new WebApplicationException("Friend request can't be rejected as its not in valid state. Current status:"+friendRequest.getStatus());
		}
	}
	
	public User getUserByEmail(String userEmail) {
		userEntity = userDAO.getUserByEmail(userEmail);
		if (userEntity==null) {
			throw new NotFoundException("No User found with email: "+userEmail);
		}
		setUserEntity(userEntity);
		return user;
	}
	
	public UserBasicInformationDTO signIn(String email, String password) {
		user = getUserByEmail(email);
		if (user.getPassword().equals(password)) {
			return getUserBasicInformationDTO(user);
		} else {
			throw new LoginFailedException("Login Failed");
		}
	}

	private UserBasicInformationDTO getUserBasicInformationDTO(User user) {
		UserBasicInformationDTO userBasicInformationDTO = new UserBasicInformationDTO();
		userBasicInformationDTO.setToken(AuthService.getInstance().getToken(user.getId()));
		//Note - You don't have to get just basic profile of user as getUserByEmail has already got basic user profile.
		userBasicInformationDTO.setUserProfile(user);
		userBasicInformationDTO.setUpcomingRide(RESTClientUtil.getUpcomingRide(user.getId()));
		userBasicInformationDTO.setUpcomingRideRequest(RESTClientUtil.getUpcomingRideRequest(user.getId()));
		return userBasicInformationDTO;
	}
	
	public UserBasicInformationDTO googleSignIn(String email) {
		user = getUserByEmail(email);
		return getUserBasicInformationDTO(user);		
	}
	
}


















































