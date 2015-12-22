package com.digitusrevolution.rideshare.ride.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.PolyUtil;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.MathUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.google.Leg;
import com.digitusrevolution.rideshare.ride.dto.google.Step;

public class RouteDO{
	
	private Route route;
	private static final Logger logger = LogManager.getLogger(RouteDO.class.getName());
	
	public RouteDO() {
		route = new Route();
	}
	
	public GoogleDirection getDirection(Point startPoint, Point endPoint){
		String json = RESTClientUtil.getDirection(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
		JSONUtil<GoogleDirection> jsonUtilGoogleDirection = new JSONUtil<>(GoogleDirection.class);
		GoogleDirection googleDirection = jsonUtilGoogleDirection.getModel(json);
		return googleDirection;
	}
	
	public Route processRoute(GoogleDirection googleDirection, List<RideBasicInfo> ridesBasicInfo){		
		
		Leg leg = googleDirection.getRoutes().get(0).getLegs().get(0);
		List<Step> steps = leg.getSteps();
		String encodedOverallPath = googleDirection.getRoutes().get(0).getOverviewPolyline().getPoints();
		List<LatLng> latLngsOverall = PolyUtil.decode(encodedOverallPath);
		//Distance is in Meters
		double totalDistance = leg.getDistance().getValue();
		//Duration is in seconds
		double totalTime = leg.getDuration().getValue();
		double overallSpeed = MathUtil.getSpeed(totalDistance, totalTime);
		int seq = 1;
		int skip = 1;
		double sumOfDistance = 0;
		double sumOfTime = 0;
		double combinedDistance = 0;
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("MIN_DISTANCE_BETWEEN_ROUTE_POINTS"));
		logger.debug("Ride Start Time:"+ridesBasicInfo.get(0).getDateTime());
		for (Step step : steps) {	
			double stepDistance = step.getDistance().getValue();
			double stepTime = step.getDuration().getValue();
			double stepSpeed = MathUtil.getSpeed(stepDistance, stepTime);
			String encodedPath = step.getPolyline().getPoints();
			logger.trace("encodedPath:"+encodedPath);
			List<LatLng> latLngs = PolyUtil.decode(encodedPath);
			logger.trace("encodedPath Points Count:"+latLngs.size());
			LatLng from = latLngs.get(0);
			for (int i = 0; i < latLngs.size(); i++) {
				LatLng to = latLngs.get(i);
				RidePoint ridePoint = new RidePoint();
				ridePoint.getPoint().setLatitude(latLngs.get(i).latitude);
				ridePoint.getPoint().setLongitude(latLngs.get(i).longitude);
				ridePoint.setSequence(seq);
				double distance = SphericalUtil.computeDistanceBetween(from, to);
				combinedDistance += distance;
				if (combinedDistance <= minDistance){
					logger.trace("Less:"+distance+","+seq+","+skip+","+combinedDistance);
					skip++;
					continue;
				} 
				logger.trace("More:"+distance+","+seq+","+skip+","+combinedDistance);
				logger.trace(distance);
				
				double time = MathUtil.getTime(distance, stepSpeed);
				long seconds = (long) time;

				double timeBasedOnOverallSpeed = MathUtil.getTime(distance, overallSpeed);
				logger.trace("timeBasedOnOverallSpeed:"+timeBasedOnOverallSpeed+":timeBasedOnStepSpeed:"+time+":Diff:"+(timeBasedOnOverallSpeed-time));
				logger.trace(timeBasedOnOverallSpeed-time);
				List<RideBasicInfo> updatedRidesBasicInfo = new ArrayList<>();
				for (RideBasicInfo rideBasicInfo : ridesBasicInfo) {
					ZonedDateTime lastPointDateTime = rideBasicInfo.getDateTime();
					ZonedDateTime thisPointDateTime = lastPointDateTime.plusSeconds(seconds);
					rideBasicInfo.setDateTime(thisPointDateTime);
					RideBasicInfo updatedRideBasicInfo = new RideBasicInfo();
					updatedRideBasicInfo.setId(rideBasicInfo.getId());
					updatedRideBasicInfo.setDateTime(rideBasicInfo.getDateTime());
					updatedRidesBasicInfo.add(updatedRideBasicInfo);
				}
				ridePoint.setRidesBasicInfo(updatedRidesBasicInfo);
				route.getRidePoints().add(ridePoint);
				from = to;
				seq++;
				sumOfDistance += distance;
				sumOfTime += time; 
				combinedDistance = 0;
			}			
		}
		logger.debug("Ride Finish Time:"+ridesBasicInfo.get(0).getDateTime());
		for (RidePoint ridePoint : route.getRidePoints()) {
			logger.debug(ridePoint.toString());
		}
		logger.debug("Summary-------------");
		logger.debug("Minimum distance between two Points:"+minDistance);
		logger.debug("Total Points in Steps:"+steps.size());
		logger.debug("Total Points in Overall Polyline:"+latLngsOverall.size());
		logger.debug("Total Points in Steps Polyline:"+(skip+seq)+":Total Skipped Points:"+skip+":Diff:"+(seq));
		logger.debug("Total Points stored in Route:"+route.getRidePoints().size());
		logger.debug("Total Distance:"+totalDistance+":Sum of calculated distance:"+sumOfDistance+":Diff:"+(totalDistance-sumOfDistance));
		logger.debug("Total travel time:"+totalTime+":Sum of total time:"+sumOfTime+":Diff:"+(totalTime-sumOfTime));

		return route;
	}
}
