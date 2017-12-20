package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;

public class UserFeedbackInfo {
	
	private BasicUser givenByUser;
	private BasicRide ride;
	private BasicRideRequest rideRequest;
	private float rating;
	
	public BasicUser getGivenByUser() {
		return givenByUser;
	}
	public void setGivenByUser(BasicUser givenByUser) {
		this.givenByUser = givenByUser;
	}
	public BasicRide getRide() {
		return ride;
	}
	public void setRide(BasicRide ride) {
		this.ride = ride;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public BasicRideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(BasicRideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}

	
}
