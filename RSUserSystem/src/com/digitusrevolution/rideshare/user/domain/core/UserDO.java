package com.digitusrevolution.rideshare.user.domain.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;
import javax.persistence.PreRemove;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.exception.EmailExistException;
import com.digitusrevolution.rideshare.common.exception.OTPFailedException;
import com.digitusrevolution.rideshare.common.exception.SignInFailedException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientImpl;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.Preference;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.FullUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupListType;
import com.digitusrevolution.rideshare.model.user.dto.MembershipStatus;
import com.digitusrevolution.rideshare.model.user.dto.UserProfile;
import com.digitusrevolution.rideshare.model.user.dto.UserSignInResult;
import com.digitusrevolution.rideshare.user.data.GroupDAO;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.CountryDO;
import com.digitusrevolution.rideshare.user.domain.OTPDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;
import com.digitusrevolution.rideshare.user.domain.VehicleCategoryDO;
import com.digitusrevolution.rideshare.user.domain.VehicleSubCategoryDO;

public class UserDO implements DomainObjectPKLong<User>{

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
	public long create(User user){
		setUser(user);
		long id = userDAO.create(userEntity);
		return id;
	}

	@Override
	public User get(long id){
		userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setUserEntity(userEntity);
		return user;
	}

	@Override
	public User getAllData(long id){		
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
	public void delete(long id){
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

	public Collection<Role> getRoles(long id){
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
		//This will get the vehicle with updated ID and then set the default vehicle
		user = get(user.getId());
		if (user.getVehicles().size() == 1) {
			updateDefaultVehicle(user, getVehicle(user, vehicle.getRegistrationNumber()));
		}
	}
	
	private Vehicle getVehicle(User user, String registrationNumber) {
		for (Vehicle vehicle: user.getVehicles()) {
			if (vehicle.getRegistrationNumber().equals(registrationNumber)) {
				return vehicle;
			}
		}
		return null;
	}

	private void updateDefaultVehicle(User user, Vehicle vehicle) {
		user.getPreference().setDefaultVehicle(vehicle);
		update(user);
	}
	
	public long registerUser(User user, String otp){
		long id = 0;
		OTPDO otpdo = new OTPDO();
		boolean otpValidationStatus = otpdo.validateOTP(user.getMobileNumber(), otp); 
		boolean emailStatus = isEmailExist(user.getEmail());
		if (!otpValidationStatus){
			throw new OTPFailedException("OTP Validation Failed");
		}
		if (emailStatus){
			throw new EmailExistException("Email id already exist :"+user.getEmail());					
		} 
		else {
			CountryDO countryDO = new CountryDO();
			Country country = countryDO.get(user.getCountry().getName());
			user.setCountry(country);
			user.setPreference(getDefaultPreference());
			Role role = new Role();
			role.setName(RoleName.Passenger);
			user.getRoles().add(role);
			//This will create virtual account
			Account account = RESTClientUtil.createVirtualAccount();
			//This will take care of exception thrown by the Billing system if any
			if (account==null) {
				throw new WebApplicationException("Unable to create Virtual account for the user email:"+user.getEmail());
			}
			//This will associate the same virtual account to the user
			user.getAccounts().add(account);
			id = create(user);		
			logger.debug("New user registered with email id/userId:"+user.getEmail()+"/"+id);
		}
		return id;
	}
		
	private Preference getDefaultPreference() {
		
		Preference preference = new Preference();
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		int vehicleCategoryId = Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_VEHICLE_CATEGORY_ID"));
		VehicleCategory vehicleCategory = vehicleCategoryDO.get(vehicleCategoryId);
		preference.setVehicleCategory(vehicleCategory);
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		int vehicleSubCategoryId = Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_VEHICLE_SUB_CATEGORY_ID"));
		VehicleSubCategory vehicleSubCategory = vehicleSubCategoryDO.get(vehicleSubCategoryId);
		preference.setVehicleSubCategory(vehicleSubCategory);

		LocalTime pickupTimeVariation = LocalTime.of(0, Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_PICKUP_TIME_VARIATION_MINS")));
		preference.setPickupTimeVariation(pickupTimeVariation);
		preference.setPickupPointVariation(Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_PICKUP_POINT_VARIATION_IN_METERS")));
		preference.setDropPointVariation(Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_DROP_POINT_VARIATION_IN_METERS")));
		
		preference.setSeatRequired(Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_SEAT_REQUIRED")));
		preference.setLuggageCapacityRequired(Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_LUGGAGE_REQUIRED")));
		
		preference.setMinProfileRating(Integer.parseInt(PropertyReader.getInstance().getProperty("USER_DEFAULT_PREFS_MIN_PROFILE_RATING")));
		
		TrustCategory trustCategory = new TrustCategory();
		trustCategory.setName(TrustCategoryName.Anonymous);
		preference.setTrustCategory(trustCategory);
		
		preference.setRideMode(RideMode.Free);

		return preference;
	}

	/*
	 * Purpose - Add account to the user
	 * 
	 */
	public void addAccount(long userId, Account account){
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
	public List<User> findAllPotentialFriendsBasedOnEmailOrMobile(long userId, List<String> emailIds, List<String> mobileNumbers){
		Set<UserEntity> registeredUserEntities = userDAO.findAllRegisteredUserBasedOnEmailOrMobile(userId, emailIds, mobileNumbers);
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
	
	public List<User> searchUserByName(String name, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		Set<UserEntity> userEntities = userDAO.searchUserByName(name, startIndex);
		LinkedList<User> users = new LinkedList<>();
		users = (LinkedList<User>) userMapper.getDomainModels(users, userEntities, false);
		Collections.sort(users);
		return users;
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
	public void sendFriendRequest(long userId, List<User> friends){
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

	private boolean isFriendRequestSubmitted(User user, long friendUserId){
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
	public void acceptFriendRequest(long userId, long friendUserId){
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
	public void rejectFriendRequest(long userId, long friendUserId){
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
	
	public UserSignInResult signIn(String email, String password) {
		user = getUserByEmail(email);
		if (user.getPassword().equals(password)) {
			UserSignInResult userSignInResult = getUserSignInResult(user);
			userSignInResult.setToken(AuthService.getInstance().getToken(user.getId()));
			return userSignInResult;
		} else {
			throw new SignInFailedException("Login Failed");
		}
	}

	private UserSignInResult getUserSignInResult(User user) {
		UserSignInResult userSignInResult = new UserSignInResult();
		//Note - You don't have to get just basic profile of user as getUserByEmail has already got basic user profile.
		userSignInResult.setUser(JsonObjectMapper.getMapper().convertValue(user, BasicUser.class));
		userSignInResult.setCurrentRide(RESTClientUtil.getCurrentRide(user.getId()));
		userSignInResult.setCurrentRideRequest(RESTClientUtil.getCurrentRideRequest(user.getId()));
		return userSignInResult;
	}
	
	public UserSignInResult googleSignIn(String email) {
		user = getUserByEmail(email);
		UserSignInResult userSignInResult = getUserSignInResult(user);
		userSignInResult.setToken(AuthService.getInstance().getToken(user.getId()));
		return userSignInResult;		
	}
	
	public UserSignInResult signInWithToken(String token) {
		user = get(AuthService.getInstance().getUserId(token));
		UserSignInResult userSignInResult = getUserSignInResult(user);
		//Since token generation is based on the signIn type, so we have excluded this step from common getUserSignInResult function
		userSignInResult.setToken(token);
		return userSignInResult;		
	}
	
	public void addUserFeedback(long userId, User givenByUser, Ride ride, RideRequest rideRequest, float rating) {
		user = getAllData(userId);
		UserFeedback feedback = new UserFeedback();
		feedback.setForUser(user);
		feedback.setGivenByUser(givenByUser);
		feedback.setRating(rating);
		feedback.setRide(ride);
		feedback.setRideRequest(rideRequest);
		//Since this is a set, it will replace old value of user feedback if it exist, 
		//which will take care of our feedback updates for the same ride and user
		user.getFeedbacks().add(feedback);
		user.setProfileRating(getProfileRating(user));
		update(user);
	}

	private float getProfileRating(User user) {
		float userRating = 0;
		for (UserFeedback userFeedback: user.getFeedbacks()) {
			userRating += userFeedback.getRating();	
		}
		float profileRating = userRating / user.getFeedbacks().size();
		return profileRating;
	}
	
	public UserProfile getUserProfile(long userId, long signedInUserId) {
		
		user = get(userId);
		UserProfile userProfile = new UserProfile();
		userProfile.setUser(JsonObjectMapper.getMapper().convertValue(user, BasicUser.class));
		
		userProfile.setOfferedRides(userDAO.getRidesOffered(userId));	
		userProfile.setRequestedRides(userDAO.getRideRequests(userId));
		
		userProfile.setCommonGroups(getCommonGroups(userId, signedInUserId));
		
		return userProfile;
	}
	
	public List<GroupDetail> getGroups(long userId, GroupListType listType, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<GroupEntity> groupEntities = userDAO.getGroups(listType, userId, startIndex);
		GroupMapper groupMapper = new GroupMapper();
		LinkedList<Group> groups = new LinkedList<>();
		//We need just basic group information, so no need to fetch child
		groupMapper.getDomainModels(groups, groupEntities, false);
		//this will sort the list further
		Collections.sort(groups);
		GroupDO groupDO = new GroupDO();
		return groupDO.getGroupDetails(userId, groups);
	}
	
	
	public List<GroupDetail> getGroups(long userId){
		List<GroupEntity> groupEntities = userDAO.getGroups(userId);
		GroupMapper groupMapper = new GroupMapper();
		LinkedList<Group> groups = new LinkedList<>();
		//We need just basic group information, so no need to fetch child
		groupMapper.getDomainModels(groups, groupEntities, false);
		//this will sort the list further
		Collections.sort(groups);
		GroupDO groupDO = new GroupDO();
		return groupDO.getGroupDetails(userId, groups);
	}
	
	public List<GroupDetail> getCommonGroups(long userId, long signedInUserId){
		//Don't call getGroups which will avoid unnecessary further calls when using mapper
		List<GroupEntity> firstGroupEntities = userDAO.getGroups(userId);
		List<GroupEntity> secondGroupEntities = userDAO.getGroups(signedInUserId);		
		firstGroupEntities.retainAll(secondGroupEntities);		
		
		LinkedList<Group> groups = new LinkedList<>();
		GroupMapper groupMapper = new GroupMapper();
		//We need just basic group information, so no need to fetch child
		groupMapper.getDomainModels(groups, firstGroupEntities, false);
		//this will sort the list further
		Collections.sort(groups);
		GroupDO groupDO = new GroupDO();
		//VERY IMP - Its very important to get memebership status of all groups against the signedInUser and not userId for which you are requesting profile
		//as group membership status plays very important role in making lots of decision on frontend
		return groupDO.getGroupDetails(signedInUserId, groups);
	}
	
	
	public boolean isInvited(long groupId, long userId){
		return userDAO.isInvited(groupId, userId);
	}
	
	public List<BasicMembershipRequest> getUserMembershipRequests(long userId, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<MembershipRequestEntity> membershipRequestEntities = userDAO.getUserMembershipRequests(userId, startIndex);
		return getBasicMembershipRequests(membershipRequestEntities);
	}

	public List<BasicMembershipRequest> getBasicMembershipRequests(
			Collection<MembershipRequestEntity> membershipRequestEntities) {
		MembershipRequestMapper requestMapper = new MembershipRequestMapper();
		LinkedList<MembershipRequest> membershipRequests = new LinkedList<>();
		membershipRequests = (LinkedList<MembershipRequest>) requestMapper.getDomainModels(membershipRequests, membershipRequestEntities, false);
		Collections.sort(membershipRequests);
		
		LinkedList<BasicMembershipRequest> basicMembershipRequests = new LinkedList<>();
		for (MembershipRequest request: membershipRequests) {
			BasicMembershipRequest basicMembershipRequest = getBasicMembershipRequestFromRequest(request);
			basicMembershipRequests.add(basicMembershipRequest);
		}
		return basicMembershipRequests;
	}

	public BasicMembershipRequest getBasicMembershipRequestFromRequest(MembershipRequest request) {
		BasicMembershipRequest basicMembershipRequest = new BasicMembershipRequest();
		basicMembershipRequest = JsonObjectMapper.getMapper().convertValue(request, BasicMembershipRequest.class);
		GroupDO groupDO = new GroupDO();
		GroupDetail groupDetail = groupDO.getGroupDetailFromGroup(request.getGroup(), request.getUser().getId());
		//This is important so that we get updated group detail instead of empty information on membercount and group status
		basicMembershipRequest.setGroup(groupDetail);
		return basicMembershipRequest;
	}
}


















































