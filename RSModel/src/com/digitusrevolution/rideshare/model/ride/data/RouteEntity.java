package com.digitusrevolution.rideshare.model.ride.data;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;


@Embeddable
public class RouteEntity {
	
	@Embedded
	@ElementCollection
	@JoinTable(name="route_point",joinColumns=@JoinColumn(name="ride_id"))
	private List<RoutePointEntity> routePoints;

	public List<RoutePointEntity> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(List<RoutePointEntity> routePoints) {
		this.routePoints = routePoints;
	}
	
}
