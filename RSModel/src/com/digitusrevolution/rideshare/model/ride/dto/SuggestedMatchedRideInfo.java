package com.digitusrevolution.rideshare.model.ride.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

//Reason behind this jsonignore so that it doesn't throw error while converting from Domain Model to DTO which has less fields
@JsonIgnoreProperties (ignoreUnknown=true)
public class SuggestedMatchedRideInfo extends MatchedTripInfo {
	
	private BasicRide ride;
	private String ridePickupPointAddress;
	private String rideDropPointAddress;
	private float price;

	public BasicRide getRide() {
		return ride;
	}
	public void setRide(BasicRide ride) {
		this.ride = ride;
	}
	public String getRidePickupPointAddress() {
		return ridePickupPointAddress;
	}
	public void setRidePickupPointAddress(String ridePickupPointAddress) {
		this.ridePickupPointAddress = ridePickupPointAddress;
	}
	public String getRideDropPointAddress() {
		return rideDropPointAddress;
	}
	public void setRideDropPointAddress(String rideDropPointAddress) {
		this.rideDropPointAddress = rideDropPointAddress;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

}
