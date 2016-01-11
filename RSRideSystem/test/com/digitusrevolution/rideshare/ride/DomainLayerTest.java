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
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer.LatLng;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer.LatLngBounds;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
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

		RideDO rideDO = new RideDO();
		RideRequestDO rideRequestDO = new RideRequestDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(190);
		MultiPolygon polygonAroundRouteUsingRouteBoxer = rideRequestDO.getPolygonAroundRouteUsingRouteBoxer(ridePoints, 5000);
		RideRequestPointDAO rideRequestPointDAO = new RideRequestPointDAO();
		rideRequestPointDAO.getAllMatchingRideRequestWithinMultiPolygon(polygonAroundRouteUsingRouteBoxer);
		

//		rideRequestDO.searchRides(190);
		
	}

	public void routebox(){
		RideDO rideDO = new RideDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(160);
		RouteBoxer routeBoxer = new RouteBoxer();
		List<LatLng> latLngs = new LinkedList<>();
		List<Point> routePoints = new LinkedList<>();
		for (RidePoint ridePoint : ridePoints) {
			LatLng latLng = routeBoxer.new LatLng(ridePoint.getPoint().getLatitude(), ridePoint.getPoint().getLongitude());
			Point point = new Point(ridePoint.getPoint().getLongitude(), ridePoint.getPoint().getLatitude());
			latLngs.add(latLng);
			routePoints.add(point);
		}

		List<LatLngBounds> boxes = routeBoxer.box(latLngs, 10000);
		logger.debug("boxes:" + boxes);
		logger.debug("Total Box:" + boxes.size());

		List<Point> boxSWPoints = new LinkedList<>();
		List<Point> boxNEPoints = new LinkedList<>();
		List<Point> boxNWPoints = new LinkedList<>();
		List<Point> boxSEPoints = new LinkedList<>();
		List<Point> boxTopPoints = new LinkedList<>();
		List<Point> boxBottomPoints = new LinkedList<>();
		List<Point> sqPoints = new LinkedList<>();
		for (LatLngBounds latLngBounds : boxes) {
			Point southWestPoint = new Point(latLngBounds.getSouthWest().lng, latLngBounds.getSouthWest().lat);
			Point northEastPoint = new Point(latLngBounds.getNorthEast().lng, latLngBounds.getNorthEast().lat);
			Point northWestPoint = new Point(latLngBounds.getSouthWest().lng, latLngBounds.getNorthEast().lat);
			Point southEastPoint = new Point(latLngBounds.getNorthEast().lng, latLngBounds.getSouthWest().lat);
			boxSWPoints.add(southWestPoint);
			boxNEPoints.add(northEastPoint);
			boxNWPoints.add(northWestPoint);
			boxSEPoints.add(southEastPoint);
			sqPoints.add(northWestPoint);
			sqPoints.add(northEastPoint);
			sqPoints.add(southEastPoint);
			sqPoints.add(southWestPoint);
			sqPoints.add(northWestPoint);
		}

		List<Point> reverseBottomPoints = new LinkedList<>();
		ListIterator<Point> iterator = boxBottomPoints.listIterator(boxBottomPoints.size());
		while (iterator.hasPrevious()) {
			reverseBottomPoints.add(iterator.previous());		
		}

		List<Point> allPoints = new LinkedList<>();
		allPoints.addAll(sqPoints);				
//		allPoints.addAll(reverseBottomPoints);
		allPoints.add(sqPoints.get(0));

		Polygon polygon = GeoJSONUtil.getPolygonFromPoints(allPoints);
		JSONUtil<Polygon> jsonUtilPolygon = new JSONUtil<>(Polygon.class);
		String polygonGeoJson = jsonUtilPolygon.getJson(polygon);
		logger.debug("polygonGeoJson:"+polygonGeoJson);

		LineString routeLineString = GeoJSONUtil.getLineStringFromPoints(routePoints);
		JSONUtil<LineString> jsonUtilLineString = new JSONUtil<>(LineString.class);
		logger.debug("Route:"+jsonUtilLineString.getJson(routeLineString));

		GeometryCollection geometryCollection = new GeometryCollection();
		geometryCollection.add(polygon);
		geometryCollection.add(routeLineString);
		JSONUtil<GeometryCollection> jsonUtilGeometryCollection = new JSONUtil<>(GeometryCollection.class);
		logger.debug("Geometry:" + jsonUtilGeometryCollection.getJson(geometryCollection));

	}
}
