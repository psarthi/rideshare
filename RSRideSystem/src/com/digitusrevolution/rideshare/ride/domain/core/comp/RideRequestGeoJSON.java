package com.digitusrevolution.rideshare.ride.domain.core.comp;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.ride.dto.RideRequestSearchResult;

public class RideRequestGeoJSON {
	
	private static final Logger logger = LogManager.getLogger(RideRequestGeoJSON.class.getName());
	private RideRequestDO rideRequestDO;
	
	public RideRequestGeoJSON(RideRequestDO rideRequestDO) {
		this.rideRequestDO = rideRequestDO; 
	}
	
	public FeatureCollection getAllRideRequestPoints(){
		FeatureCollection featureCollection = new FeatureCollection();

		List<RideRequest> rideRequests = rideRequestDO.getAll();
		for (RideRequest rideRequest : rideRequests) {
			List<RideRequestPoint> requestPoints = rideRequestDO.getPointsOfRideRequest(rideRequest.getId());			
			for (RideRequestPoint rideRequestPoint : requestPoints) {
				List<Feature> features = getRideRequestGeoJSON(rideRequestPoint.getRideRequestId());
				featureCollection.addAll(features);
			}
		}	

		return featureCollection;
	}

	/*
	 * Purpose - Get feature collection of all ride pickup and drop points, ride request pickup and drop points, multi polygon around route
	 * 
	 */
	public FeatureCollection getMatchingRideRequests(int rideId,double lastSearchDistance, int lastResultIndex){
		RideRequestSearchResult rideRequestSearchResult = rideRequestDO.searchRideRequests(rideId, lastSearchDistance, lastResultIndex);
		FeatureCollection featureCollection = getMatchingRideRequestsGeoJson(rideId, rideRequestSearchResult);
		return featureCollection;
	}

	/*
	 * Purpose - Get GeoJSON for Matching Ride Requests
	 * 
	 * High level logic -
	 * 
	 * - Get GeoJson for all matching Ride Pickup and Drop points
	 * - Get GeoJson for Ride (Includes Start Point, End Point and Route)
	 * - Get GeoJson for all matched ride requests (Include Pickup and Drop points)
	 * 
	 */
	private FeatureCollection getMatchingRideRequestsGeoJson(int rideId,
			RideRequestSearchResult rideRequestSearchResult) {
		RideDO rideDO = new RideDO();
		//This will get ride pickup and drop points 
		FeatureCollection featureCollection = rideDO.getMatchedTripInfoGeoJSON(rideRequestSearchResult.getMatchedTripInfos());
		//This will get route along with start and end point which is common to all matching ride requests
		List<Feature> rideGeoJsonFeatures = rideDO.getRideGeoJson(rideDO.getAllData(rideId));
		featureCollection.addAll(rideGeoJsonFeatures);
		for (MatchedTripInfo matchedTripInfo : rideRequestSearchResult.getMatchedTripInfos()) {
			//This will get ride request pickup and drop points 
			List<Feature> rideRequestGeoJSONFeatures = getRideRequestGeoJSON(matchedTripInfo.getRideRequestId());
			featureCollection.addAll(rideRequestGeoJSONFeatures);
		}
		//This will get polygon around route
		Feature featureMultiPolygon = new Feature();
		featureMultiPolygon.setGeometry(rideRequestSearchResult.getMultiPolygon());
		featureCollection.add(featureMultiPolygon);
		JSONUtil<FeatureCollection> jsonUtilFeatureCollection = new JSONUtil<>(FeatureCollection.class);
		logger.debug("Ride Request Search Result GeoJSON:"+jsonUtilFeatureCollection.getJson(featureCollection));
		return featureCollection;
	}

	//This will return geoJSON of ride request points
	public FeatureCollection getRideRequestPoints(int rideRequestId) {
		FeatureCollection featureCollection = new FeatureCollection();
		List<Feature> features = getRideRequestGeoJSON(rideRequestId);
		featureCollection.addAll(features);
		return featureCollection;
	}

	/*
	 * Purpose - This will get features for ride request pickup and drop point
	 * 
	 */
	public List<Feature> getRideRequestGeoJSON(int rideRequestId) {
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
	 * Purpose - This will get properties for Ride Request points
	 */
	private Map<String, Object> getRideRequestPointProperties(RideRequestPoint rideRequestPoint, String pointType) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", pointType);
		properties.put("RideRequestId", rideRequestPoint.getRideRequestId());
		properties.put("DateTimeUTC", rideRequestPoint.getDateTime());
		properties.put("DistanceVariation", rideRequestPoint.getDistanceVariation());
		return properties;
	}

}
