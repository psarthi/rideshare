package com.digitusrevolution.rideshare.common.util.external;

public class MathUtil {
	
	/**
	 * Extend the Number object to convert degrees to radians
	 *
	 * @return {Number} Bearing in radians
	 * @ignore
	 */ 
	public static double toRad(double value) {
		return value * Math.PI / 180;
	};

	/**
	 * Extend the Number object to convert radians to degrees
	 *
	 * @return {Number} Bearing in degrees
	 * @ignore
	 */ 
	public static double toDeg(double value) {
		return value * 180 / Math.PI;
	};

	/**
	 * Normalize a heading in degrees to between 0 and +360
	 *
	 * @return {Number} Return 
	 * @ignore
	 */ 
	public static double toBrng(double value) {
		return (toDeg(value) + 360) % 360;
	};
	
}
