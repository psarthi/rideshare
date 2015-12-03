package com.digitusrevolution.rideshare.ride.domain;

import java.util.List;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class RouteDO{
	
	private Route route;
	
	public RouteDO() {
		route = new Route();
	}
	
	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
	}

	public List<Route> getRoutes(Point startPoint, Point endPoint){		

		return null;
	}
}
