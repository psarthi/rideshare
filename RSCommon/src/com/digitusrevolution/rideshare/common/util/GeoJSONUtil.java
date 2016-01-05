package com.digitusrevolution.rideshare.common.util;

import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.LngLatAlt;

import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class GeoJSONUtil {

	/*
	 * All Geometry Objects of GeoJSON Library is of type GeoJsonObject, 
	 * that's why we can pass any Geometry object directly here
	 */
	public static Feature getFeatureFromGeometry(GeoJsonObject geoJsonObject, Map<String, Object> properties){
		Feature feature = new Feature();
		feature.setGeometry(geoJsonObject);
		feature.setProperties(properties);
		return feature;
	}
	
	public static GeoJsonObject getGeoJsonObject(String geoJson){
		JSONUtil<GeoJsonObject> jsonUtilGeoJsonObject = new JSONUtil<>(GeoJsonObject.class);
		GeoJsonObject geoJsonObject = jsonUtilGeoJsonObject.getModel(geoJson);
		return geoJsonObject;
	}
	
	public static String getGeoJsonString(GeoJsonObject geoJsonObject){
		JSONUtil<GeoJsonObject> jsonUtilGeoJsonObject = new JSONUtil<>(GeoJsonObject.class);
		String geoJson = jsonUtilGeoJsonObject.getJson(geoJsonObject);
		return geoJson;
	}

	
	public static LineString getLineStringFromPoints(List<Point> points){
		LineString lineString = new LineString();
		for (Point point : points) {
			LngLatAlt lngLatAlt = new LngLatAlt();
			lngLatAlt.setLatitude(point.getLatitude());
			lngLatAlt.setLongitude(point.getLongitude());
			lineString.add(lngLatAlt);
		}
		return lineString;
	}
	
}
