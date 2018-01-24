package com.digitusrevolution.rideshare.user.domain.service;

import java.util.Collection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.inf.DomainServiceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainServiceLong;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserDomainService implements DomainServiceLong<User>{

	private static final Logger logger = LogManager.getLogger(UserDomainService.class.getName());

	@Override
	public User get(long id, boolean fetchChild){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		User user = null;
		try {
			transaction = session.beginTransaction();
	
			UserDO userDO = new UserDO();
			if (fetchChild){
				user = userDO.getAllData(id);
			} else {
				user = userDO.get(id);			
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
		return user;
	}
	
	@Override
	public List<User> getAll(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<User> users = null;
		try {
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			users = userDO.getAll();

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

	public Collection<Role> getRoles(long id){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Collection<Role> roles = null;
		try {
			transaction = session.beginTransaction();
	
			UserDO userDO = new UserDO();
			roles = userDO.getRoles(id);

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
		return roles;
		
	}

	public List<GroupDetail> getGroups(long userId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<GroupDetail> groups = null;
		try {
			transaction = session.beginTransaction();
	
			UserDO userDO = new UserDO();
			groups = userDO.getGroups(userId);

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
		return groups;
	}
	
}
