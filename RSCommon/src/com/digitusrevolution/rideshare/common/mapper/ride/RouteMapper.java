package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.data.RoutePointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.RoutePoint;

public class RouteMapper implements Mapper<Route, RouteEntity>{

	@Override
	public RouteEntity getEntity(Route route){
		RouteEntity routeEntity = new RouteEntity();
		Collection<RoutePoint> routePoints = route.getRoutePoints();
		Collection<RoutePointEntity> routePointEntities = routeEntity.getRoutePoints();

		for (RoutePoint routePoint : routePoints) {
			Point point = routePoint.getPoint();
			int sequence = routePoint.getSequence();
			RoutePointEntity routePointEntity = new RoutePointEntity();
			PointMapper pointMapper = new PointMapper();
			PointEntity pointEntity = pointMapper.getEntity(point);
			routePointEntity.setPoint(pointEntity);
			routePointEntity.setSequence(sequence);	
			routePointEntities.add(routePointEntity);
		}
		
		routeEntity.setRoutePoints(routePointEntities);
		return routeEntity;
	}
	
	@Override
	public Route getDomainModel(RouteEntity routeEntity){
		Route route = new Route();
		Collection<RoutePoint> routePoints = route.getRoutePoints();
		Collection<RoutePointEntity> routePointEntities = routeEntity.getRoutePoints();

		for (RoutePointEntity routePointEntity : routePointEntities) {
			PointEntity pointEntity = routePointEntity.getPoint();
			int sequence = routePointEntity.getSequence();
			RoutePoint routePoint = new RoutePoint();
			PointMapper pointMapper = new PointMapper();
			Point point = pointMapper.getDomainModel(pointEntity);
			routePoint.setPoint(point);
			routePoint.setSequence(sequence);	
			routePoints.add(routePoint);
		}
		
		route.setRoutePoints(routePoints);
		return route;
	}

	@Override
	public RouteEntity getEntityChild(Route model, RouteEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Route getDomainModelChild(Route model, RouteEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Route> getDomainModels(Collection<RouteEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<RouteEntity> getEntities(Collection<Route> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
