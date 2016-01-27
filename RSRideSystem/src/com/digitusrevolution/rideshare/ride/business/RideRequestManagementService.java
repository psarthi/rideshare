package com.digitusrevolution.rideshare.ride.business;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDistance;

public class RideRequestManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideRequestManagementService.class.getName());
	
	public int requestRide(RideRequest rideRequest){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id =0;
		try {
			transation = session.beginTransaction();
			
			//Start - Temp. Code to work with Web frontend. This data should be populated in request 
			User passenger = RESTClientUtil.getUser(2);
			rideRequest.setPassenger(passenger);
			
			VehicleCategory vehicleCategory = RESTClientUtil.getVehicleCategory(1);
			rideRequest.setVehicleCategory(vehicleCategory);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get(TrustCategoryName.Anonymous.toString());
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			trustNetwork.setTrustCategories(trustNetwork.getTrustCategories());
			rideRequest.setTrustNetwork(trustNetwork);
			
			rideRequest.setPickupPointVariation(1000);
			LocalTime timeVariation = LocalTime.of(0, 30);
			rideRequest.setPickupTimeVariation(timeVariation);
			rideRequest.setDropPointVariation(1000);
			
			RouteDO routeDO = new RouteDO();
			ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
			GoogleDistance googleDistance = routeDO.getDistance(rideRequest.getPickupPoint().getPoint(), rideRequest.getDropPoint().getPoint(),pickupTimeUTC);	
			int travelDistance = googleDistance.getRows().get(0).getElements().get(0).getDistance().getValue();
			int travelTime = googleDistance.getRows().get(0).getElements().get(0).getDuration().getValue();

			rideRequest.setTravelDistance(travelDistance);
			rideRequest.setTravelTime(travelTime);
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
	
	
	public void respondToRideRequest(){
		
	}
	
	//TBD - It will be based on front end and notification mechanism
	public void notifyPassenger(){
		
	}
	
	public FeatureCollection getMatchingRideRequests(int rideId,double lastSearchDistance, int lastResultIndex){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		FeatureCollection featureCollection = null;
		try {
			transation = session.beginTransaction();
			
			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);

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
		
		return featureCollection;
		
	}
	
}
