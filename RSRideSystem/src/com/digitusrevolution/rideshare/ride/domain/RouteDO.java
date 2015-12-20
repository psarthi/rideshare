package com.digitusrevolution.rideshare.ride.domain;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.google.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteDO{
	
	private Route route;
	
	public RouteDO() {
		route = new Route();
	}
	
	public Route getRoute(RidePoint startPoint, RidePoint endPoint){		
		
		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getDirection(startPoint.getPoint().getLatitude(),startPoint.getPoint().getLongitude(), endPoint.getPoint().getLatitude(), endPoint.getPoint().getLongitude());
		ObjectMapper objectMapper = new ObjectMapper();
		GoogleDirection googleDirection = null;
		try {
			googleDirection = objectMapper.readValue(json, GoogleDirection.class);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to process JSON",e);
		}
		
		List<Step> steps = googleDirection.getRoutes().get(0).getLegs().get(0).getSteps();
		int seq = 1;
		for (Step step : steps) {			
			RidePoint ridePoint = new RidePoint();
			ridePoint.getPoint().setLatitude(step.getStartLocation().getLat());
			ridePoint.getPoint().setLongitude(step.getStartLocation().getLng());
			ridePoint.setSequence(seq);
			route.getRidePoints().add(ridePoint);
			seq++;
		}
		return route;
	}
}
