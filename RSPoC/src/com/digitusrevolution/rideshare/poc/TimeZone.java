package com.digitusrevolution.rideshare.poc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Set;

import com.digitusrevolution.rideshare.common.util.DateTimeUtil;

public class TimeZone {

	public static void main(String[] args) {

		LocalDateTime dateTime1 = LocalDateTime.now();
		System.out.println("Local Date time w/o timezone: "+dateTime1);
		
		ZoneId utc = ZoneOffset.UTC;
		System.out.println("UTC ZoneID: "+utc.getId());
		ZoneId india = ZoneId.of("Asia/Kolkata");
		ZoneId australia = ZoneId.of("Australia/Canberra");
		
		ZonedDateTime zonedDateTime1 = dateTime1.atZone(india);
		System.out.println(zonedDateTime1.toEpochSecond());
		System.out.println("zonedDateTime1:" + zonedDateTime1);
		System.out.println(zonedDateTime1.getOffset()+","+zonedDateTime1.getZone());
		zonedDateTime1 = zonedDateTime1.withZoneSameInstant(australia);
		System.out.println(zonedDateTime1.getOffset()+","+zonedDateTime1.getZone());
		
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, MMM d yyyy hh:mm a");
		LocalDateTime dateTime2 = LocalDateTime.of(2015, Month.DECEMBER, 7, 9, 30);
		 
		ZonedDateTime zonedDateTime2 = ZonedDateTime.of(dateTime2, india);
		System.out.println("zonedDateTime2: " + zonedDateTime2.format(formatter) +","+india);
		System.out.println(zonedDateTime2.getOffset()+","+zonedDateTime2.getZone());

		 
		ZonedDateTime zonedDateTime3 = zonedDateTime2.withZoneSameInstant(australia);		
		System.out.println(zonedDateTime3.format(formatter)+","+australia);
		System.out.println(zonedDateTime3.getOffset()+","+zonedDateTime3.getZone());
		
		ZonedDateTime zonedDateTime4 = zonedDateTime2.withZoneSameInstant(ZoneOffset.UTC);
		System.out.println(zonedDateTime4.format(formatter)+","+utc);
		System.out.println(zonedDateTime4.getOffset()+","+zonedDateTime4.getZone());
		
		System.out.println("zonedDateTime1:" + zonedDateTime1.withZoneSameInstant(utc));
		System.out.println("zonedDateTime2:" + zonedDateTime2.withZoneSameInstant(utc));
		
		System.out.println("Equal toInstant: " + zonedDateTime1.toInstant().equals(zonedDateTime2));
		System.out.println("compareTo: " + zonedDateTime1.compareTo(zonedDateTime2));
		System.out.println("isAfter: " +zonedDateTime1.isAfter(zonedDateTime2));
		System.out.println("isBefore: " +zonedDateTime1.isBefore(zonedDateTime2));
		
		Set<String> strings = ZoneId.getAvailableZoneIds();
		for (String string : strings) {
			System.out.println(string+","+ZoneId.of(string));
		}


		ZonedDateTime firstOfWeek = DateTimeUtil.getCurrentTimeInUTC().with ( ChronoField.DAY_OF_WEEK , 1 ).with(LocalTime.MIDNIGHT); // ISO 8601, Monday is first day of week.
		ZonedDateTime firstOfNextWeek = firstOfWeek.plusWeeks ( 1 );
		System.out.println("Current Time-"+DateTimeUtil.getCurrentTimeInUTC());
		System.out.println("FirstDayOfWeek"+firstOfWeek);
		System.out.println("FirstDayOfNextWeek"+firstOfNextWeek);
		
		System.out.println(DateTimeUtil.getStartDateOfTheWeek(DateTimeUtil.getCurrentTimeInUTC()));
		System.out.println(DateTimeUtil.getStartDateOfNextWeek(DateTimeUtil.getCurrentTimeInUTC()));
		System.out.println(DateTimeUtil.getCurrentTimeInUTC().with(LocalDate.of(2018, 02, 10)).with(LocalTime.of(12, 30)));
		
		
	}

}
