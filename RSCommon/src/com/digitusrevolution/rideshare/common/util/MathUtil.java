package com.digitusrevolution.rideshare.common.util;

public class MathUtil {
	
	public static double getSpeed(double distance, double time){
		double speed = distance / time ;		
		return speed;
	}
	
	public static double getTime(double distance, double speed){
		double time = distance / speed;
		return time;
	}
}
