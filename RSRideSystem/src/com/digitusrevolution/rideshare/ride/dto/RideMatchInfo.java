package com.digitusrevolution.rideshare.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;

public class RideMatchInfo {

	private int rideId;
	private int rideRequestId;
	private RidePoint ridePickupPoint;
	private RidePoint rideDropPoint;
	private double pickupPointDistance;
	private double dropPointDistance;
	private double travelDistance;

	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public int getRideRequestId() {
		return rideRequestId;
	}
	public void setRideRequestId(int rideRequestId) {
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
	public double getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(double travelDistance) {
		this.travelDistance = travelDistance;
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
	@Override
	public String toString() {
		return "[rideId,rideRequestId,RidePickupPoint,RideDropPoint,pickupPointDistance,dropPointDistance,travelDistance]:"+this.rideId+","
				+this.rideRequestId+","+this.ridePickupPoint.toString()+","+this.rideDropPoint.toString()
				+","+this.pickupPointDistance+","+this.dropPointDistance+","+this.travelDistance+"\n";
	}

}
