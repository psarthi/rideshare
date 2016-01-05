package com.digitusrevolution.rideshare.ride.business;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

public class RideOfferManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideOfferManagementService.class.getName());
	
	public int offerRide(Ride ride){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id =0;
		try {
			transation = session.beginTransaction();
			
			//Start - Temp. Code to work with Web frontend, it will be removed and direction needs to be passed as a parameter to this call
			ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
			RouteDO routeDO = new RouteDO();
			GoogleDirection direction = routeDO.getDirection(ride.getStartPoint().getPoint(), ride.getEndPoint().getPoint(),startTimeUTC);
			
			User driver = RESTClientUtil.getUser(1);
			ride.setDriver(driver);
			
			Vehicle vehicle = RESTClientUtil.getVehicle(driver.getId(), 1);
			ride.setVehicle(vehicle);

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			ride.setTrustNetwork(trustNetwork);
			
			ride.setRecur(false);
			//End
			
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
