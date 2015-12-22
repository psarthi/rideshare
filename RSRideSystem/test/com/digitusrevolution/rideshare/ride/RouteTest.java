package com.digitusrevolution.rideshare.ride;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

public class RouteTest {
	
	public static void main(String[] args) {
		RouteDO routeDO = new RouteDO();
		PointDO pointDO = new PointDO();
		Point startPoint = pointDO.getCordinates("Hoodi Circle Bangalore");
		Point endPoint = pointDO.getCordinates("Silk Board Bangalore");
		GoogleDirection googleDirection = routeDO.getDirection(startPoint, endPoint);
		ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		routeDO.processRoute(googleDirection, startDateTime, 1);
	}

}
