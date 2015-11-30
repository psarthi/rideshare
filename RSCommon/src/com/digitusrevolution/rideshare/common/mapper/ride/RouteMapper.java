package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;
import java.util.Map;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class RouteMapper implements Mapper<Route, RouteEntity>{

	@Override
	public RouteEntity getEntity(Route route){
		RouteEntity routeEntity = new RouteEntity();
		Map<Integer, PointEntity> routeEntityPoints = routeEntity.getPoints();
		Map<Integer, Point> routePoints = route.getPoints();
		for (Map.Entry<Integer, Point> entry: routePoints.entrySet()){
			PointMapper pointMapper = new PointMapper();
			PointEntity pointEntity = new PointEntity();
			pointEntity = pointMapper.getEntity(entry.getValue());
			routeEntityPoints.put(entry.getKey(), pointEntity);
		}
		return routeEntity;
	}
	
	@Override
	public Route getDomainModel(RouteEntity routeEntity){
		Route route = new Route();
		Map<Integer, PointEntity> routeEntityPoints = routeEntity.getPoints();
		Map<Integer, Point> routePoints = route.getPoints();
		for (Map.Entry<Integer, PointEntity> entry: routeEntityPoints.entrySet()){
			PointMapper pointMapper = new PointMapper();
			Point point = new Point();
			point = pointMapper.getDomainModel(entry.getValue());
			routePoints.put(entry.getKey(), point);
		}
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
