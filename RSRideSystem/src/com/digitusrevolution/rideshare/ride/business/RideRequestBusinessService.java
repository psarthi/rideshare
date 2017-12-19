package com.digitusrevolution.rideshare.ride.business;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.google.Element;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDistance;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

public class RideRequestBusinessService {
	
	private static final Logger logger = LogManager.getLogger(RideRequestBusinessService.class.getName());

	
	public int requestRide(BasicRideRequest rideRequest){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		int rideRequestId = 0;
		try {
			transaction = session.beginTransaction();
						
			//This is written here as there is no difference between calling this API from Android or backend from the cost perspective
			RouteDO routeDO = new RouteDO();
			ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
			GoogleDistance googleDistance = routeDO.getDistance(rideRequest.getPickupPoint().getPoint(), rideRequest.getDropPoint().getPoint(),pickupTimeUTC);
			//This will get first element
			Element element = googleDistance.getRows().get(0).getElements().get(0);
			int travelDistance = element.getDistance().getValue();
			int travelTime = element.getDuration().getValue();
			
			//This will set Pickup and Drop address
			rideRequest.setPickupPointAddress(googleDistance.getOriginAddresses().get(0));
			rideRequest.setDropPointAddress(googleDistance.getDestinationAddresses().get(0));
			
			rideRequest.setTravelDistance(travelDistance);
			rideRequest.setTravelTime(travelTime);
		
			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequestId = rideRequestDO.requestRide(JsonObjectMapper.getMapper().convertValue(rideRequest, RideRequest.class));
			RideDO rideDO = new RideDO();
			rideDO.autoMatchRide(rideRequestId);
			
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
		
		return rideRequestId;
	}

	public RideRequestResult getRideRequestResult(int rideRequestId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		RideRequestResult rideRequestResult = null;
		try {
			transaction = session.beginTransaction();
			
			rideRequestResult = new RideRequestResult();
			RideRequestDO rideRequestDO = new RideRequestDO();
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
			rideRequestResult.setRideRequest(JsonObjectMapper.getMapper().convertValue(rideRequest, FullRideRequest.class));
			RideRequest currentRideRequest = rideRequestDO.getCurrentRideRequest(rideRequest.getPassenger().getId());
			
			if (rideRequest.getId() == currentRideRequest.getId()) {
				rideRequestResult.setCurrentRideRequest(true);
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
		return rideRequestResult;
	}
	
	public List<BasicRideRequest> getRideRequests(int passengerId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<BasicRideRequest> basicRideRequests = new ArrayList<>();
		try {
			transaction = session.beginTransaction();
	
			RideRequestDO rideRequestDO = new RideRequestDO();
			List<RideRequest> rideRequests = rideRequestDO.getAllRideRequests(passengerId);
			//This will sort in descending order
			Collections.sort(rideRequests);
			
			int itemsCount = 10;
			//This will return the result from lets say 0 to 9, 10 to 19, 20 to 29 etc.
			List<RideRequest> subRideRequests = rideRequests.subList(page*itemsCount, (page+1)*itemsCount);
			
			for (RideRequest rideRequest: subRideRequests) {
				basicRideRequests.add(JsonObjectMapper.getMapper().convertValue(rideRequest, BasicRideRequest.class));
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
		return basicRideRequests;
	}
	
	public FullRideRequest getRideRequest(int rideRequestId){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullRideRequest fullRideRequest = null;
		try {
			transaction = session.beginTransaction();
		
			RideRequestDO rideRequestDO = new RideRequestDO();
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
			fullRideRequest = JsonObjectMapper.getMapper().convertValue(rideRequest, FullRideRequest.class);

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
		return fullRideRequest;
	}
	
	public FullRideRequest getCurrentRideRequest(int passengerId){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullRideRequest fullRideRequest = null;
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			RideRequest rideRequest = rideRequestDO.getCurrentRideRequest(passengerId);
			fullRideRequest = JsonObjectMapper.getMapper().convertValue(rideRequest, FullRideRequest.class);
			
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

		return fullRideRequest;
	}
	
	public void cancelRideRequest(int rideRequestId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequestDO.cancelRideRequest(rideRequestId);

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
	
	public void cancelDriver(int rideId, int rideRequestId, float rating){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			logger.debug("Cancelling Driver for ride Id/Ride Request Id:"+rideId+","+rideRequestId);
			RideDO rideDO = new RideDO();
			rideDO.cancelAcceptedRideRequest(rideId, rideRequestId, false);
			//TODO Implement User Feedback later
			
			rideDO.autoMatchRide(rideRequestId);

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
	
	public boolean validatePaymentConfirmationCode(int rideRequestId, String code){
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		RideRequest rideRequest = rideRequestDomainService.get(rideRequestId, false);
		if (rideRequest.getConfirmationCode().equals(code)) return true;
		return false;
	}
	
}



































