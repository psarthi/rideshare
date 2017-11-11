package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserBasicInformationDTO {

	private String token;
	private User userProfile;
	private Ride upcomingRide;
	private RideRequest upcomingRideRequest;
	private Ride currentRide;
	private RideRequest currentRideRequest;
	
	public Ride getCurrentRide() {
		return currentRide;
	}
	public void setCurrentRide(Ride currentRide) {
		this.currentRide = currentRide;
	}
	public RideRequest getCurrentRideRequest() {
		return currentRideRequest;
	}
	public void setCurrentRideRequest(RideRequest currentRideRequest) {
		this.currentRideRequest = currentRideRequest;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public User getUserProfile() {
		return userProfile;
	}
	public void setUserProfile(User userProfile) {
		this.userProfile = userProfile;
	}
	public Ride getUpcomingRide() {
		return upcomingRide;
	}
	public void setUpcomingRide(Ride upcomingRide) {
		this.upcomingRide = upcomingRide;
	}
	public RideRequest getUpcomingRideRequest() {
		return upcomingRideRequest;
	}
	public void setUpcomingRideRequest(RideRequest upcomingRideRequest) {
		this.upcomingRideRequest = upcomingRideRequest;
	}
}
