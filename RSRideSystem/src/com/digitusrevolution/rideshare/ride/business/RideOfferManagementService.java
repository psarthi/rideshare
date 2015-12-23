package com.digitusrevolution.rideshare.ride.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

public class RideOfferManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideOfferManagementService.class.getName());
	
	public int offerRide(Ride ride, GoogleDirection direction){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id =0;
		try {
			transation = session.beginTransaction();

			RideDO rideDO = new RideDO();
			id = rideDO.offerRide(ride,direction);

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

}
