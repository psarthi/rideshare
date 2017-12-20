package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;

public class UserSignInResult {

	private String token;
	private BasicUser user;
	private FullRide currentRide;
	private FullRideRequest currentRideRequest;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public BasicUser getUser() {
		return user;
	}
	public void setUser(BasicUser user) {
		this.user = user;
	}
	public FullRide getCurrentRide() {
		return currentRide;
	}
	public void setCurrentRide(FullRide currentRide) {
		this.currentRide = currentRide;
	}
	public FullRideRequest getCurrentRideRequest() {
		return currentRideRequest;
	}
	public void setCurrentRideRequest(FullRideRequest currentRideRequest) {
		this.currentRideRequest = currentRideRequest;
	}

}
