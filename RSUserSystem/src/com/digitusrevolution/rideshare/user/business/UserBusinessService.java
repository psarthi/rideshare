package com.digitusrevolution.rideshare.user.business;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.HttpHeaders;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.exception.InvalidTokenException;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Preference;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.FullUser;
import com.digitusrevolution.rideshare.model.user.dto.GoogleSignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupListType;
import com.digitusrevolution.rideshare.model.user.dto.SignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserProfile;
import com.digitusrevolution.rideshare.model.user.dto.UserRegistration;
import com.digitusrevolution.rideshare.model.user.dto.UserSignInResult;
import com.digitusrevolution.rideshare.model.user.dto.UserStatus;
import com.digitusrevolution.rideshare.user.domain.OTPDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.VehicleDO;

public class UserBusinessService {

	private static final Logger logger = LogManager.getLogger(UserBusinessService.class.getName());

	public long registerUser(UserRegistration userRegistration){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		long id = 0;
		try {
			transaction = session.beginTransaction();
			
			//IMP - Google Token revalidation is imp to avoid fraud registration
			if (AuthService.getInstance().validateGoogleSignInToken(userRegistration.getEmail(), userRegistration.getSignInToken())) {
				UserDO userDO = new UserDO();
				User user = JsonObjectMapper.getMapper().convertValue(userRegistration, User.class);			
				id = userDO.registerUser(user, userRegistration.getOtp());															
			} else {
				throw new InvalidTokenException();
			}

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}	

		return id;
	}

	public void addAccount(long userId, Account account){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userDO.addAccount(userId, account);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}	
	}

	public List<User> findAllPotentialFriendsBasedOnEmailOrMobile(long userId, List<String> emailIds, List<String> mobileNumbers){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<User> users = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			users = userDO.findAllPotentialFriendsBasedOnEmailOrMobile(userId, emailIds, mobileNumbers);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return users;

	}

	public void sendFriendRequest(long userId, List<User> friends){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userDO.sendFriendRequest(userId, friends);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
	}

	public void acceptFriendRequest(long userId, long friendUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userDO.acceptFriendRequest(userId, friendUserId);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
	}

	public void rejectFriendRequest(long userId, long friendUserId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userDO.rejectFriendRequest(userId, friendUserId);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			

	}

	public UserSignInResult signIn(SignInInfo signInDTO) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		UserSignInResult userSignInResult = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userSignInResult = userDO.signIn(signInDTO.getEmail(), signInDTO.getPassword());

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return userSignInResult;
	}

	public UserSignInResult googleSignIn(GoogleSignInInfo googleSignInInfo, String token) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		UserSignInResult userSignInResult = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			if (token!=null) {
				if (AuthService.getInstance().verifyToken(token)) {
					logger.debug("Token is valid, so bypassing backend google validation");
					userSignInResult = userDO.googleSignIn(googleSignInInfo.getEmail());					
				} else {
					throw new InvalidTokenException();
				}
			}
			else {
				//IMP - Google Token revalidation is imp to avoid fraud signIn
				logger.debug("Token is not available, so doing google token re-validation");
				if (AuthService.getInstance().validateGoogleSignInToken(googleSignInInfo.getEmail(), googleSignInInfo.getSignInToken())) {
					userSignInResult = userDO.googleSignIn(googleSignInInfo.getEmail());	
				} else {
					throw new WebApplicationException("Google sign in token is invalid, please try again");
				}
			}
			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return userSignInResult;
	}

	public UserSignInResult signInWithToken(String token) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		UserSignInResult userSignInResult = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userSignInResult = userDO.signInWithToken(token);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return userSignInResult;
	}


	public UserStatus checkUserExist(String userEmail){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		boolean status = false;
		UserStatus userStatus = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			status = userDO.isEmailExist(userEmail);
			//Initializing here so that we can check the status of userStatus as null in BusinessResource in case of any exception
			userStatus = new UserStatus();
			userStatus.setUserExist(status);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return userStatus;
	}

	public String getOTP(String mobileNumber){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		String OTP = null;
		try {
			transaction = session.beginTransaction();

			OTPDO otpDO = new OTPDO();
			OTP = otpDO.getOTP(mobileNumber);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return OTP;
	}

	public boolean validateOTP(String mobileNumber, String otp){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		boolean status = false;
		try {
			transaction = session.beginTransaction();

			OTPDO otpDO = new OTPDO();
			status = otpDO.validateOTP(mobileNumber, otp);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}			
		return status;
	}

	public void updateUserPreference(long userId, Preference preference) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			User user = userDO.getAllData(userId);
			user.setPreference(preference);
			userDO.update(user);

			//This will take care of updating the default vehcile configuration e.g seat/luggage change
			//which will not have any effect on existing rides as we are not referring to current vehcile seats in ride logic
			//instead we save offered ride seats in ride itself and we match as per that number
			//so logically it may happen that user may reduce / increase the vehicle but this would effect future ride creation and not existing rides
			VehicleDO vehicleDO = new VehicleDO();
			//This will ensure we can update preference even if there is no vehicle added
			if (preference.getDefaultVehicle()!=null) {
				vehicleDO.update(preference.getDefaultVehicle());	
			}

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}		
	}

	public FullUser get(long id, boolean fetchChild){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullUser fullUser = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			User user;
			if (fetchChild){
				user = userDO.getAllData(id);
			} else {
				user = userDO.get(id);			
			}

			fullUser = JsonObjectMapper.getMapper().convertValue(user, FullUser.class);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return fullUser;
	}

	public void addUserFeedback(long userId, UserFeedbackInfo userFeedbackInfo) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			BasicRide basicRide = userFeedbackInfo.getRide();
			Ride ride = JsonObjectMapper.getMapper().convertValue(basicRide, Ride.class);

			BasicRideRequest basicRideRequest = userFeedbackInfo.getRideRequest();
			RideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(basicRideRequest, RideRequest.class);


			BasicUser basicUser = userFeedbackInfo.getGivenByUser();
			User givenByUser = JsonObjectMapper.getMapper().convertValue(basicUser, User.class);

			userDO.addUserFeedback(userId, givenByUser, ride, rideRequest, userFeedbackInfo.getRating());

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
	}

	public UserProfile getUserProfile(long userId, long signedInUserId) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		UserProfile userProfile = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			userProfile = userDO.getUserProfile(userId, signedInUserId);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return userProfile;
	}

	public List<GroupDetail> getGroups(long userId, GroupListType listType, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<GroupDetail> groupDetails = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			groupDetails = userDO.getGroups(userId, listType, page);

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return groupDetails;
	}

	public List<BasicUser> searchUserByName(String name, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		List<BasicUser> basicUsers = new LinkedList<>();
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			List<User> users = userDO.searchUserByName(name, page);
			for (User user: users) {
				BasicUser basicUser = new BasicUser();
				basicUser = JsonObjectMapper.getMapper().convertValue(user, BasicUser.class);
				basicUsers.add(basicUser);
			}

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return basicUsers;
	}

	public List<BasicMembershipRequest> getUserMembershipRequests(long userId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		List<BasicMembershipRequest> requests = new LinkedList<>();
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			requests = userDO.getUserMembershipRequests(userId, page);			
			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
				session.close();				
			}
		}
		return requests;
	}
}



























