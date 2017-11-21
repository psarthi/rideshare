package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;

public class UserSignInResult {

	private String token;
	private BasicUser userProfile;
	private FullRide currentRide;
	private FullRideRequest currentRideRequest;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public BasicUser getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(BasicUser userProfile) {
		this.userProfile = userProfile;
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
