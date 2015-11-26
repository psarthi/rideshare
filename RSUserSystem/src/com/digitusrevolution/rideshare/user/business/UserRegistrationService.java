package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.core.UserService;
import com.digitusrevolution.rideshare.user.exception.EmailExistException;

public class UserRegistrationService {
	
	private static final Logger logger = LogManager.getLogger(UserRegistrationService.class.getName());
	
	public User registerUser(User user){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			boolean status;
			UserService userService = new UserService();
			status = userService.checkUserExist(user.getEmail());
			if (status){
				throw new EmailExistException("Email id already exist :"+user.getEmail());					
			} else {
				user = userService.createUser(user);
			}
			
			transation.commit();
		} catch (HibernateException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
				throw e;
			}
		}
	
		return user;
		
	}

}
