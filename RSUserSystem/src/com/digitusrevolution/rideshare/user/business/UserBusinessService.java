package com.digitusrevolution.rideshare.user.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GoogleSignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.SignInInfo;
import com.digitusrevolution.rideshare.model.user.dto.UserSignInResult;
import com.digitusrevolution.rideshare.model.user.dto.UserStatus;
import com.digitusrevolution.rideshare.model.user.dto.UserRegistration;
import com.digitusrevolution.rideshare.user.domain.OTPDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserBusinessService {
	
	private static final Logger logger = LogManager.getLogger(UserBusinessService.class.getName());
	
	public int registerUser(UserRegistration userRegistration){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			
			User user = JsonObjectMapper.getMapper().convertValue(userRegistration, User.class);			
			id = userDO.registerUser(user, userRegistration.getOtp());							
			
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

	public void addAccount(int userId, Account account){
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
	
	public List<User> findAllPotentialFriendsBasedOnEmailOrMobile(int userId, List<String> emailIds, List<String> mobileNumbers){
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
	
	public void sendFriendRequest(int userId, List<User> friends){
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
	
	public void acceptFriendRequest(int userId, int friendUserId){
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
	
	public void rejectFriendRequest(int userId, int friendUserId){
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
	
	public UserSignInResult googleSignIn(GoogleSignInInfo googleSignInInfo) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		UserSignInResult userSignInResult = null;
		try {
			transaction = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			userSignInResult = userDO.googleSignIn(googleSignInInfo.getEmail());
			
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
		UserStatus userStatus = new UserStatus();
		try {
			transaction = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			status = userDO.isEmailExist(userEmail);
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

}



























