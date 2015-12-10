package com.digitusrevolution.rideshare.model.ride.domain;

public class Point {
	
	private int id;
	private double latitude;
	private double longitude;
	
	public Point() {
	 // This is mainly required for Hibernate for using proxy object
	}
	
	public Point(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	@Override
	public String toString() {
		return "Point [x=" + latitude + ", y=" + longitude + "]";
	}
}
