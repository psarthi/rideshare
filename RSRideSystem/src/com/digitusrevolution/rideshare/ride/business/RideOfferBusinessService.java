package com.digitusrevolution.rideshare.ride.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

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
	public int offerRide(RideOfferInfo rideOfferInfo){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int rideId = 0;
		try {
			transaction = session.beginTransaction();
			
			BasicRide basicRide = rideOfferInfo.getRide();
			Ride ride = JsonObjectMapper.getMapper().convertValue(basicRide, Ride.class);
			GoogleDirection googleDirection = rideOfferInfo.getGoogleDirection();
						
			RideDO rideDO = new RideDO();
			List<Integer> rideIds = rideDO.offerRide(ride,googleDirection);
			//This will get first ride ID
			rideId = rideIds.get(0);
			//Need to think on the logic of recurring ride
			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequestDO.autoMatchRideRequest(rideId);
			
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
		
		return rideId;
	}


	public RideOfferResult getRideOfferResult(int rideId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RideOfferResult rideOfferResult = null;
		try {
			transaction = session.beginTransaction();
			
			rideOfferResult = new RideOfferResult();
			RideDO rideDO = new RideDO();
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			Ride ride = rideDO.getAllData(rideId);
			rideOfferResult.setRide(JsonObjectMapper.getMapper().convertValue(ride, FullRide.class));
			Ride currentRide = rideDO.getCurrentRide(ride.getDriver().getId());
			
			if (ride.getId() == currentRide.getId()) {
				rideOfferResult.setCurrentRide(true);
			}
			
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
		return rideOfferResult;
	}

	public List<FullRide> getRides(int driverId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<FullRide> fullRides = new ArrayList<>();
		try {
			transaction = session.beginTransaction();
	
			RideDO rideDO = new RideDO();
			List<Ride> rides = rideDO.getAllRides(driverId);
			//This will sort in descending order
			Collections.sort(rides);
			
			int itemsCount = 10;
			//This will return the result from lets say 0 to 9, 10 to 19, 20 to 29 etc.
			List<Ride> subRides = rides.subList(page*itemsCount, (page+1)*itemsCount);
			
			for (Ride ride: subRides) {
				fullRides.add(JsonObjectMapper.getMapper().convertValue(ride, FullRide.class));
			}
			
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
		return fullRides;
	}

	public FullRide getRide(int rideId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullRide fullRide = null;
		try {
			transaction = session.beginTransaction();
			
			RideDO rideDO = new RideDO();
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			Ride ride = rideDO.getAllData(rideId);
			fullRide = JsonObjectMapper.getMapper().convertValue(ride, FullRide.class);
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
		return fullRide;
	}
	
	public FullRide getCurrentRide(int driverId){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullRide fullRide = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			Ride ride = rideDO.getCurrentRide(driverId);
			fullRide = JsonObjectMapper.getMapper().convertValue(ride, FullRide.class);

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

		return fullRide;
	}
	
	public void startRide(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.startRide(rideId);

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
	
	public void endRide(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.endRide(rideId);

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
	
	public void pickupPassenger(int rideId, int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.pickupPassenger(rideId, rideRequestId);

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
	
	public void dropPassenger(int rideId, int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		FullRide fullRide = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.dropPassenger(rideId, rideRequestId);

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
	
	public void cancelRide(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.cancelRide(rideId);

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
	
	public void cancelPassenger(int rideId, int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			logger.debug("Cancelling Passenger for ride Id/Ride Request Id:"+rideId+","+rideRequestId);
			RideDO rideDO = new RideDO();
			rideDO.cancelAcceptedRideRequest(rideId, rideRequestId, false);
			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequestDO.autoMatchRideRequest(rideId);

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
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
