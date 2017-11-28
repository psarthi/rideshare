package com.digitusrevolution.rideshare.model.ride.dto;

import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;

public class RideOfferInfo {

	private BasicRide ride;
	private GoogleDirection googleDirection;
	
	public BasicRide getRide() {
		return ride;
	}
	public void setRide(BasicRide ride) {
		this.ride = ride;
	}
	public GoogleDirection getGoogleDirection() {
		return googleDirection;
	}
	public void setGoogleDirection(GoogleDirection googleDirection) {
		this.googleDirection = googleDirection;
	}
	
	
}
