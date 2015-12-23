package com.digitusrevolution.rideshare.ride;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
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
		RouteTest routeTest = new RouteTest();
		
		routeDO.getRoute(googleDirection, routeTest.getSampleRidesBasicInfo());
	}
	
	public List<RideBasicInfo> getSampleRidesBasicInfo(){
		ZonedDateTime startDateTime = ZonedDateTime.now(ZoneOffset.UTC);
		List<RideBasicInfo> ridesBasicInfo = new ArrayList<>(); 
		RideBasicInfo rideBasicInfo1 = new RideBasicInfo();
		rideBasicInfo1.setId(1);
		rideBasicInfo1.setDateTime(startDateTime);
		ridesBasicInfo.add(rideBasicInfo1);

		RideBasicInfo rideBasicInfo2 = new RideBasicInfo();
		rideBasicInfo2.setId(2);
		rideBasicInfo2.setDateTime(startDateTime.plusDays(1));
		ridesBasicInfo.add(rideBasicInfo2);

		RideBasicInfo rideBasicInfo3 = new RideBasicInfo();
		rideBasicInfo3.setId(3);
		rideBasicInfo3.setDateTime(startDateTime.plusDays(2));
		ridesBasicInfo.add(rideBasicInfo3);
		
		RideBasicInfo rideBasicInfo4 = new RideBasicInfo();
		rideBasicInfo4.setId(4);
		rideBasicInfo4.setDateTime(startDateTime.plusDays(4));
		ridesBasicInfo.add(rideBasicInfo4);

		return ridesBasicInfo;
	}

}
