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
	
	/*
	 * Reference from bearing calculation - 
	 * 
	 * Since atan2 returns values in the range -π ... +π (that is, -180° ... +180°), to normalize the result to a 
	 * compass bearing (in the range 0° ... 360°, with −ve values transformed into the range 180° ... 360°), 
	 * convert to degrees and then use (θ+360) % 360, where % is (floating point) modulo.
	 * 
	 * for e.g. when θ = -129.60, then calculation would be (-129.60 + 360) % 360 = 230.39
	 * when θ = 168.24, then calculation would be (168.24 + 360) % 360 = 168.24
	 * 
	 */
	public static double convertToCompassBearing(double degree){
		return (degree+360) % 360;		
	}
	
	public static double getMod360(double degree){
		return degree % 360;
	}
}
