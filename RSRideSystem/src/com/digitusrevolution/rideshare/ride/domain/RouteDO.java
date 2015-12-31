package com.digitusrevolution.rideshare.ride.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.InternalServerErrorException;

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
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDistance;
import com.digitusrevolution.rideshare.ride.dto.google.Leg;
import com.digitusrevolution.rideshare.ride.dto.google.Step;

public class RouteDO{

	private Route route;
	private static final Logger logger = LogManager.getLogger(RouteDO.class.getName());

	public RouteDO() {
		route = new Route();
	}

	public GoogleDirection getDirection(Point startPoint, Point endPoint, ZonedDateTime departureTimeUTC){
		String json = RESTClientUtil.getDirection(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude(),departureTimeUTC);
		JSONUtil<GoogleDirection> jsonUtilGoogleDirection = new JSONUtil<>(GoogleDirection.class);
		GoogleDirection googleDirection = jsonUtilGoogleDirection.getModel(json);
		if(!googleDirection.getStatus().equals("OK")){
			throw new InternalServerErrorException("Exception in getting data from Google Direction Service:[Status,Error Message]"+googleDirection.getStatus()+","+googleDirection.getError_message());
		}
		return googleDirection;
	}
	
	public GoogleDistance getDistance(Point startPoint, Point endPoint, ZonedDateTime departureTimeUTC){
		String json = RESTClientUtil.getDistance(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude(),departureTimeUTC);
		JSONUtil<GoogleDistance> jsonUtilGoogleDirection = new JSONUtil<>(GoogleDistance.class);
		GoogleDistance googleDistance = jsonUtilGoogleDirection.getModel(json);
		if(!googleDistance.getStatus().equals("OK")){
			throw new InternalServerErrorException("Exception in getting data from Google Distance Service:[Status,Error Message]"+googleDistance.getStatus()+","+googleDistance.getError_message());
		}
		return googleDistance;
	}


	/*
	 * Purpose - This function is responsible for finding out all points which ride 
	 * would go through and which needs to be stored in the system with ride basic information
	 * which is rideId as well as date and time when vehicle would reach that point, along with 
	 * the sequence in which it will come.
	 * 
	 * Since number of points is very high, so we filter them based on miniumum distance between two points
	 * which is configuration property.  
	 * 
	 * High level logic - 
	 * 
	 * - Add start location as first point in the route, which will never get added in the below steps
	 *  	as first point is comparing with itself which would always be Zero distance and hence would not get added 
	 * - For rest of the ridepoints, follow below steps -
	 * - Get the steps details from google direction
	 * - For each steps, decode the polyline path to get all the points in that step
	 * - Calculate the average speed for that step, based on time and distance provided by google
	 *   	Don't use overall speed of the whole ride, as this can give wrong calculation e.g. If you are travelling 
	 *   	from Bangalore to New Delhi, which is around 2K Kms then average speed for the whole ride would be 
	 *   	around 70-80 Kms/hr but that doesn't hold good within city  
	 * - Store the first point as "from" location
	 * - Loop through all the points available in the steps which has been given by decoding
	 * - Store each point as "to" location
	 * - Calculate the distance between last point and this point i.e. between "from" to "to"
	 * - Compare the distance with minimum required distance between points
	 * - In case, its less move to the next point and calculate the distance again from that same earlier location
	 * 		Note - Don't change the from location as the distance calculation is between last stored point to this point
	 * - In case, if its more then calculate the time to travel based on step speed calculated earlier    
	 * - Get the time of last point and increment that by the time calculated above
	 * - Add the point to the route and ensure that each point is different as by just adding point to the route would 
	 * 	 	result into wrong data, as all points would point to the last point as Java is copy by reference
	 * - Ensure each point maintains a sequence number
	 * 
	 */
	public Route getRoute(GoogleDirection direction, List<RideBasicInfo> ridesBasicInfo){		

		Leg leg = direction.getRoutes().get(0).getLegs().get(0);
		List<Step> steps = leg.getSteps();
		//**Start - This is just for analysis purpose, below code is not relevant to the logic
		String encodedOverallPath = direction.getRoutes().get(0).getOverviewPolyline().getPoints();
		List<LatLng> latLngsOverall = PolyUtil.decode(encodedOverallPath);
		//Distance is in Meters
		double totalDistance = leg.getDistance().getValue();
		//Duration is in seconds
		double totalTime = leg.getDuration().getValue();
		double overallSpeed = MathUtil.getSpeed(totalDistance, totalTime);
		double sumOfDistance = 0;
		double sumOfTime = 0;
		//**End
		int seq = 1;
		int skip = 0;
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("MIN_DISTANCE_BETWEEN_ROUTE_POINTS"));
		//This will add start location to the list, which will get skipped once it enters the 
		//loop as first point comparision would always be zero, which is less than min distance 
		//and it would not get added to the route
		RidePoint startRidePoint = new RidePoint();
		startRidePoint.getPoint().setLatitude(leg.getStartLocation().getLat());
		startRidePoint.getPoint().setLongitude(leg.getStartLocation().getLng());
		startRidePoint.setSequence(seq);
		//Below is done to have seperate copy of the data, else this would point to the last point due to copy by reference.
		List<RideBasicInfo> copyOfRidesBasicInfo = new ArrayList<>();
		for (RideBasicInfo rideBasicInfo : ridesBasicInfo) {
			ZonedDateTime startPointDateTime = rideBasicInfo.getDateTime();
			rideBasicInfo.setDateTime(startPointDateTime);
			RideBasicInfo copyOfRideBasicInfo = new RideBasicInfo();
			copyOfRideBasicInfo.setId(rideBasicInfo.getId());
			copyOfRideBasicInfo.setDateTime(rideBasicInfo.getDateTime());
			copyOfRidesBasicInfo.add(copyOfRideBasicInfo);
		}
		startRidePoint.setRidesBasicInfo(copyOfRidesBasicInfo);
		logger.debug("Ride Start Time:"+ridesBasicInfo.get(0).getDateTime());
		logger.debug("RidePoint added [point,time]:"+startRidePoint.getPoint().toString()+","+startRidePoint.getRidesBasicInfo().get(0).getDateTime());
		route.getRidePoints().add(startRidePoint);
		seq++;
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
				double distance = SphericalUtil.computeDistanceBetween(from, to);
				if (distance <= minDistance){
					logger.trace("[Less:distance,seq,skip,from,to]:"+distance+","+seq+","+skip+","+from.toString()+","+to.toString());
					skip++;
					continue;
				} 
				logger.trace("[More:distance,seq,skip,from,to]:"+distance+","+seq+","+skip+","+from.toString()+","+to.toString());
				logger.trace(distance);				

