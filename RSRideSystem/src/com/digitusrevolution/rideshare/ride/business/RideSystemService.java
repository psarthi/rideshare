package com.digitusrevolution.rideshare.ride.business;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideSystemService {

	private static final Logger logger = LogManager.getLogger(RideSystemService.class.getName());

	public FeatureCollection getAllRidePoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transation = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getAllRidePoints();			

			transation.commit();
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
		return featureCollection;	
	}

	public FeatureCollection getRidePoints(int rideId){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transation = session.beginTransaction();

			RideDO rideDO = new RideDO();
			featureCollection = rideDO.getRidePoints(rideId);			

			transation.commit();
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
		return featureCollection;	
	}

	public FeatureCollection getAllRideRequestPoints(){
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transation = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getAllRideRequestPoints();			

			transation.commit();
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
		return featureCollection;	

	}

	public FeatureCollection getRideRequestPoints(int rideRequestId) {
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		FeatureCollection featureCollection = new FeatureCollection();
		try {
			transation = session.beginTransaction();

			RideRequestDO rideRequestDO = new RideRequestDO();
			featureCollection = rideRequestDO.getRideRequestPoints(rideRequestId);			

			transation.commit();
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
		return featureCollection;	
	}

	//TBD - It will be based on front end and notification mechanism
	public void notifyDrivers(){		
		
	}

}
