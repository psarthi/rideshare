package com.digitusrevolution.rideshare.ride.business;

import java.util.List;

import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.common.PropertyReader;
import com.digitusrevolution.rideshare.common.RESTClientImpl;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.common.inf.RESTClient;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.ride.domain.core.RideDomainService;


public class RideOfferManagementService {
	
	private static final Logger logger = LogManager.getLogger(RideOfferManagementService.class.getName());
	
	public List<Route> getRoutes(Point startPoint, Point endPoint){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		List<Route> routes = null;
		try {
			transation = session.beginTransaction();

			RideDomainService rideDomainService = new RideDomainService();
			routes = rideDomainService.getRoutes(startPoint, endPoint);
			
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
			
			int userId = ride.getDriver().getId();
			User user = RESTClientUtil.getUser(userId);

			RideDomainService rideDomainService = new RideDomainService();
			id = rideDomainService.create(ride);
			
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
