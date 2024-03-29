package com.digitusrevolution.rideshare.ride.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RideType;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRidesInfo;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.UserFeedbackInfo;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideSystemBusinessService {

	private static final Logger logger = LogManager.getLogger(RideSystemBusinessService.class.getName());

	public FullRidesInfo getCurrentRides(long userId) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FullRidesInfo fullRidesInfo = new FullRidesInfo();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			Ride ride = rideDO.getCurrentRide(userId);
			RideRequestDO rideRequestDO = new RideRequestDO();
			RideRequest rideRequest = rideRequestDO.getCurrentRideRequest(userId);
			fullRidesInfo.setRide(JsonObjectMapper.getMapper().convertValue(ride, FullRide.class));
			fullRidesInfo.setRideRequest(JsonObjectMapper.getMapper().convertValue(rideRequest, FullRideRequest.class));

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
		return fullRidesInfo;
	}

	public boolean giveUserFeedback(long rideId, long rideRequestId, float rating, RideType rideType) {
		RideDO rideDO = new RideDO();
		Ride ride = rideDO.get(rideId);
		RideRequestDO requestDO = new RideRequestDO();
		RideRequest rideRequest = requestDO.get(rideRequestId);
		User givenByUser;
		long forUserId;
		if (rideType.equals(RideType.OfferRide)) {
			givenByUser = ride.getDriver();
			forUserId = rideRequest.getPassenger().getId();
		} else {
			givenByUser = rideRequest.getPassenger();
			forUserId = ride.getDriver().getId();
		}

		UserFeedbackInfo feedbackInfo = new UserFeedbackInfo();
		BasicRide basicRide = JsonObjectMapper.getMapper().convertValue(ride, BasicRide.class);
		BasicRideRequest basicRideRequest = JsonObjectMapper.getMapper().convertValue(rideRequest, BasicRideRequest.class);
		BasicUser givenByBasicUser = JsonObjectMapper.getMapper().convertValue(givenByUser, BasicUser.class);

		feedbackInfo.setRide(basicRide);
		feedbackInfo.setRideRequest(basicRideRequest);
		feedbackInfo.setRating(rating);
		feedbackInfo.setGivenByUser(givenByBasicUser);
		boolean status = RESTClientUtil.userFeedback(forUserId, feedbackInfo, rideType);
		return status;
	}


}