				double time = MathUtil.getTime(distance, stepSpeed);

				//**Start - This is just for analysis purpose, below code is not relevant to the logic
				double timeBasedOnOverallSpeed = MathUtil.getTime(distance, overallSpeed);
				logger.trace("timeBasedOnOverallSpeed:"+timeBasedOnOverallSpeed+":timeBasedOnStepSpeed:"+time+":Diff:"+(timeBasedOnOverallSpeed-time));
				logger.trace(timeBasedOnOverallSpeed-time);
				//**End
				List<RideBasicInfo> updatedRidesBasicInfo = new ArrayList<>();
				for (RideBasicInfo rideBasicInfo : ridesBasicInfo) {
					ZonedDateTime lastPointDateTime = rideBasicInfo.getDateTime();
					ZonedDateTime thisPointDateTime = lastPointDateTime.plusSeconds((long) time);
					rideBasicInfo.setDateTime(thisPointDateTime);
					RideBasicInfo updatedRideBasicInfo = new RideBasicInfo();
					updatedRideBasicInfo.setId(rideBasicInfo.getId());
					updatedRideBasicInfo.setDateTime(rideBasicInfo.getDateTime());
					updatedRidesBasicInfo.add(updatedRideBasicInfo);
				}
				RidePoint ridePoint = new RidePoint();
				ridePoint.getPoint().setLatitude(latLngs.get(i).latitude);
				ridePoint.getPoint().setLongitude(latLngs.get(i).longitude);
				ridePoint.setSequence(seq);
				ridePoint.setRidesBasicInfo(updatedRidesBasicInfo);
				logger.trace("RidePoint added [point,time]:"+ridePoint.getPoint().toString()+","+ridePoint.getRidesBasicInfo().get(0).getDateTime());
				route.getRidePoints().add(ridePoint);
				from = to;
				seq++;
				//**Start - This is just for analysis purpose, below code is not relevant to the logic
				sumOfDistance += distance;
				sumOfTime += time;
				//**End
			}			
		}
		logger.debug("Ride Finish Time:"+ridesBasicInfo.get(0).getDateTime());
		//**Start - This is just for analysis purpose, below code is not relevant to the logic
		for (RidePoint ridePoint : route.getRidePoints()) {
			logger.trace(ridePoint.toString());
		}
		//**End
		logger.debug("Summary-------------");
		logger.debug("Minimum distance between two Points:"+minDistance);
		logger.debug("Total Points in Steps:"+steps.size());
		logger.debug("Total Points in Overall Polyline:"+latLngsOverall.size());
		//Reason for deducting "1" as sequence is increment after the last point as well
		logger.debug("Total Points in Steps Polyline:"+(skip+seq-1)+":Total Skipped Points:"+skip+":Diff:"+(seq-1));
		logger.debug("Total Points stored in Route:"+route.getRidePoints().size());
		logger.debug("Total Distance:"+totalDistance+":Sum of calculated distance:"+sumOfDistance+":Diff:"+(totalDistance-sumOfDistance));
		logger.debug("Total travel time:"+totalTime+":Sum of total time:"+sumOfTime+":Diff:"+(totalTime-sumOfTime));

		return route;
	}
}
