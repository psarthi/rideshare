package com.digitusrevolution.rideshare.ride.business;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDistance;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideSystemBusinessService {

	private static final Logger logger = LogManager.getLogger(RideSystemBusinessService.class.getName());

	/*
	 * Purpose - Create a ride in the system
	 * 
	 * @param Ride - Ride details
	 * @param GoogleDirection - Containing the exact route information which user would take for the ride 
	 * 
	 * Note -
	 * 
	 * Providing route options is responsibility of front end and while calling the service, it should send the exact route and not multiple routes
	 * 
	 * 
	 */
	public List<Integer> offerRide(RideOfferInfo rideOfferInfo){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Integer> rideIds = null;
		try {
			transaction = session.beginTransaction();
			
			BasicRide basicRide = rideOfferInfo.getRide();
			Ride ride = JsonObjectMapper.getMapper().convertValue(basicRide, Ride.class);
			GoogleDirection googleDirection = rideOfferInfo.getGoogleDirection();
			
			//Start - Temp. Code to work with Web frontend, it will be removed and direction needs to be passed as a parameter to this call
			ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
			RouteDO routeDO = new RouteDO();
			GoogleDirection direction = routeDO.getDirection(ride.getStartPoint().getPoint(), ride.getEndPoint().getPoint(),startTimeUTC);
			
			User driver = RESTClientUtil.getUser(1);
			ride.setDriver(driver);
			
			Vehicle vehicle = RESTClientUtil.getVehicle(driver.getId(), 1);
			ride.setVehicle(vehicle);
			
			ride.setSeatOffered(2);

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get(TrustCategoryName.Anonymous.toString());
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			ride.setTrustNetwork(trustNetwork);
			
			ride.setRecur(false);
			//End
			
			RideDO rideDO = new RideDO();
			//Replace direction with googleDirection when you get that from front end.
			rideIds = rideDO.offerRide(ride,direction);

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
		
		return rideIds;
	}

	public int requestRide(RideRequest rideRequest){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int id =0;
		try {
			transaction = session.beginTransaction();
			
			//Start - Temp. Code to work with Web frontend. This data should be populated in request 
			User passenger = RESTClientUtil.getUser(2);
			rideRequest.setPassenger(passenger);
			
			VehicleCategory vehicleCategory = RESTClientUtil.getVehicleCategory(1);
			rideRequest.setVehicleCategory(vehicleCategory);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get(TrustCategoryName.Anonymous.toString());
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
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
		
		return id;
	}
	
	public FeatureCollection getAllRidePoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getAllRidePoints();			

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			} else {
				logger.error("Transaction Failed with value:"+transaction);
				e.printStackTrace();
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

	public FeatureCollection getRidePoints(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getRidePoints(rideId);			

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
		return featureCollection;	
	}

	public FeatureCollection getAllRideRequestPoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getAllRideRequestPoints();			

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
		return featureCollection;	

	}

	public FeatureCollection getRideRequestPoints(int rideRequestId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getRideRequestPoints(rideRequestId);			

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
		return featureCollection;	
	}
		
	public List<Ride> getAllUpcomingRides(int driverId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Ride> upcomingRides = null;
		try {
			transaction = session.beginTransaction();
		
			RideDO rideDO = new RideDO();
			upcomingRides = rideDO.getAllUpcomingRides(driverId);

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
		
		return upcomingRides;
	}
	
	public FeatureCollection getMatchingRides(int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getMatchingRides(rideRequestId);			

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
		return featureCollection;	
	}
	
	public void rejectRideRequest(int rideId, int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.rejectRideRequest(rideId, rideRequestId);			

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
	
	public Ride getCurrentRide(int driverId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Ride upcomingRide = null;
		try {
			transaction = session.beginTransaction();
		
			RideDO rideDO = new RideDO();
			upcomingRide = rideDO.getCurrentRide(driverId);

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
		
		return upcomingRide;
	}
	

	public FeatureCollection getMatchingRideRequests(int rideId,double lastSearchDistance, int lastResultIndex){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = null;
		try {
			transaction = session.beginTransaction();
			
			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);

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
		
		return featureCollection;
		
	}
	
	public RideRequest getCurrentRideRequest(int passengerId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RideRequest upcomingRideRequest = null;
		try {
			transaction = session.beginTransaction();
		
			RideRequestDO rideRequestDO = new RideRequestDO();
			upcomingRideRequest = rideRequestDO.getCurrentRideRequest(passengerId);

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
		
		return upcomingRideRequest;
	}
}
