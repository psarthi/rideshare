package com.digitusrevolution.rideshare.model.ride.dto;

public class RideOfferResult {

	private boolean currentRide;
	private FullRide ride;
	
	public boolean isCurrentRide() {
		return currentRide;
	}
	public void setCurrentRide(boolean currentRide) {
		this.currentRide = currentRide;
	}
	public FullRide getRide() {
		return ride;
	}
	public void setRide(FullRide ride) {
		this.ride = ride;
	}
}
