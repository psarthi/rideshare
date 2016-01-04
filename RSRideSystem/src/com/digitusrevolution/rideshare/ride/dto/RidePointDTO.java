package com.digitusrevolution.rideshare.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;

public class RidePointDTO {
	
	private RidePoint ridePoint;
	private double distance;
	
	public RidePoint getRidePoint() {
		return ridePoint;
	}
	public void setRidePoint(RidePoint ridePoint) {
		this.ridePoint = ridePoint;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	

}
