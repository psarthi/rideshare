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
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
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

	public Bill generateBill(MatchedTripInfo matchedTripInfo) {

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		Bill bill = null;
		//Reason for checking Ride Id and Ride Request Id instead of matchedTripInfo, 
		//as we are using matchedTripInfo to store RideId / RideRequestId even if there is no match
		//And this is a common function used in both scenario - offer ride and request ride 
		if (matchedTripInfo.getRideId()!=0 && matchedTripInfo.getRideRequestId()!=0) {
			try {
				transaction = session.beginTransaction();

				RideDO rideDO = new RideDO();
				Ride ride = rideDO.get(matchedTripInfo.getRideId());
				RideRequestDO rideRequestDO = new RideRequestDO();
				RideRequest rideRequest = rideRequestDO.get(matchedTripInfo.getRideRequestId());
				float discountPercentage = 0;
				if (ride.getRideMode().equals(RideMode.Free)) discountPercentage = 100; 

				TripInfo tripInfo = new TripInfo();
				tripInfo.setRide(ride);
				tripInfo.setRideRequest(rideRequest);
				tripInfo.setDiscountPercentage(discountPercentage);

				bill = RESTClientUtil.generateBill(tripInfo);

				transaction.commit();

			} catch (RuntimeException e) {
				if (transaction!=null){
					logger.error("Unable to generate bill. Transaction Failed, Rolling Back");
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
		return bill;
	}
}
