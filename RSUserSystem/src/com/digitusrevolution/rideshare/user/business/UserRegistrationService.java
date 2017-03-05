package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.UserAccount;
import com.digitusrevolution.rideshare.model.user.dto.UserRegistrationDetailDTO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class UserRegistrationService {
	
	private static final Logger logger = LogManager.getLogger(UserRegistrationService.class.getName());
	
	public int registerUser(UserRegistrationDetailDTO userRegistrationDetailDTO){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id = 0;
		try {
			transation = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			User user = getUser(userRegistrationDetailDTO);			
			id = userDO.registerUser(user);
			
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
		
		return id;
	}

	private User getUser(UserRegistrationDetailDTO userRegistrationDetailDTO) {
		User user = new User();
		user.setFirstName(userRegistrationDetailDTO.getFirstName());
		user.setLastName(userRegistrationDetailDTO.getLastName());
		user.setSex(userRegistrationDetailDTO.getSex());
		user.setMobileNumber(userRegistrationDetailDTO.getMobileNumber());
		user.setEmail(userRegistrationDetailDTO.getEmail());
		user.setPassword(userRegistrationDetailDTO.getPassword());
		user.setCity(userRegistrationDetailDTO.getCity());
		user.setState(userRegistrationDetailDTO.getState());
		user.setCountry(userRegistrationDetailDTO.getCountry());
		user.setPhoto(userRegistrationDetailDTO.getPhoto());
		return user;
	}
	
	public void addAccount(UserAccount userAccount){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			UserDO userDO = new UserDO();
			userDO.addAccount(userAccount.getUserId(), userAccount.getAccount());
			
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



























