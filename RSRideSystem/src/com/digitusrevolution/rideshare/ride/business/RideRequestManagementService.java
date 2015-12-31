package com.digitusrevolution.rideshare.ride.business;

import java.time.LocalTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideRequestManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideRequestManagementService.class.getName());
	
	public int requestRide(RideRequest rideRequest){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id =0;
		try {
			transation = session.beginTransaction();
			
			//Start - Temp. Code to work with Web frontend 
			User passenger = RESTClientUtil.getUser(2);
			rideRequest.setPassenger(passenger);
			
			VehicleCategory vehicleCategory = RESTClientUtil.getVehicleCategory(1);
			rideRequest.setVehicleCategory(vehicleCategory);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			rideRequest.setTrustNetwork(trustNetwork);
			
			rideRequest.setPickupPointVariation(5000);
			LocalTime timeVariation = LocalTime.of(0, 30);
			rideRequest.setPickupTimeVariation(timeVariation);
			rideRequest.setDropPointVariation(5000);
			//End
			
			RideRequestDO rideRequestDO = new RideRequestDO();
			id = rideRequestDO.requestRide(rideRequest);

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
	
	public void respondToRideRequest(RideRequest rideRequest){
		
	}

	public void notifyDrivers(RideRequest rideRequest, List<User> drivers){
		
	}

	public void notifyPassenger(int passengerId){
		
	}
}
