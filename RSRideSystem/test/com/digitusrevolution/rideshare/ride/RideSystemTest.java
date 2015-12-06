package com.digitusrevolution.rideshare.ride;

import java.util.Collection;
import java.util.Map;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.RoutePoint;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;

public class RideSystemTest {

	public static void main(String[] args) {
		
		PointDO pointDO = new PointDO();
		Point startPoint = pointDO.getCordinates("Gopalan Grandeur Bangalore");
		System.out.println("Start Point Lat/Lang: " + startPoint.getLatitude()+","+startPoint.getLongitude());	
		
		PointDO pointDO1 = new PointDO();
		Point endPoint = pointDO1.getCordinates("Silk Board Bangalore");
		System.out.println("End Point Lat/Lang: " + endPoint.getLatitude()+","+endPoint.getLongitude());

		RouteDO routeDO = new RouteDO();
		Route route = routeDO.getRoute(startPoint, endPoint);
		Collection<RoutePoint> routePoints = route.getRoutePoints();
		
		for (RoutePoint routePoint : routePoints) {
			
			System.out.println("Route Points are with sequence,lat,lng:" + routePoint.getSequence()+":"+routePoint.getPoint().getLatitude()+","+routePoint.getPoint().getLongitude());
		}

			

	}

}
