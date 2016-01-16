package com.digitusrevolution.rideshare.ride.domain.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.dto.RideMatchInfo;

public class RideSystemUtil {

	/*
	 * Purpose - This will get features for ride request pickup and drop point
	 */
	public static List<Feature> getRideRequestGeoJSON(int rideRequestId) {
		RideRequestDO rideRequestDO = new RideRequestDO();
		List<RideRequestPoint> rideRequestPoints = rideRequestDO.getPointsOfRideRequest(rideRequestId);
		RideRequestPoint rideRequestPoint1 = rideRequestPoints.get(0);
		RideRequestPoint rideRequestPoint2 = rideRequestPoints.get(1);
		Map<String, Object> pickupPointProperties;
		Map<String, Object> dropPointProperties;
		RideRequestPoint rideRequestPickupPoint;
		RideRequestPoint rideRequestDropPoint;
		if (rideRequestPoint1.getDateTime().compareTo(rideRequestPoint2.getDateTime()) < 0){
			rideRequestPickupPoint = rideRequestPoint1;
			rideRequestDropPoint = rideRequestPoint2;
		} else {
			rideRequestPickupPoint = rideRequestPoint2;
			rideRequestDropPoint = rideRequestPoint1;
		}
		pickupPointProperties = getRideRequestPointProperties(rideRequestPickupPoint, "pickuppoint");
		dropPointProperties = getRideRequestPointProperties(rideRequestDropPoint, "droppoint");
		List<Feature> features = new LinkedList<>();
		org.geojson.Point geoJsonPickupPoint = GeoJSONUtil.getGeoJsonPointFromPoint(rideRequestPickupPoint.getPoint());
		Feature feature1 = GeoJSONUtil.getFeatureFromGeometry(geoJsonPickupPoint, pickupPointProperties);
		features.add(feature1);
		org.geojson.Point geoJsonDropPoint = GeoJSONUtil.getGeoJsonPointFromPoint(rideRequestDropPoint.getPoint());
		Feature feature2 = GeoJSONUtil.getFeatureFromGeometry(geoJsonDropPoint, dropPointProperties);
		features.add(feature2);
		return features;
	}

	/*
	 * Purpose - Get GeoJSON for ride pickup and drop points
	 * Note - Purposefully ride and ride request geoJson has not been included here 
	 * as it may become duplicates for searchRides and SearchRideRequest, so its the caller 
	 * responsibility to add geoJson for rides and ride requests
	 */
	public static FeatureCollection getRideMatchInfoGeoJSON(List<RideMatchInfo> rideMatchInfos) {
		FeatureCollection featureCollection = new FeatureCollection();
		for (RideMatchInfo rideMatchInfo : rideMatchInfos) {
			Point ridePickupPoint = rideMatchInfo.getRidePickupPoint().getPoint();
			Map<String, Object> ridePickupPointProperties = getRideMatchInfoProperties(rideMatchInfo,"ridepickuppoint");
			org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(ridePickupPoint);
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, ridePickupPointProperties);
			featureCollection.add(feature);

			Point rideDropPoint = rideMatchInfo.getRideDropPoint().getPoint();
			Map<String, Object> rideDropPointProperties = getRideMatchInfoProperties(rideMatchInfo,"ridedroppoint");
			geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(rideDropPoint);
			feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, rideDropPointProperties);
			featureCollection.add(feature);
		}
		return featureCollection;
	}

	/*
	 * Purpose - This will get Properties for Ride Pickup and Drop Point
	 * 
	 */
	private static Map<String, Object> getRideMatchInfoProperties(RideMatchInfo rideMatchInfo, String pointType) {
		Map<String, Object> ridePickupPointProperties = new HashMap<>();
		ridePickupPointProperties.put("type", pointType);
		ridePickupPointProperties.put("RideId", rideMatchInfo.getRideId());
		ridePickupPointProperties.put("RideRequestId", rideMatchInfo.getRideRequestId());
		if (pointType.equals("ridepickuppoint")){
			ridePickupPointProperties.put("Distance", rideMatchInfo.getPickupPointDistance());
			ridePickupPointProperties.put("Sequence", rideMatchInfo.getRidePickupPoint().getSequence());
			ridePickupPointProperties.put("DateTimeUTC", rideMatchInfo.getRidePickupPoint().getRidesBasicInfo().get(0).getDateTime());
		} else {
			ridePickupPointProperties.put("Distance", rideMatchInfo.getDropPointDistance());
			ridePickupPointProperties.put("Sequence", rideMatchInfo.getRideDropPoint().getSequence());
			ridePickupPointProperties.put("DateTimeUTC", rideMatchInfo.getRideDropPoint().getRidesBasicInfo().get(0).getDateTime());
		}
		ridePickupPointProperties.put("RideRequestTravelDistance", rideMatchInfo.getRideRequestTravelDistance());
		return ridePickupPointProperties;
	}

	/*
	 * Purpose - This will get properties for Ride Request points
	 */
	private static Map<String, Object> getRideRequestPointProperties(RideRequestPoint rideRequestPoint, String pointType) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", pointType);
		properties.put("RideRequestId", rideRequestPoint.getRideRequestId());
		properties.put("DateTimeUTC", rideRequestPoint.getDateTime());
		properties.put("DistanceVariation", rideRequestPoint.getDistanceVariation());
		return properties;
	}

	public static List<Feature> getRideGeoJson(Ride ride) {
		List<Point> points = new ArrayList<>();
		Collection<RidePoint> ridePoints = ride.getRoute().getRidePoints();
		for (RidePoint ridePoint : ridePoints) {
			points.add(ridePoint.getPoint());
		}
		LineString lineString = GeoJSONUtil.getLineStringFromPoints(points);
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", "route");
		properties.put("RideId", ride.getId());
		properties.put("StartDateTimeUTC", ride.getStartTime());
		Feature feature = GeoJSONUtil.getFeatureFromGeometry(lineString, properties);
		List<Feature> features = new LinkedList<>();
		features.add(feature);
		feature = getRidePointGeoJson(ride.getStartPoint(), "startpoint");
		features.add(feature);
		feature = getRidePointGeoJson(ride.getEndPoint(), "endpoint");
		features.add(feature);
		return features;
	}

	private static Feature getRidePointGeoJson(RidePoint ridePoint, String pointType) {
		Feature feature;
		org.geojson.Point geoJsonPoint;
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", pointType);
		properties.put("RideId", ridePoint.getRidesBasicInfo().get(0).getId());
		properties.put("DateTimeUTC", ridePoint.getRidesBasicInfo().get(0).getDateTime());
		geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(ridePoint.getPoint());
		feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
		return feature;
	}

}
