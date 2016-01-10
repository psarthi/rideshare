package com.digitusrevolution.rideshare.ride;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class DomainLayerTest {
	
	private static final Logger logger = LogManager.getLogger(DomainLayerTest.class.getName());

	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			DomainLayerTest domainLayerTest = new DomainLayerTest();
			domainLayerTest.test();
			transation.commit();

			/*
			 * Reason for catching RuntimeException and not HibernateException as all exceptions thrown by Hibernate
			 * is not of type HibernateException such as NotFoundException
			 */
		} catch (RuntimeException e) {
			if (transation!=null){
				logger.error("Transaction Failed, Rolling Back");
				transation.rollback();
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
	
	public void test(){

			RideDO rideDO = new RideDO();
			List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(109);
			RideRequestPointDAO rideRequestPointDAO = new RideRequestPointDAO();
			rideRequestPointDAO.getAllMatchingRideRequestPointNearGivenPoint(ridePoints);

	}
}
