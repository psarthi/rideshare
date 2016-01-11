package com.digitusrevolution.rideshare.common.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LineString;
import org.geojson.LngLatAlt;
import org.geojson.MultiPoint;
import org.geojson.Polygon;

import com.digitusrevolution.rideshare.common.math.google.LatLng;
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
	
	public static MultiPoint getMultiPointFromPoints(List<Point> points){
		MultiPoint multiPoint = new MultiPoint();
		for (Point point : points) {
			LngLatAlt lngLatAlt = new LngLatAlt();
			lngLatAlt.setLatitude(point.getLatitude());
			lngLatAlt.setLongitude(point.getLongitude());
			multiPoint.add(lngLatAlt);
		}
		return multiPoint;
	}

	public static org.geojson.Point getGeoJsonPointFromPoint(Point point){
		org.geojson.Point geoJsonPoint = new org.geojson.Point();
		LngLatAlt lngLatAlt = new LngLatAlt();
		lngLatAlt.setLatitude(point.getLatitude());
		lngLatAlt.setLongitude(point.getLongitude());
		geoJsonPoint.setCoordinates(lngLatAlt);
		return geoJsonPoint;
	}
	
	public static LatLng getLatLng(Point point){
		LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
		return latLng;
	}
	
	public static Point getPoint(LatLng latLng){
		Point point = new Point(latLng.longitude, latLng.latitude);
		return point;
	}
	
	public static Polygon getPolygonFromPoints(List<Point> points){
		Polygon polygon = new Polygon();
		List<LngLatAlt> lngLatAlts = new LinkedList<>();
		for (Point point : points) {
			LngLatAlt lngLatAlt = new LngLatAlt();
			lngLatAlt.setLatitude(point.getLatitude());
			lngLatAlt.setLongitude(point.getLongitude());
			lngLatAlts.add(lngLatAlt);
		}
		polygon.add(lngLatAlts);
		return polygon;
	}


}
