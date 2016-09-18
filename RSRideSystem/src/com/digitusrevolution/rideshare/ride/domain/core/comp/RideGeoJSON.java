package com.digitusrevolution.rideshare.ride.domain.core.comp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;

public class RideGeoJSON {
	
	private static final Logger logger = LogManager.getLogger(RideGeoJSON.class.getName());
	private RideDO rideDO;
	
	//No need to create empty constructor for Jackson JSON conversion, as these classes would have primarily only behaviors and no state
	public RideGeoJSON(RideDO rideDO) {
		this.rideDO = rideDO;
	}
	
	/*
	 * Purpose - This will get all ride points across the system, which is helpful to show the dashboard
	 * 
	 */
	public FeatureCollection getAllRidePoints(){
		FeatureCollection featureCollection = new FeatureCollection();
		List<Ride> rides = rideDO.getAllWithRoute();
		for (Ride ride : rides) {
			List<Feature> features = getRidePoints(ride);
			featureCollection.addAll(features);
		}	
		return featureCollection;
	}
	
	/*
	 * Purpose - This will get all the matching rides for specific ride request. 
	 * This function internally calls search ride and convert the data into GeoJson format 
	 */
	public FeatureCollection getMatchingRides(int rideRequestId){
		List<MatchedTripInfo> matchedTripInfos = rideDO.searchRides(rideRequestId);
		FeatureCollection featureCollection = getMatchingRidesGeoJson(rideRequestId, matchedTripInfos);
		return featureCollection;
	}

	/*
	 * Purpose - Get GeoJSON for Matching Rides
	 * 
	 * High level logic -
	 * 
	 * - Get GeoJson for all matching Ride Pickup and Drop points
	 * - Get GeoJson for Ride Request points (Include Pickup and Drop points)
	 * - Get GeoJson for all matched rides (Includes Start Point, End Point and Route)
	 * 
	 */
	private FeatureCollection getMatchingRidesGeoJson(int rideRequestId, List<MatchedTripInfo> matchedTripInfos) {
		//This will get ride related geoJson for each matching rides
		FeatureCollection featureCollection = getMatchedTripInfoGeoJSON(matchedTripInfos);
		//This will get ride request related geoJson (Note - Its common for all matching rides) 
		RideRequestDO rideRequestDO = new RideRequestDO();
		List<Feature> rideRequestGeoJSONFeatures = rideRequestDO.getRideRequestGeoJSON(rideRequestId);
		featureCollection.addAll(rideRequestGeoJSONFeatures);
		//This will get all route along with start and end point for all matching rides
		for (MatchedTripInfo matchedTripInfo : matchedTripInfos) {
			Ride ride = rideDO.getAllData(matchedTripInfo.getRideId());
			List<Feature> rideGeoJsonFeatures = getRideGeoJson(ride);
			featureCollection.addAll(rideGeoJsonFeatures);
		}
		return featureCollection;
	}

	/*
	 * Purpose - This will get all ride points for a specific ride
	 */
	public FeatureCollection getRidePoints(int rideId){
		FeatureCollection featureCollection = new FeatureCollection();
		featureCollection.addAll(getRidePoints(rideDO.getAllData(rideId)));
		return featureCollection;
	}
	
	/*
	 * Purpose - This will get all ride points for a particular ride Id along with start and end point 
	 */
	private List<Feature> getRidePoints(Ride ride) {
		List<Feature> features = getRideGeoJson(ride);
		return features;
	}

	/*
	 * Purpose - Get GeoJSON for ride pickup and drop points
	 * Note - Purposefully ride and ride request geoJson has not been included here 
	 * as it may become duplicates for searchRides and SearchRideRequest, so its the caller 
	 * responsibility to add geoJson for rides and ride requests
	 */
	public FeatureCollection getMatchedTripInfoGeoJSON(List<MatchedTripInfo> matchedTripInfos) {
		FeatureCollection featureCollection = new FeatureCollection();
		for (MatchedTripInfo matchedTripInfo : matchedTripInfos) {
			Point ridePickupPoint = matchedTripInfo.getRidePickupPoint().getPoint();
			Map<String, Object> ridePickupPointProperties = getMatchedTripInfoProperties(matchedTripInfo,"ridepickuppoint");
			org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(ridePickupPoint);
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, ridePickupPointProperties);
			featureCollection.add(feature);

			Point rideDropPoint = matchedTripInfo.getRideDropPoint().getPoint();
			Map<String, Object> rideDropPointProperties = getMatchedTripInfoProperties(matchedTripInfo,"ridedroppoint");
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
	private Map<String, Object> getMatchedTripInfoProperties(MatchedTripInfo matchedTripInfo, String pointType) {
		Map<String, Object> ridePickupPointProperties = new HashMap<>();
		ridePickupPointProperties.put("type", pointType);
		ridePickupPointProperties.put("RideId", matchedTripInfo.getRideId());
		ridePickupPointProperties.put("RideRequestId", matchedTripInfo.getRideRequestId());
		if (pointType.equals("ridepickuppoint")){
			ridePickupPointProperties.put("Distance", matchedTripInfo.getPickupPointDistance());
			ridePickupPointProperties.put("Sequence", matchedTripInfo.getRidePickupPoint().getSequence());
			ridePickupPointProperties.put("DateTimeUTC", matchedTripInfo.getRidePickupPoint().getRidesBasicInfo().get(0).getDateTime());
		} else {
			ridePickupPointProperties.put("Distance", matchedTripInfo.getDropPointDistance());
			ridePickupPointProperties.put("Sequence", matchedTripInfo.getRideDropPoint().getSequence());
			ridePickupPointProperties.put("DateTimeUTC", matchedTripInfo.getRideDropPoint().getRidesBasicInfo().get(0).getDateTime());
		}
		ridePickupPointProperties.put("RideRequestTravelDistance", matchedTripInfo.getRideRequestTravelDistance());
		return ridePickupPointProperties;
	}


	public List<Feature> getRideGeoJson(Ride ride) {
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
	

	private Feature getRidePointGeoJson(RidePoint ridePoint, String pointType) {
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
