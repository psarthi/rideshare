package com.digitusrevolution.rideshare.ride;

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideSearchTest {

	private static final Logger logger = LogManager.getLogger(RideSearchTest.class.getName());
	
	public static void main(String[] args) {
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();
			
			RideSearchTest rideSearchTest = new RideSearchTest();
			rideSearchTest.searchRide();
			transaction.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
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
	
	public void searchRide(){
		
		RideDO rideDO = new RideDO();
		rideDO.searchRides(42, false);
//		rideRequest = rideRequestDO.get(2);
//		rideRequestDO.searchRides(rideRequest);
		
	}
	
	public void searchRideTest(){
		
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.get(36);
		
		RidePointDAO ridePointDAO = new RidePointDAO();
		double maxDistance = 100000000;
		double minDistance = 0;
		LocalTime localTime = LocalTime.of(0, 30);
		
		ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getPickupPoint(), false);
		Set<RidePoint> ridePointsSet = new HashSet<>();
		List<RidePoint> ridePointsSorted = new LinkedList<>();


/*		for (RidePoint ridePoint : ridePoints) {
			boolean status = ridePointsSet.add(ridePoint);
			if (status){
				logger.debug("****"+status+","+ridePoint.toString());
				ridePointsSorted.add(ridePoint);
			} else {
				//logger.debug(status+","+ridePoint.toString());				
			}

		}
		
		logger.debug("Total Ride points:"+ridePoints.size());
		logger.debug("Total Closest Ride points:"+ridePointsSet.size());
		for (RidePoint ridePoint : ridePointsSet) {
			logger.debug(ridePoint.toString());
		}
		int i = 0;
		for (RidePoint ridePoint : ridePointsSorted) {
			logger.debug("["+i+"]"+ridePoint.toString());
			i++;
		}*/
		
	}
	

}
