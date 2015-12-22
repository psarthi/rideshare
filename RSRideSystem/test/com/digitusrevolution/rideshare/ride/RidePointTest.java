package com.digitusrevolution.rideshare.ride;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;

public class RidePointTest {
	
	public static void main(String[] args) {
		
		RidePoint ridePoint = new RidePoint();
		ridePoint.getPoint().setLatitude(12.12);
		ridePoint.getPoint().setLongitude(13.13);
		ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		ridePoint.setDateTime(zonedDateTime);
		ridePoint.setRideId(1);
		ridePoint.setSequence(1);
		
		RidePointDAO ridePointDAO = new RidePointDAO();
		String id = ridePointDAO.create(ridePoint);
		
		RidePoint ridePoint1 = ridePointDAO.get(id);
		JSONUtil<RidePoint> jsonUtil = new JSONUtil<>(RidePoint.class);
		System.out.println(jsonUtil.getJson(ridePoint1));
		
		ridePoint1.setRideId(2);
		ridePointDAO.update(ridePoint1);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZonedDateTime zonedDateTime1 = ridePoint1.getDateTime().withZoneSameInstant(india);
		System.out.println("zonedDateTime in UTC: " + ridePoint1.getDateTime().format(formatter));
		System.out.println("zonedDateTime in IST: " + zonedDateTime1.format(formatter));
		
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
		
		ridePoints = ridePointDAO.getAllRidePointsOfRide(2);
		
		for (RidePoint ridePoint2 : ridePoints) {
			System.out.println(jsonUtil.getJson(ridePoint2));
		}

		JSONUtil<LineString> jsonUtilLineString = new JSONUtil<>(LineString.class);
		lineString = GeoJSONUtil.getLineStringFromRidePoints(ridePoints);
		System.out.println(jsonUtilLineString.getJson(lineString));

	//	ridePointDAO.delete(_id);

		
		System.out.println("End of Program");
	}

}
