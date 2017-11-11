package com.digitusrevolution.rideshare.ride.business;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

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
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferDTO;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideOfferBusinessService {
	
	private static final Logger logger = LogManager.getLogger(RideOfferBusinessService.class.getName());
	
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
	public List<Integer> offerRide(RideOfferDTO rideOfferDTO){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<Integer> rideIds = null;
		try {
			transaction = session.beginTransaction();
			
			Ride ride = rideOfferDTO.getRide();
			GoogleDirection googleDirection = rideOfferDTO.getGoogleDirection();
			
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
	
	public void acceptRideRequest(int rideId, int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.acceptRideRequest(rideId, rideRequestId);;			

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
	
	public Ride getUpcomingRide(int driverId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Ride upcomingRide = null;
		try {
			transaction = session.beginTransaction();
		
			RideDO rideDO = new RideDO();
			upcomingRide = rideDO.getUpcomingRide(driverId);

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
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
