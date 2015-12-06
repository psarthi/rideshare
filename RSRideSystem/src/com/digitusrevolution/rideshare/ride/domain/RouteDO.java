package com.digitusrevolution.rideshare.ride.domain;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import com.digitusrevolution.rideshare.common.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.RoutePoint;
import com.digitusrevolution.rideshare.ride.dto.GoogleDirection;
import com.digitusrevolution.rideshare.ride.dto.Step;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RouteDO{
	
	private Route route;
	
	public RouteDO() {
		route = new Route();
	}
	
	public Route getRoute(Point startPoint, Point endPoint){		
		
		RESTClientUtil restClientUtil = new RESTClientUtil();
		String json = restClientUtil.getDirection(startPoint.getLatitude(),startPoint.getLongitude(), endPoint.getLatitude(), endPoint.getLongitude());
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
			Point point = new Point();
			point.setLatitude(step.getStartLocation().getLat());
			point.setLongitude(step.getStartLocation().getLng());
			RoutePoint routePoint = new RoutePoint();
			routePoint.setPoint(point);
			routePoint.setSequence(seq);
			route.getRoutePoints().add(routePoint);
			seq++;
		}
		return route;
	}
}
