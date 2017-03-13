package com.digitusrevolution.rideshare.model.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;

public class RideOfferDTO {

	private Ride ride;
	private GoogleDirection googleDirection;
	
	public Ride getRide() {
		return ride;
	}
	public void setRide(Ride ride) {
		this.ride = ride;
	}
	public GoogleDirection getGoogleDirection() {
		return googleDirection;
	}
	public void setGoogleDirection(GoogleDirection googleDirection) {
		this.googleDirection = googleDirection;
	}
	
	
}
