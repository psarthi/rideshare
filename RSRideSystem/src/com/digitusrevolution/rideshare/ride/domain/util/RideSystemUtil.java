package com.digitusrevolution.rideshare.ride.domain.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.digitusrevolution.rideshare.ride.dto.RideMatchInfo;

public class RideSystemUtil {

	/*
	 * Purpose - This will get feature for ride request pickup and drop point
	 */
	public static List<Feature> getRideRequestGeoJSON(int rideRequestId) {
		//This will add ride request point to the feature collection
		RideRequestDO rideRequestDO = new RideRequestDO();
		List<RideRequestPoint> rideRequestPoints = rideRequestDO.getPointsOfRideRequest(rideRequestId);
		List<Feature> features = new LinkedList<>();
		for (RideRequestPoint rideRequestPoint : rideRequestPoints) {
			Point point = rideRequestPoint.getPoint();
			Map<String, Object> properties = new HashMap<>();
			properties.put("type", "riderequestpoint");
			properties.put("DistanceVariation", rideRequestPoint.getDistanceVariation());
			org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(point);
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
			features.add(feature);
		}
		return features;
	}
	
	/*
	 * Purpose - Get GeoJSON format for all matching rides or ride requests
	 */
	public static FeatureCollection getRideMatchInfoGeoJSON(List<RideMatchInfo> rideMatchInfos) {
		FeatureCollection featureCollection = new FeatureCollection();
		for (RideMatchInfo rideMatchInfo : rideMatchInfos) {
			Point ridePickupPoint = rideMatchInfo.getRidePickupPoint().getPoint();
			Map<String, Object> ridePickupPointProperties = getRideProperties(rideMatchInfo,"ridepickuppoint");
			org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(ridePickupPoint);
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, ridePickupPointProperties);
			featureCollection.add(feature);

			Point rideDropPoint = rideMatchInfo.getRideDropPoint().getPoint();
			Map<String, Object> rideDropPointProperties = getRideProperties(rideMatchInfo,"ridedroppoint");
			geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(rideDropPoint);
			feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, rideDropPointProperties);
			featureCollection.add(feature);
		}
		return featureCollection;
	}

	private static Map<String, Object> getRideProperties(RideMatchInfo rideMatchInfo, String pointType) {
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
}
