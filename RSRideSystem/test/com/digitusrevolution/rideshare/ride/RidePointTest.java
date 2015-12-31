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
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;

public class RidePointTest {
	
	public static void main(String[] args) {

		RidePoint ridePoint = new RidePoint();
		ridePoint.getPoint().setLatitude(12.12);
		ridePoint.getPoint().setLongitude(13.13);
		
		RidePointTest ridePointTest = new RidePointTest();
		ridePoint.setRidesBasicInfo(ridePointTest.getSampleRidesBasicInfo());
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
		ZonedDateTime zonedDateTime = ridePoint1.getRidesBasicInfo().get(0).getDateTime();
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
	
	public List<RideBasicInfo> getSampleRidesBasicInfo(){
		ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		List<RideBasicInfo> ridesBasicInfo = new ArrayList<>(); 
		RideBasicInfo rideBasicInfo1 = new RideBasicInfo();
		rideBasicInfo1.setId(1);
		rideBasicInfo1.setDateTime(startDateTime);
		ridesBasicInfo.add(rideBasicInfo1);

		RideBasicInfo rideBasicInfo2 = new RideBasicInfo();
		rideBasicInfo2.setId(2);
		rideBasicInfo2.setDateTime(startDateTime.plusDays(1));
		ridesBasicInfo.add(rideBasicInfo2);

		RideBasicInfo rideBasicInfo3 = new RideBasicInfo();
		rideBasicInfo3.setId(3);
		rideBasicInfo3.setDateTime(startDateTime.plusDays(2));
		ridesBasicInfo.add(rideBasicInfo3);
		
		RideBasicInfo rideBasicInfo4 = new RideBasicInfo();
		rideBasicInfo4.setId(4);
		rideBasicInfo4.setDateTime(startDateTime.plusDays(4));
		ridesBasicInfo.add(rideBasicInfo4);

		return ridesBasicInfo;
	}

}
