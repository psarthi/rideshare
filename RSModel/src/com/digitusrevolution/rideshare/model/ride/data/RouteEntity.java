package com.digitusrevolution.rideshare.model.ride.data;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;

@Embeddable
public class RouteEntity {
	
	@Embedded
	@ElementCollection
	@JoinTable(name="route_point",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RoutePointEntity> routePoints = new ArrayList<RoutePointEntity>();

	public Collection<RoutePointEntity> getRoutePoints() {
		return routePoints;
	}

	public void setRoutePoints(Collection<RoutePointEntity> routePoints) {
		this.routePoints = routePoints;
	}

}
