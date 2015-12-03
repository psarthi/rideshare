package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class VehicleRegistrationService {
	
	private static final Logger logger = LogManager.getLogger(VehicleRegistrationService.class.getName());
	
	public void addVehicle(int userId, Vehicle vehicle){	
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	

		try {			
			transation = session.beginTransaction();

			UserDO userDO = new UserDO();
			User user = userDO.get(userId);
			userDO.setUser(user);
			userDO.addVehicle(vehicle);
			
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
