package com.digitusrevolution.rideshare.user.business;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.UserService;

public class UserBusinessService {

	private UserDO userDO;
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(UserBusinessService.class.getName());

	public UserBusinessService() {
		userDO = new UserDO();
		userService = new UserService();
	}

	public User create(User user){	
		logger.debug("Getting Session");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			logger.debug("Beginning Transaction");
			transation = session.beginTransaction();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
			user = userService.create(user);
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
			transation.commit();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
		return user;
	}

	public boolean isExist(String userEmail){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		boolean status = false;
		try {
			transation = session.beginTransaction();
			
			status = userService.isExist(userEmail);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}	
		return status;

	}

	public User get(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
			
			user = userService.get(id);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
		return user;

	}
	
	public User getChild(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
			
			user = userService.getChild(id);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
		return user;

	}

	public List<User> getAll(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<User> users = new ArrayList<>();
		try {
			transation = session.beginTransaction();
			
			users = userService.getAll();

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
		return users;
	}

	public void update(User user){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			userService.update(user);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
	}

	public User addVehicle(User user, Vehicle vehicle){
		
		logger.debug("Getting Session");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			logger.debug("Beginning Transaction");
			transation = session.beginTransaction();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());			
			userDO.setUser(user);
			user = userDO.addVehicle(vehicle);
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
			transation.commit();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.debug("Closing Session");
				session.close();				
			}
		}
		return user;
	}
}
