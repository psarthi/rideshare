package com.digitusrevolution.rideshare.ride.business;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.PreBookingRideRequestResult;
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
			//IMP - Its important to get duration in traffic instead of using standard duration as traffic duration would cover actual time in traffic condition
			//This is not applicable for Ride Offer as we are calculating time of each point based on speed and distance and for google route api as 
			//there is no traffic time for each steps, its only at higher level on the overall time in traffic which would not be applicable for offer ride
			int travelTime = element.getDurationInTraffic().getValue();

			//This will set Pickup and Drop address
			//Actually address should be set from Places API in Android itself, to avoid ambiguity
			if (rideRequest.getPickupPointAddress()==null) rideRequest.setPickupPointAddress(googleDistance.getOriginAddresses().get(0));
			if (rideRequest.getDropPointAddress()==null) rideRequest.setDropPointAddress(googleDistance.getDestinationAddresses().get(0));

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
			List<RideRequest> rideRequests = rideRequestDO.getRideRequests(passengerId, page);

			for (RideRequest rideRequest: rideRequests) {
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

			RideSystemBusinessService systemBusinessService = new RideSystemBusinessService();
			boolean status = systemBusinessService.giveUserFeedback(rideId, rideRequestId, rating, RideType.RequestRide);
			if (status) {
				logger.debug("Cancelling Driver for ride Id/Ride Request Id:"+rideId+","+rideRequestId);
				RideDO rideDO = new RideDO();
				rideDO.cancelAcceptedRideRequest(rideId, rideRequestId, CancellationType.Driver);
				rideDO.autoMatchRide(rideRequestId);
			} else {
				throw new WebApplicationException("User Feedback failed, so unable to cancel driver");
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
	}

	public boolean validatePaymentConfirmationCode(int rideRequestId, String code){
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		RideRequest rideRequest = rideRequestDomainService.get(rideRequestId, false);
		if (rideRequest.getConfirmationCode().equals(code)) return true;
		return false;
	}

	public PreBookingRideRequestResult getPreBookingInfo(BasicRideRequest basicRideRequest) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		PreBookingRideRequestResult preBookingRideRequestResult = null;
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			RideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(basicRideRequest, RideRequest.class);
			preBookingRideRequestResult = rideRequestDO.getPreBookingInfo(rideRequest);
			
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
		return preBookingRideRequestResult;
	}
}



































