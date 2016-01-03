package com.digitusrevolution.rideshare.model.ride.domain;

import java.time.ZonedDateTime;

public class RideRequestPoint {
	
	private String _id;
	private Point point = new Point();
	private int rideRequestId;
	private ZonedDateTime dateTime;
	//Distance would hold the distance of this point from any other point during search of nearby points
	private double distance;
	
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public Point getPoint() {
		return point;
	}
	public void setPoint(Point point) {
		this.point = point;
	}
	public int getRideRequestId() {
		return rideRequestId;
	}
	public void setRideRequestId(int rideRequestId) {
		this.rideRequestId = rideRequestId;
	}
	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
