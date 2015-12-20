package com.digitusrevolution.rideshare;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.PolyUtil;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.core.SphericalGeometry;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.dto.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MathTest {

	public static void main(String[] args) {


		PointDO pointDO = new PointDO();
		Point startPoint = pointDO.getCordinates("Bangalore"); 			
		Point endPoint = pointDO.getCordinates("New Delhi"); 

		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getDirection(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleDirection googleDirection =null;
		try {
			googleDirection = objectMapper.readValue(json, GoogleDirection.class);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to process JSON",e);
		}
		
		List<LatLng> latLngs = PolyUtil.decode(googleDirection.getRoutes().get(0).getOverviewPolyline().getPoints());
		List<Step> steps = googleDirection.getRoutes().get(0).getLegs().get(0).getSteps();
		
		LatLng pickupPoint = new LatLng(12.906074, 77.577326); 
		Point pointC = new Point(12.906074, 77.577326);
		System.out.println("Pickup Point: " + pickupPoint.toUrlValue());
		
		boolean status = PolyUtil.isLocationOnPath(pickupPoint, latLngs, true, 400);
		System.out.println("Location On Edge status: " + status);
		
		LatLng from = new LatLng(12.83995, 77.67702);		
		Point pointA = new Point(from.latitude,from.longitude);
		SphericalGeometry geometry = new SphericalGeometry();
		
		for (LatLng latLng : latLngs) {
			LatLng to = latLng;
			Point pointB = new Point(to.latitude,to.longitude);
		//	System.out.println("Mine: From: "+pointA + ":To:" + pointB + " Distance: " + geometry.getDistanceByHaversine(pointA, pointB));
			System.out.println(geometry.getDistanceByHaversine(pointA, pointB));
			if (geometry.isOnSegment(pointA, pointB, pointC)){
				System.out.println("Matched!!!");
			}
			
			from = to;
			pointA = pointB;
		}
		
		
		
		System.out.println("Total Points: "+latLngs.size());
		System.out.println("Total Steps: "+steps.size());
		
		LatLng latLngA = new LatLng(12.83995, 77.67702);
		LatLng latLngB = new LatLng(12.922471, 77.624018);

		pointA = new Point(12.839469, 77.676546);
		Point pointB = new Point(12.922471, 77.624018);
		
		System.out.println("Distance (Google):" + SphericalUtil.computeDistanceBetween(latLngA, latLngB));
		System.out.println("Distance (Mine):" + geometry.getDistanceByHaversine(pointA, pointB));

		System.out.println("Cross Track Distance (Mine): "+ geometry.getCrossTrackDistance(pointA, pointB, pointC));
		
		double alongTrackDistance = geometry.getAlongTrackDistance(pointA, pointB, pointC);
		System.out.println("Along Track Distance (Mine): "+ alongTrackDistance);
		
		double heading = SphericalUtil.computeHeading(latLngA, latLngB);
		double bearing = geometry.getInitialBearing(pointA, pointB);
		System.out.println("Bearing (Google):" + heading);
		System.out.println("Bearing (Mine):" + bearing);

		LatLng latLngIntersection = SphericalUtil.computeOffset(latLngA, alongTrackDistance, heading);		
		Point pointIntersection = geometry.getDestinationPoint(pointA, bearing, alongTrackDistance);
		
		System.out.println("Intersection Point (Google): " + latLngIntersection);
		System.out.println("Intersection Point (Mine): " + pointIntersection);

		System.out.println("Cross Track Distance from Interesection (Mine) : "+ geometry.getCrossTrackDistance(pointA, pointB, pointIntersection));
		
		
		

	}
}
