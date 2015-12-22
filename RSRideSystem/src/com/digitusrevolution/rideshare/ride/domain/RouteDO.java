package com.digitusrevolution.rideshare.ride.domain;

import java.util.List;

import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.google.Step;

public class RouteDO{
	
	private Route route;
	
	public RouteDO() {
		route = new Route();
	}
	
	public Route getRoute(RidePoint startPoint, RidePoint endPoint){		
		
		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getDirection(startPoint.getPoint().getLatitude(),startPoint.getPoint().getLongitude(), endPoint.getPoint().getLatitude(), endPoint.getPoint().getLongitude());
		JSONUtil<GoogleDirection> jsonUtil = new JSONUtil<>(GoogleDirection.class);
		GoogleDirection googleDirection = jsonUtil.getModel(json);
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
