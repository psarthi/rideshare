package com.digitusrevolution.rideshare.ride.business;


import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

public class RideOfferManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideOfferManagementService.class.getName());
	
	public List<Route> getRoutes(Point startPoint, Point endPoint){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<Route> routes = null;
		try {
			transation = session.beginTransaction();

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
		return routes;

	}
	
	public int offerRide(Ride ride){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		int id =0;
		try {
			transation = session.beginTransaction();

			RideDO rideDO = new RideDO();
			id = rideDO.offerRide(ride);

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
		
		return id;
	}

}
