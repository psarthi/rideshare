package com.digitusrevolution.rideshare.ride;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.domain.resource.RideDomainResource;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;
import com.mysql.fabric.FabricStateResponse;

public class RideTest {
	
	public static void main(String[] args) {
				
		ZonedDateTime firstOfWeek = DateTimeUtil.getCurrentTimeInUTC().with ( ChronoField.DAY_OF_WEEK , 1 ); // ISO 8601, Monday is first day of week.
		ZonedDateTime firstOfNextWeek = firstOfWeek.plusWeeks ( 1 );
		System.out.println(DateTimeUtil.getCurrentTimeInUTC());
		System.out.println(firstOfWeek);
		System.out.println(firstOfNextWeek);
	}

}
