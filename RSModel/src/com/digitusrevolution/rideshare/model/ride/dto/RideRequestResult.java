package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.List;

public class RideRequestResult {

	private boolean currentRideRequest;
	private FullRideRequest rideRequest;
	private List<SuggestedMatchedRideInfo> suggestedMatchedRideInfos;
	
	public boolean isCurrentRideRequest() {
		return currentRideRequest;
	}
	public void setCurrentRideRequest(boolean currentRideRequest) {
		this.currentRideRequest = currentRideRequest;
	}
	public FullRideRequest getRideRequest() {
		return rideRequest;
	}
	public void setRideRequest(FullRideRequest rideRequest) {
		this.rideRequest = rideRequest;
	}
	public List<SuggestedMatchedRideInfo> getSuggestedMatchedRideInfos() {
		return suggestedMatchedRideInfos;
	}
	public void setSuggestedMatchedRideInfos(List<SuggestedMatchedRideInfo> suggestedMatchedRideInfos) {
		this.suggestedMatchedRideInfos = suggestedMatchedRideInfos;
	}

}
