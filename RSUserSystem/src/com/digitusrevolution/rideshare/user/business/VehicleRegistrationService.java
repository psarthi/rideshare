package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.business.dto.VehicleDTO;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;
import com.digitusrevolution.rideshare.user.domain.core.UserDomainService;

public class VehicleRegistrationService {
	
	private static final Logger logger = LogManager.getLogger(VehicleRegistrationService.class.getName());
	
	public void addVehicle(VehicleDTO vehicleDTO){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	

		try {			
			UserDO userDO = new UserDO();
			UserDomainService userDomainService = new UserDomainService();
			transation = session.beginTransaction();
			User user = userDomainService.getChild(vehicleDTO.getUserId());
			userDO.setUser(user);
			userDO.addVehicle(vehicleDTO.getVehicle());
			
			
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
		
	}
	
}
