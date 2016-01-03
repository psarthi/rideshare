package com.digitusrevolution.rideshare.ride;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

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

			//Trust Category needs to be loaded only once
//			dataLoader.loadTrustCategory();
			
//			dataLoader.loadRide();
//			dataLoader.loadRideRequest();
			dataLoader.cleanup();

//			dataLoader.test();
			
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
			
			User driver = RESTClientUtil.getUser(1);
			ride.setDriver(driver);
			
			Vehicle vehicle = RESTClientUtil.getVehicle(driver.getId(), 1);
			ride.setVehicle(vehicle);

			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			ride.setTrustNetwork(trustNetwork);

			// Ride - 1

			PointDO pointDO = new PointDO();
			Point startPoint = pointDO.getCordinates("Gopalan Grandeur Bangalore"); 			
			Point endPoint = pointDO.getCordinates("Silk Board Bangalore");

			RidePoint startRidePoint = new RidePoint();
			startRidePoint.setPoint(startPoint);
			RidePoint endRidePoint = new RidePoint();
			endRidePoint.setPoint(endPoint);
			
			ride.setStartPoint(startRidePoint);
			ride.setEndPoint(endRidePoint);
			
			LocalDateTime localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 9, 30);
			ZoneId india = ZoneId.of("Asia/Kolkata");
			ZonedDateTime startTime = ZonedDateTime.of(localDateTime, india);
			ride.setStartTime(startTime);
			ZonedDateTime startTimeUTC = startTime.withZoneSameInstant(ZoneOffset.UTC);
						
			RouteDO routeDO = new RouteDO();
			GoogleDirection direction = routeDO.getDirection(startPoint, endPoint,startTimeUTC);
			
			int id = rideDO.offerRide(ride,direction);	
			System.out.println("Ride has been created: "+id);

/*			// Ride - 2

			startPoint = pointDO.getCordinates("ITPL Bangalore"); 			
			endPoint = pointDO.getCordinates("Silk Board Bangalore"); 	

			startRidePoint.setPoint(startPoint);
			endRidePoint.setPoint(endPoint);

			ride.setStartPoint(startRidePoint);
			ride.setEndPoint(endRidePoint);
			
			localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 9, 30);
			startTime = ZonedDateTime.of(localDateTime, india);
			ride.setStartTime(startTime);
	
			direction = routeDO.getDirection(startPoint, endPoint);
					
			id = rideDO.offerRide(ride,direction);	
			System.out.println("Ride has been created: "+id);

			// Ride - 3

			startPoint = pointDO.getCordinates("Marathalli Bangalore"); 			
			endPoint = pointDO.getCordinates("Electronic City Bangalore");
			startRidePoint.setPoint(startPoint);
			endRidePoint.setPoint(endPoint);

			
			ride.setStartPoint(startRidePoint);
			ride.setEndPoint(endRidePoint);
			
			localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 9, 30);
			startTime = ZonedDateTime.of(localDateTime, india);
			ride.setStartTime(startTime);
			
			direction = routeDO.getDirection(startPoint, endPoint);
			
			id = rideDO.offerRide(ride,direction);	
			System.out.println("Ride has been created: "+id);

			// Ride - 4

			startPoint = pointDO.getCordinates("Hebbal Bangalore"); 			
			endPoint = pointDO.getCordinates("BTM Layout Bangalore"); 
	
			startRidePoint.setPoint(startPoint);
			endRidePoint.setPoint(endPoint);
			ride.setStartPoint(startRidePoint);
			ride.setEndPoint(endRidePoint);
			
			localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 9, 30);
			startTime = ZonedDateTime.of(localDateTime, india);
			ride.setStartTime(startTime);

			direction = routeDO.getDirection(startPoint, endPoint);
			
			id = rideDO.offerRide(ride,direction);	
			System.out.println("Ride has been created: "+id);*/
			
		}
		
		
		public void loadRideRequest(){
			
			RideRequestDO rideRequestDO = new RideRequestDO();
			RideRequest rideRequest = new RideRequest();
			
			User passenger = RESTClientUtil.getUser(2);
			rideRequest.setPassenger(passenger);
			
			VehicleCategory vehicleCategory = RESTClientUtil.getVehicleCategory(1);
			rideRequest.setVehicleCategory(vehicleCategory);
			
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			TrustCategory trustCategory = trustCategoryDO.get("Anonymous");
	 
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.getTrustCategories().add(trustCategory);
			rideRequest.setTrustNetwork(trustNetwork);

			// Ride Request - 1

			PointDO pointDO = new PointDO();
			Point pickupPoint = pointDO.getCordinates("RMZ Ecospace Bangalore"); 			
			Point dropPoint = pointDO.getCordinates("NPS HSR Bangalore"); 
			
			RideRequestPoint pickupLocation = new RideRequestPoint();
			RideRequestPoint dropLocation = new RideRequestPoint();
			pickupLocation.setPoint(pickupPoint);
			dropLocation.setPoint(dropPoint);
			rideRequest.setPickupPoint(pickupLocation);
			rideRequest.setDropPoint(dropLocation);
			
			rideRequest.setPickupPointVariation(1);
			rideRequest.setDropPointVariation(1);			
			
			LocalDateTime localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 10, 00);
			ZoneId india = ZoneId.of("Asia/Kolkata");
			ZonedDateTime pickupTime = ZonedDateTime.of(localDateTime, india);
			rideRequest.setPickupTime(pickupTime);
			rideRequest.setPickupTimeVariation(LocalTime.of(0, 30));
			
			int id = rideRequestDO.requestRide(rideRequest);	
			System.out.println("Ride Request has been created: "+id);
			
		}

		
		public void test(){
			
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
			RideDO rideDO = new RideDO();
			ZonedDateTime zonedDateTime = rideDO.get(5).getStartTime();
			System.out.println(zonedDateTime.format(formatter)+","+zonedDateTime.getZone());
			
			ZoneId india = ZoneId.of("Asia/Kolkata");
			ZonedDateTime dateTime1 = zonedDateTime.withZoneSameInstant(india);		
			System.out.println(dateTime1.format(formatter)+","+dateTime1.getZone());
						
		}
		
		public void cleanup(){
			
			RideRequestDO rideRequestDO = new RideRequestDO();
			List<RideRequest> rideRequests = rideRequestDO.getAll();
			for (RideRequest rideRequest : rideRequests) {
				rideRequestDO.delete(rideRequest);
			}			
		}
}
