package com.digitusrevolution.rideshare.ride;

import java.util.HashMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.RoutePoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;

@Path("/domain/loadsample/ride")
public class RideDataLoader {
	
	private static final Logger logger = LogManager.getLogger(RideDataLoader.class.getName());
	
	@GET
	public Response load(){
		logger.info("Inside Ride DataLoader");
		String[] args ={};
		RideDataLoader.main(args);
		return Response.ok().build();
	}
	
	@GET
	@Path("/prereq")
	public Response prereq(){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			RideDataLoader dataLoader = new RideDataLoader();
			dataLoader.loadTrustCategory();
			
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
		
		return Response.ok().build();
		
	}
	
	public static void main(String args[]){
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transation = null;	
		try {
			transation = session.beginTransaction();
			
			RideDataLoader dataLoader = new RideDataLoader();
			dataLoader.loadRide();
			
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
	
		public void loadTrustCategory(){
			TrustCategory trustCategory = new TrustCategory();
			trustCategory.setName("Anonymous");

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			trustCategoryDO.create(trustCategory);			
		}
		
		public void loadRide(){
			
			RideDO rideDO = new RideDO();
			Ride ride = new Ride();
			
			RESTClientUtil restClientUtil = new RESTClientUtil();
			User driver = restClientUtil.getUser(1);
			ride.setDriver(driver);
			
			Point point = new Point();
			point.setLatitude(1.12);
			point.setLongitude(2.11);
			
			Point point1 = new Point();
			point1.setLatitude(3.12);
			point1.setLongitude(4.11);

			ride.setStartPoint(point);
			ride.setEndPoint(point1);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			ride.setTrustNetwork(trustNetwork);

			
			Route route = new Route();
			
			
			Point point2 = new Point();
			point2.setLatitude(5.12);
			point2.setLongitude(6.11);
			
			RoutePoint routePoint = new RoutePoint();
			routePoint.setPoint(point);
			routePoint.setSequence(1);
			route.getRoutePoints().add(routePoint);

			RoutePoint routePoint1 = new RoutePoint();
			routePoint1.setPoint(point1);
			routePoint1.setSequence(2);
			route.getRoutePoints().add(routePoint1);
			
			RoutePoint routePoint2 = new RoutePoint();
			routePoint2.setPoint(point2);
			routePoint2.setSequence(3);
			route.getRoutePoints().add(routePoint2);
			
			ride.setRoute(route);
			
			Vehicle vehicle = restClientUtil.getVehicle(driver.getId(), 1);
			ride.setVehicle(vehicle);
					
			int id = rideDO.offerRide(ride);
		
			ride = rideDO.get(id);
			System.out.println("Route Point Size: "+ride.getRoute().getRoutePoints().size());;

			
		}
		
}
