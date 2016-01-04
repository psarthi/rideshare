package com.digitusrevolution.rideshare.ride.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RideSearchResult {
	
	@JsonProperty("_id")
	private int rideId;
	@JsonProperty("rideSearchPoint")
	private RideSearchPoint rideSearchPoint;

	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public RideSearchPoint getRideSearchPoint() {
		return rideSearchPoint;
	}
	public void setRideSearchPoint(RideSearchPoint rideSearchPoint) {
		this.rideSearchPoint = rideSearchPoint;
	}	
}
