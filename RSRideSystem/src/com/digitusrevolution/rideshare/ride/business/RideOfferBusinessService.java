package com.digitusrevolution.rideshare.ride.business;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDistance;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
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
						
 			//Start - Temp. Code to work with Web frontend, it will be removed and direction needs to be passed as a parameter to this call
			ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
			RouteDO routeDO = new RouteDO();
			GoogleDirection direction = routeDO.getDirection(ride.getStartPoint().getPoint(), ride.getEndPoint().getPoint(),startTimeUTC);

			RideDO rideDO = new RideDO();
			List<Integer> rideIds = rideDO.offerRide(ride,direction);
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

}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
