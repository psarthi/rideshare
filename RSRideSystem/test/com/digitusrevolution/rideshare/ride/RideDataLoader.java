package com.digitusrevolution.rideshare.ride;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
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
			//dataLoader.loadRide();
			dataLoader.test();
			
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
	
			PointDO pointDO = new PointDO();
			Point startPoint = pointDO.getCordinates("Gopalan Grandeur Bangalore"); 
			
			Point endPoint = pointDO.getCordinates("Silk Board Bangalore"); 
			
			ride.setStartPoint(startPoint);
			ride.setEndPoint(endPoint);
			
			LocalDateTime localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 7, 9, 30);
			ZoneId zoneId = ZoneId.of("Asia/Kolkata"); 
			ZonedDateTime startTime = ZonedDateTime.of(localDateTime, zoneId);
			ride.setDateTime(startTime);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			ride.setTrustNetwork(trustNetwork);

			RouteDO routeDO = new RouteDO();
			Route route = routeDO.getRoute(startPoint, endPoint);
						
			ride.setRoute(route);
			
			Vehicle vehicle = restClientUtil.getVehicle(driver.getId(), 1);
			ride.setVehicle(vehicle);
					
			int id = rideDO.offerRide(ride);
		
			ride = rideDO.get(id);
			System.out.println("Route Point Size: "+ride.getRoute().getRoutePoints().size());;

		}
		
		public void test(){
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
			RideDO rideDO = new RideDO();
			ZonedDateTime zonedDateTime = rideDO.get(1).getDateTime();
			System.out.println(zonedDateTime.format(formatter)+","+zonedDateTime.getZone());
			
			
			ZonedDateTime dateTime1 = zonedDateTime.toOffsetDateTime().atZoneSameInstant(ZoneId.of("Australia/Canberra"));		
			System.out.println(dateTime1.format(formatter)+","+dateTime1.getZone());
			
			ZonedDateTime dateTime2 = zonedDateTime.toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"));
			System.out.println(dateTime2.format(formatter)+","+dateTime2.getZone());
			
		}
		
}
