package com.digitusrevolution.rideshare.ride;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import com.digitusrevolution.rideshare.common.RideShareUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.RoutePoint;
import com.digitusrevolution.rideshare.ride.domain.PointDO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;

public class RideSystemTest {

	public static void main(String[] args) {

		/*		PointDO pointDO = new PointDO();
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
		 */
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM d yyyy hh:mm a");
		LocalDateTime dateTime = LocalDateTime.of(2015, Month.DECEMBER, 7, 9, 30);
		ZoneId zoneId = ZoneId.of("Asia/Kolkata"); 
		ZonedDateTime zonedDateTime = ZonedDateTime.of(dateTime, zoneId);

		System.out.println(dateTime +"," + zonedDateTime.format(formatter) +","+zoneId);

		ZonedDateTime dateTime1 = zonedDateTime.toOffsetDateTime().atZoneSameInstant(ZoneId.of("Australia/Canberra"));		
		System.out.println(dateTime1.format(formatter));
		
		ZonedDateTime dateTimeUTC = zonedDateTime.toOffsetDateTime().atZoneSameInstant(ZoneId.of("UTC"));
		System.out.println(dateTimeUTC.format(formatter));
		
		Set<String> strings = ZoneId.getAvailableZoneIds();
		for (String string : strings) {
			System.out.println(string+","+ZoneId.of(string));
		}


		
		
	}

}
