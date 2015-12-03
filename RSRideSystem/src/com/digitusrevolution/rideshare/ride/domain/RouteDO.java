package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.mapper.ride.RouteMapper;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class RouteDO{
	
	private Route route;
	private RouteEntity routeEntity;
	private RouteMapper routeMapper;
	
	public RouteDO() {
		route = new Route();
		routeEntity = new RouteEntity();
		routeMapper = new RouteMapper();
	}
	
	public Route getRoute() {
		return route;
	}

	public void setRoute(Route route) {
		this.route = route;
		routeEntity = routeMapper.getEntity(route);
	}

	public RouteEntity getRouteEntity() {
		return routeEntity;
	}

	public void setRouteEntity(RouteEntity routeEntity) {
		this.routeEntity = routeEntity;
		route = routeMapper.getDomainModel(routeEntity);
	}


}
