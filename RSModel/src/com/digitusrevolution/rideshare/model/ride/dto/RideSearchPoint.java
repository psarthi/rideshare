package com.digitusrevolution.rideshare.model.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePointProperty;
import com.fasterxml.jackson.annotation.JsonProperty;


public class RideSearchPoint{
	
	private String _id;
	//This is to match the name with ridepoint object
	@JsonProperty("rides")
	private RidePointProperty ridePointProperty;
	private Point point = new Point();
	private int sequence;
	private double distance;
		
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}	
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
	public int getSequence() {
		return sequence;
	}
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}
	public RidePointProperty getRidePointProperty() {
		return ridePointProperty;
	}
	public void setRidePointProperty(RidePointProperty ridePointProperty) {
		this.ridePointProperty = ridePointProperty;
	}	
/*	@Override
	public String toString() {
		JSONUtil<RideSearchPoint> jsonUtil = new JSONUtil<>(RideSearchPoint.class);
		return jsonUtil.getJson(this);
	}
*/	
}
