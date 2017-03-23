package com.digitusrevolution.rideshare.ride.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideSystemBusinessService {

	private static final Logger logger = LogManager.getLogger(RideSystemBusinessService.class.getName());

	public FeatureCollection getAllRidePoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getAllRidePoints();			

			transaction.commit();
		} catch (RuntimeException e) {
			if (transaction!=null){
				logger.error("Transaction Failed, Rolling Back");
				transaction.rollback();
				throw e;
			} else {
				logger.error("Transaction Failed with value:"+transaction);
				e.printStackTrace();
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

	public FeatureCollection getRidePoints(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getRidePoints(rideId);			

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

	public FeatureCollection getAllRideRequestPoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getAllRideRequestPoints();			

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

	public FeatureCollection getRideRequestPoints(int rideRequestId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transaction = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getRideRequestPoints(rideRequestId);			

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
}
