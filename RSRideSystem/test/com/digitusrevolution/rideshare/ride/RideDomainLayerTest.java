package com.digitusrevolution.rideshare.ride;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.MultiPoint;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.digitusrevolution.rideshare.common.db.HibernateUtil;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.PolyUtil;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.model.billing.dto.TripInfo;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.TrustCategoryDO;
import com.digitusrevolution.rideshare.ride.domain.TrustNetworkDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.domain.resource.RideRequestDomainResource;

public class RideDomainLayerTest {

	private static final Logger logger = LogManager.getLogger(RideDomainLayerTest.class.getName());

	public static void main(String args[]){

		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
		Transaction transaction = null;	
		try {
			transaction = session.beginTransaction();

			RideDomainLayerTest domainLayerTest = new RideDomainLayerTest();
			domainLayerTest.test();
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

	public void test(){
	
		RideDO rideDO = new RideDO();
//		rideDO.acceptRideRequest(1, 1);
//		Ride currentRide = rideDO.getUpcomingRide(1);
//		System.out.println("Current Ride Id is:"+currentRide.getId());
//		System.out.println("Current Ride time is:"+currentRide.getStartTime());
		
//		rideDO.getUpcomingRides(1);
		
//		RideRequestDO rideRequestDO = new RideRequestDO();
//		rideRequestDO.cancelRideRequest(2);
		
	//	RideDO rideDO = new RideDO();
	//	rideDO.acceptRideRequest(2, 2);
	//	rideDO.cancelRideRequest(1,1);
	//	rideDO.startRide(1);
	//	rideDO.pickupPassenger(1, 2);
	//	rideDO.dropPassenger(1, 2);
	//	rideDO.endRide(1);
	//	rideDO.cancelRide(1);

		
//		Ride ride = rideDO.get(1);
//		RideRequestDO rideRequestDO = new RideRequestDO();
//		RideRequest rideRequest = rideRequestDO.get(1);
//		RideDTO rideDTO = new RideDTO();
//		rideDTO.setRide(ride);
//		rideDTO.setRideRequest(rideRequest);
//		RESTClientUtil.generateBill(rideDTO);
		
//		rideDO.delete(1);
//		rideDO.startRide(165);
//		rideDO.pickupPassenger(165, 3);
//		rideDO.dropPassenger(165, 2);
//		rideDO.acceptRideRequest(166, 68);
//		rideDO.rejectRideRequest(163, 58);
				
//		List<Ride> rides = rideDO.getAll();
//		for (Ride ride : rides) {
//			ride.setSeatOffered(2);
//			rideDO.update(ride);
//		}
//		
//		RideRequestDO rideRequestDO = new RideRequestDO();
//		List<RideRequest> rideRequests = rideRequestDO.getAll();
//		for (RideRequest rideRequest : rideRequests) {
//			rideRequest.setSeatRequired(1);
//			rideRequestDO.update(rideRequest);
//		}
	}
}

































