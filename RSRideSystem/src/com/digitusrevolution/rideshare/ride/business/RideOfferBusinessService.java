package com.digitusrevolution.rideshare.ride.business;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

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
	public long offerRide(RideOfferInfo rideOfferInfo){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		long rideId = 0;
		try {
			transaction = session.beginTransaction();

			BasicRide basicRide = rideOfferInfo.getRide();
			Ride ride = JsonObjectMapper.getMapper().convertValue(basicRide, Ride.class);
			GoogleDirection googleDirection = rideOfferInfo.getGoogleDirection();

			RideDO rideDO = new RideDO();
			List<Long> rideIds = rideDO.offerRide(ride,googleDirection);
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


	public RideOfferResult getRideOfferResult(long rideId){

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

	public List<BasicRide> getRides(long driverId, int page){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		List<BasicRide> basicRides = new ArrayList<>();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			List<Ride> rides = rideDO.getRides(driverId,page);

			for (Ride ride: rides) {
				basicRides.add(JsonObjectMapper.getMapper().convertValue(ride, BasicRide.class));
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
		return basicRides;
	}

	public FullRide getRide(long rideId){

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

	public FullRide getCurrentRide(long driverId){

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

	public void startRide(long rideId){
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

	public void endRide(long rideId){
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

	public void pickupPassenger(long rideId, long rideRequestId){
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

	public void dropPassenger(long rideId, long rideRequestId, RideMode rideMode, String paymentCode){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			rideDO.dropPassenger(rideId, rideRequestId, rideMode, paymentCode);

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

	public void cancelRide(long rideId){
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

	public void cancelPassenger(long rideId, long rideRequestId, float rating){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;
		try {
			transaction = session.beginTransaction();
			
			RideSystemBusinessService systemBusinessService = new RideSystemBusinessService();
			boolean status = systemBusinessService.giveUserFeedback(rideId, rideRequestId, rating, RideType.OfferRide);
			if (status) {
				logger.debug("Cancelling Passenger for ride Id/Ride Request Id:"+rideId+","+rideRequestId);
				RideDO rideDO = new RideDO();
				rideDO.cancelAcceptedRideRequest(rideId, rideRequestId, CancellationType.Passenger);
				RideRequestDO rideRequestDO = new RideRequestDO();
				rideRequestDO.autoMatchRideRequest(rideId);				
			} else {
				throw new WebApplicationException("User Feedback failed, so unable to cancel pasenger");
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

	public boolean makePayment(long rideRequestId) {

		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		RideRequest rideRequest = rideRequestDomainService.get(rideRequestId, true);
		BillInfo billInfo = new BillInfo();
		billInfo.setBillNumber(rideRequest.getBill().getNumber());
		if (rideRequest.getRideMode().equals(RideMode.Paid)) {
			return RESTClientUtil.makePayment(billInfo);	
		} 
		//This will be the case when its a free ride request and no payment is required
		return true;
	}

}




























