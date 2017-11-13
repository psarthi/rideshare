package com.digitusrevolution.rideshare.model.billing.dto;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

public class TripInfo {
	
	private Ride ride;
	private RideRequest rideRequest;
	
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	public RideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}
	
	

}
