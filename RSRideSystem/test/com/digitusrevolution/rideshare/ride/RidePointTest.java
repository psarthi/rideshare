package com.digitusrevolution.rideshare.ride;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePointProperty;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;

public class RidePointTest {
	
	public static void main(String[] args) {

		RidePoint ridePoint = new RidePoint();
		ridePoint.getPoint().setLatitude(12.12);
		ridePoint.getPoint().setLongitude(13.13);
		
		RidePointTest ridePointTest = new RidePointTest();
		ridePoint.setRidePointProperties(ridePointTest.getSampleRidePointProperties());
		ridePoint.setSequence(1);
		
		RidePointDAO ridePointDAO = new RidePointDAO();
		String id = ridePointDAO.create(ridePoint);

		JSONUtil<RidePoint> jsonUtil = new JSONUtil<>(RidePoint.class);
		RidePoint ridePoint1 = ridePointDAO.get(id);
		System.out.println(jsonUtil.getJson(ridePoint1));
		RidePoint ridePoint2 = ridePointDAO.getRidePointOfRide(id, 2);
		System.out.println(jsonUtil.getJson(ridePoint2));
		System.out.println("ridePoint1.equals(ridePoint2)"+ridePoint1.equals(ridePoint2));
		System.out.println("ridePoint1.equals(ridePoint1)"+ridePoint1.equals(ridePoint1));
		System.out.println("ridePoint2.equals(ridePoint2)"+ridePoint2.equals(ridePoint2));
		RidePoint ridePoint3 = ridePointDAO.get(id);
		RidePoint ridePoint4 = ridePointDAO.getRidePointOfRide(id,2);
		ridePoint3.set_id("3");
		ridePoint4.set_id("4");
		System.out.println(jsonUtil.getJson(ridePoint3));
		System.out.println(jsonUtil.getJson(ridePoint4));
		
		Set<RidePoint> ridePoints = new HashSet<>();
		ridePoints.add(ridePoint1);
		ridePoints.add(ridePoint1);
		ridePoints.add(ridePoint2);
		ridePoints.add(ridePoint2);
		
		
		System.out.println(ridePoints);
		
/*		ridePoint1.setSequence(2);
		ridePointDAO.update(ridePoint1);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zonedDateTime = ridePoint1.getRidePointProperties().get(0).getDateTime();
 		ZonedDateTime zonedDateTimeIST = zonedDateTime.withZoneSameInstant(india);
		System.out.println("zonedDateTime in UTC: " + zonedDateTime.format(formatter));
		System.out.println("zonedDateTime in IST: " + zonedDateTimeIST.format(formatter));
		
		List<RidePoint> ridePoints = ridePointDAO.getAll();
		FeatureCollection featureCollection = new FeatureCollection();
		JSONUtil<FeatureCollection> jsonUtilFeatureCollection = new JSONUtil<>(FeatureCollection.class);
		for (RidePoint ridePoint2 : ridePoints) {
			System.out.println(jsonUtil.getJson(ridePoint2));
			Feature feature = GeoJSONUtil.getFeatureFromRidePoint(ridePoint2);
			featureCollection.add(feature);
		}
		
		LineString lineString = GeoJSONUtil.getLineStringFromRidePoints(ridePoints);
		featureCollection.add(GeoJSONUtil.getFeatureFromGeometry(lineString));		
		System.out.println(jsonUtilFeatureCollection.getJson(featureCollection));
		
		System.out.println("----");
		
		ridePoints = ridePointDAO.getAllRidePointsOfRide(1);
		
		for (RidePoint ridePoint2 : ridePoints) {
			System.out.println(jsonUtil.getJson(ridePoint2));
		}

		JSONUtil<LineString> jsonUtilLineString = new JSONUtil<>(LineString.class);
		lineString = GeoJSONUtil.getLineStringFromRidePoints(ridePoints);
		System.out.println(jsonUtilLineString.getJson(lineString));*/

	//	ridePointDAO.delete(_id);

		
		System.out.println("End of Program");
	}
	
	public List<RidePointProperty> getSampleRidePointProperties(){
		ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		List<RidePointProperty> ridePointProperties = new ArrayList<>(); 
		RidePointProperty ridePointProperty1 = new RidePointProperty();
		ridePointProperty1.setId(1);
		ridePointProperty1.setDateTime(startDateTime);
		ridePointProperties.add(ridePointProperty1);

		RidePointProperty ridePointProperty2 = new RidePointProperty();
		ridePointProperty2.setId(2);
		ridePointProperty2.setDateTime(startDateTime.plusDays(1));
		ridePointProperties.add(ridePointProperty2);

		RidePointProperty ridePointProperty3 = new RidePointProperty();
		ridePointProperty3.setId(3);
		ridePointProperty3.setDateTime(startDateTime.plusDays(2));
		ridePointProperties.add(ridePointProperty3);
		
		RidePointProperty ridePointProperty4 = new RidePointProperty();
		ridePointProperty4.setId(4);
		ridePointProperty4.setDateTime(startDateTime.plusDays(4));
		ridePointProperties.add(ridePointProperty4);

		return ridePointProperties;
	}

}
