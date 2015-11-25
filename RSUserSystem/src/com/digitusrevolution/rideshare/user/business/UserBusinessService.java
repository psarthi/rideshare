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
import com.digitusrevolution.rideshare.user.domain.UserDO;
import com.digitusrevolution.rideshare.user.domain.UserService;

public class UserBusinessService {

	private UserDO userDO;
	private UserService userService;
	private static final Logger logger = LogManager.getLogger(UserBusinessService.class.getName());

	public UserBusinessService() {
		userDO = new UserDO();
		userService = new UserService();
	}

	public User createUser(User user){	
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			logger.info(session.isOpen());		
			logger.info(transation.getStatus());
			user = userService.createUser(user);
			logger.info(session.isOpen());
			logger.info(transation.getStatus());
			transation.commit();
			logger.info(transation.getStatus());
			logger.info(session.isOpen());
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
				logger.info(session.isOpen());
		//		session.close();
			}
		}
		return user;
	}

	public boolean checkUserExist(String userEmail){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		boolean status = false;
		try {
			transation = session.beginTransaction();
			
			status = userService.checkUserExist(userEmail);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
	//			session.close();
			}
		}
		return status;

	}

	public User getUser(int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
			
			user = userService.getUser(userId);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
	//			session.close();
			}
		}
		return user;

	}
	
	public User getUserFullDetail(int userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
			
			user = userService.getUserFullDetail(userId);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
		//		session.close();
			}
		}
		return user;

	}

	public List<User> getAllUser(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<User> users = new ArrayList<>();
		try {
			transation = session.beginTransaction();
			
			users = userService.getAllUser();

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
//				session.close();
			}
		}

		return users;
	}

	public void updateUser(User user){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			userService.updateUser(user);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
		//		session.close();
			}
		}

	}

	public User addVehicle(User user, Vehicle vehicle){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			userDO.setUser(user);
			user = userDO.addVehicle(vehicle);

			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		} finally {
			if (session!=null){
		//		session.close();
			}
		}
		return user;
	}
}
