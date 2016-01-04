package com.digitusrevolution.rideshare.common.util;

import java.time.LocalTime;

public class DateTimeUtil {
	
	public static long getSeconds(LocalTime localTime){
		long seconds = (localTime.getHour()*60 +localTime.getMinute())*60 + localTime.getSecond();
		return seconds;
	}

}
