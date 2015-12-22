package com.digitusrevolution.rideshare.common.util;

import java.time.format.DateTimeFormatter;
import java.util.List;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;

public class GeoJSONUtil {

	public static Feature getFeatureFromRidePoint(RidePoint ridePoint){
		JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
		String pointJson = jsonUtilPoint.getJson(ridePoint.getPoint());
		Feature feature = new Feature();
		feature.setGeometry(getGeoJsonObject(pointJson));
		//Needs to be rewritten as object structure has been changed to Map
//		feature.setProperty("rideId", ridePoint.getRideId());
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		//Needs to be rewritten as object structure has been changed to Map
//		feature.setProperty("dateTime", ridePoint.getDateTime().format(formatter));
		feature.setProperty("sequence", ridePoint.getSequence());
		return feature;		
	}
	
	/*
	 * All Geometry Objects of GeoJSON Library is of type GeoJsonObject, 
	 * that's why we can pass any Geometry object directly here
	 */
	public static Feature getFeatureFromGeometry(GeoJsonObject geoJsonObject){
		Feature feature = new Feature();
		feature.setGeometry(geoJsonObject);
		return feature;
	}
	
	public static GeoJsonObject getGeoJsonObject(String geoJson){
		JSONUtil<GeoJsonObject> jsonUtilGeoJsonObject = new JSONUtil<>(GeoJsonObject.class);
		GeoJsonObject geoJsonObject = jsonUtilGeoJsonObject.getModel(geoJson);
		return geoJsonObject;
	}
	
	public static LineString getLineStringFromRidePoints(List<RidePoint> ridePoints){
		LineString lineString = new LineString();
		for (RidePoint ridePoint : ridePoints) {
			LngLatAlt lngLatAlt = new LngLatAlt();
			lngLatAlt.setLatitude(ridePoint.getPoint().getLatitude());
			lngLatAlt.setLongitude(ridePoint.getPoint().getLongitude());
			lineString.add(lngLatAlt);
		}
		return lineString;
	}
	
}
