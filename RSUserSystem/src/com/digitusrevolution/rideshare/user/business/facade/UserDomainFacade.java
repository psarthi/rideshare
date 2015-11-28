package com.digitusrevolution.rideshare.user.business.facade;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;

public class UserDomainFacade {

	private static final Logger logger = LogManager.getLogger(UserDomainFacade.class.getName());

	public int create(User user){	
		logger.debug("Getting Session");
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id=0;
		try {
			UserDomainService userService = new UserDomainService();
			logger.debug("Beginning Transaction");
			transation = session.beginTransaction();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
			id = userService.create(user);
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
			transation.commit();
			logger.debug("Session Status: " + session.isOpen());		
			logger.debug("Transaction Status: "+transation.getStatus());
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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

	public boolean isExist(String userEmail){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		boolean status = false;
		try {
			transation = session.beginTransaction();
			UserDomainService userService = new UserDomainService();
			status = userService.isExist(userEmail);

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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

	public User get(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
			UserDomainService userService = new UserDomainService();
			user = userService.get(id);

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
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
			UserDomainService userService = new UserDomainService();
			user = userService.getChild(id);

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
		finally {
			if (session.isOpen()){
				logger.info("Closing Session");
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
			UserDomainService userService = new UserDomainService();
			users = userService.getAll();

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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

	public void update(User user){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			UserDomainService userService = new UserDomainService();
			userService.update(user);

			transation.commit();
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
