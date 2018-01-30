package com.digitusrevolution.rideshare.common.util;

import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class DateTimeUtil {
	
	public static long getSeconds(LocalTime localTime){
		long seconds = (localTime.getHour()*60 +localTime.getMinute())*60 + localTime.getSecond();
		return seconds;
	}

	public static ZonedDateTime getCurrentTimeInUTC(){
		return ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC);
	}
	
	public static String getFormattedDateTimeString(ZonedDateTime dateTime) {
		//dateTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);
		return dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM));
	}
	
}
