package com.digitusrevolution.rideshare.user.business;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.UserDTO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserBusinessService {
	
	private static final Logger logger = LogManager.getLogger(UserBusinessService.class.getName());
	
	public int registerUser(UserDTO userDTO){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id = 0;
		try {
			transaction = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			User user = getUser(userDTO);			
			id = userDO.registerUser(user);
			
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

	private User getUser(UserDTO userDTO) {
		User user = new User();
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());
		user.setSex(userDTO.getSex());
		user.setMobileNumber(userDTO.getMobileNumber());
		user.setEmail(userDTO.getEmail());
		user.setPassword(userDTO.getPassword());
		user.setCity(userDTO.getCity());
		user.setState(userDTO.getState());
		user.setCountry(userDTO.getCountry());
		user.setPhoto(userDTO.getPhoto());
		return user;
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

}



























