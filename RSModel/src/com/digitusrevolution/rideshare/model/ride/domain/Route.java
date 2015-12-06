package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Route {
	
	private Collection<RoutePoint> routePoints = new ArrayList<RoutePoint>();

	public Collection<RoutePoint> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(Collection<RoutePoint> routePoints) {
		this.routePoints = routePoints;
	}
	
}
