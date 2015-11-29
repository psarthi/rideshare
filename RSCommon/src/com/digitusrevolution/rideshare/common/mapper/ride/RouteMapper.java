package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Map;

import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class RouteMapper {

	public RouteEntity getRouteEntity (Route route){
		RouteEntity routeEntity = new RouteEntity();
		Map<Integer, PointEntity> routeEntityPoints = routeEntity.getPoints();
		Map<Integer, Point> routePoints = route.getPoints();
		for (Map.Entry<Integer, Point> entry: routePoints.entrySet()){
			PointMapper pointMapper = new PointMapper();
			PointEntity pointEntity = new PointEntity();
			pointEntity = pointMapper.getPointEntity(entry.getValue());
			routeEntityPoints.put(entry.getKey(), pointEntity);
		}
		return routeEntity;
	}
	
	public Route getRoute (RouteEntity routeEntity){
		Route route = new Route();
		Map<Integer, PointEntity> routeEntityPoints = routeEntity.getPoints();
		Map<Integer, Point> routePoints = route.getPoints();
		for (Map.Entry<Integer, PointEntity> entry: routeEntityPoints.entrySet()){
			PointMapper pointMapper = new PointMapper();
			Point point = new Point();
			point = pointMapper.getPoint(entry.getValue());
			routePoints.put(entry.getKey(), point);
		}
		return route;
	}

}
