package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.RouteMapper;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class RouteDO implements DomainObject{
	
	private Route route;
	private RouteEntity routeEntity;
	
	
	public RouteDO() {
		route = new Route();
		routeEntity = new RouteEntity();
	}
	
	
	public Route getRoute() {
		return route;
	}


	public void setRoute(Route route) {
		this.route = route;
		mapDomainModelToDataModel();
	}


	public RouteEntity getRouteEntity() {
		return routeEntity;
	}


	public void setRouteEntity(RouteEntity routeEntity) {
		this.routeEntity = routeEntity;
		mapDataModelToDomainModel();
	}


	@Override
	public void mapDomainModelToDataModel() {
		RouteMapper routeMapper = new RouteMapper();
		routeEntity = routeMapper.getRouteEntity(route);
		
	}

	@Override
	public void mapDataModelToDomainModel() {
		
		RouteMapper routeMapper = new RouteMapper();
		route = routeMapper.getRoute(routeEntity);
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

}
