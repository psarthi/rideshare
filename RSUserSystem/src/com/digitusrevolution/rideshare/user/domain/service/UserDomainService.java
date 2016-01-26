package com.digitusrevolution.rideshare.user.domain.service;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserDomainService implements DomainService<User>{

	private static final Logger logger = LogManager.getLogger(UserDomainService.class.getName());

	@Override
	public User get(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		User user = null;
		try {
			transation = session.beginTransaction();
	
			UserDO userDO = new UserDO();
			user = userDO.get(id);			
			
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
	
	@Override
	public List<User> getAll(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<User> users = null;
		try {
			transation = session.beginTransaction();

			UserDO userDO = new UserDO();
			users = userDO.getAll();

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


	public Collection<Role> getRoles(int id){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		Collection<Role> roles = null;
		try {
			transation = session.beginTransaction();
	
			UserDO userDO = new UserDO();
			roles = userDO.getRoles(id);

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
		return roles;
		
	}

}
