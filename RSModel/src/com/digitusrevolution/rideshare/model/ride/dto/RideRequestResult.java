package com.digitusrevolution.rideshare.model.ride.dto;

public class RideRequestResult {

	private boolean currentRideRequest;
	private FullRideRequest rideRequest;
	
	public boolean isCurrentRideRequest() {
		return currentRideRequest;
	}
	public void setCurrentRideRequest(boolean currentRideRequest) {
		this.currentRideRequest = currentRideRequest;
	}
	public FullRideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(FullRideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}
	

}
