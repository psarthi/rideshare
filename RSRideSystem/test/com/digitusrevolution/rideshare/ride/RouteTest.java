package com.digitusrevolution.rideshare.ride;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePointProperty;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;

public class RouteTest {
	
	public static void main(String[] args) {
		RouteDO routeDO = new RouteDO();
		PointDO pointDO = new PointDO();
		Point startPoint = pointDO.getCordinates("Hoodi Circle Bangalore");
		Point endPoint = pointDO.getCordinates("Silk Board Bangalore");
		
		LocalDateTime localDateTime = LocalDateTime.of(2015, Month.DECEMBER, 8, 9, 30);
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZonedDateTime startTime = ZonedDateTime.of(localDateTime, india);
		ZonedDateTime startTimeUTC = startTime.withZoneSameInstant(ZoneOffset.UTC);

		
		GoogleDirection googleDirection = routeDO.getDirection(startPoint, endPoint,startTimeUTC);
		RouteTest routeTest = new RouteTest();
		
		routeDO.getRoute(googleDirection, routeTest.getSampleRidePointProperties());
	}
	
	public List<RidePointProperty> getSampleRidePointProperties(){
		ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		List<RidePointProperty> ridePointProperties = new ArrayList<>(); 
		RidePointProperty ridePointProperty1 = new RidePointProperty();
		ridePointProperty1.setId(1);
		ridePointProperty1.setDateTime(startDateTime);
		ridePointProperties.add(ridePointProperty1);

		RidePointProperty ridePointProperty2 = new RidePointProperty();
		ridePointProperty2.setId(2);
		ridePointProperty2.setDateTime(startDateTime.plusDays(1));
		ridePointProperties.add(ridePointProperty2);

		RidePointProperty ridePointProperty3 = new RidePointProperty();
		ridePointProperty3.setId(3);
		ridePointProperty3.setDateTime(startDateTime.plusDays(2));
		ridePointProperties.add(ridePointProperty3);
		
		RidePointProperty ridePointProperty4 = new RidePointProperty();
		ridePointProperty4.setId(4);
		ridePointProperty4.setDateTime(startDateTime.plusDays(4));
		ridePointProperties.add(ridePointProperty4);

		return ridePointProperties;
	}

}
