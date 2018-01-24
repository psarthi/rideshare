package com.digitusrevolution.rideshare.model.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;

public class MatchedTripInfo {

	private long rideId;
	private long rideRequestId;
	private RidePoint ridePickupPoint;
	private RidePoint rideDropPoint;
	private double pickupPointDistance;
	private double dropPointDistance;
	private double rideRequestTravelDistance;

	public long getRideId() {
		return rideId;
	}
	public void setRideId(long rideId) {
		this.rideId = rideId;
	}
	public long getRideRequestId() {
		return rideRequestId;
	}
	public void setRideRequestId(long rideRequestId) {
		this.rideRequestId = rideRequestId;
	}
	public RidePoint getRidePickupPoint() {
		return ridePickupPoint;
	}
	public void setRidePickupPoint(RidePoint ridePickupPoint) {
		this.ridePickupPoint = ridePickupPoint;
	}
	public RidePoint getRideDropPoint() {
		return rideDropPoint;
	}
	public void setRideDropPoint(RidePoint rideDropPoint) {
		this.rideDropPoint = rideDropPoint;
	}
	public double getRideRequestTravelDistance() {
		return rideRequestTravelDistance;
	}
	public void setRideRequestTravelDistance(double rideRequestTravelDistance) {
		this.rideRequestTravelDistance = rideRequestTravelDistance;
	}
	public double getPickupPointDistance() {
		return pickupPointDistance;
	}
	public void setPickupPointDistance(double pickupPointDistance) {
		this.pickupPointDistance = pickupPointDistance;
	}
	public double getDropPointDistance() {
		return dropPointDistance;
	}
	public void setDropPointDistance(double dropPointDistance) {
		this.dropPointDistance = dropPointDistance;
	}
/*	@Override
	public String toString() {
		JSONUtil<MatchedTripInfo> jsonUtil = new JSONUtil<>(MatchedTripInfo.class);
		return jsonUtil.getJson(this);
	}
*/}
