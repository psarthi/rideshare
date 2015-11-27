package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;
import com.digitusrevolution.rideshare.user.exception.EmailExistException;

public class UserRegistrationService {
	
	private static final Logger logger = LogManager.getLogger(UserRegistrationService.class.getName());
	
	public int registerUser(User user){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id=0;
		try {
			transation = session.beginTransaction();
			
			boolean status;
			UserDomainService userService = new UserDomainService();
			status = userService.isExist(user.getEmail());
			if (status){
				throw new EmailExistException("Email id already exist :"+user.getEmail());					
			} else {
				id = userService.create(user);
			}
			
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
		return id;
		
	}
}
