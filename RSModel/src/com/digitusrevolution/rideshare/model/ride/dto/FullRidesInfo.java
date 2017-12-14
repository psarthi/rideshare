package com.digitusrevolution.rideshare.model.ride.dto;


public class FullRidesInfo {

	private FullRide ride;
	private FullRideRequest rideRequest;
	
	public FullRide getRide() {
		return ride;
	}
	public void setRide(FullRide ride) {
		this.ride = ride;
	}
	public FullRideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(FullRideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}

	
}
