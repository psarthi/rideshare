package com.digitusrevolution.rideshare.user.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.user.domain.core.UserDO;

public class VehicleBusinessService {
	
	private static final Logger logger = LogManager.getLogger(VehicleBusinessService.class.getName());
	
	public void addVehicle(long userId, Vehicle vehicle){	
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	

		try {			
			transaction = session.beginTransaction();

			UserDO userDO = new UserDO();
			User user = userDO.get(userId);
			userDO.setUser(user);
			userDO.addVehicle(vehicle);
			
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
	
	}
}
