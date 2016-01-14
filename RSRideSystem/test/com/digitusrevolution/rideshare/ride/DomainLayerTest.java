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
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.domain.resource.RideRequestDomainResource;

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

		RideRequestDO rideRequestDO = new RideRequestDO();
//		rideRequestDO.searchRides(160);
		
		RideDO rideDO = new RideDO();
		List<Ride> rides = rideDO.getAll();
		for (Ride ride : rides) {
			LatLng from = new LatLng(ride.getStartPoint().getPoint().getLatitude(), ride.getStartPoint().getPoint().getLongitude());
			LatLng to = new LatLng(ride.getEndPoint().getPoint().getLatitude(), ride.getEndPoint().getPoint().getLongitude());
			int distance = (int) SphericalUtil.computeDistanceBetween(from, to);
			ride.setTravelDistance(distance);
			rideDO.update(ride);
		}
		
	}
}
