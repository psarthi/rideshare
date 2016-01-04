package com.digitusrevolution.rideshare.ride.dto;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;

public class RidePointDTO {
	
	private RidePoint ridePoint;
	private double distance;
	
	public RidePoint getRidePoint() {
		return ridePoint;
	}
	public void setRidePoint(RidePoint ridePoint) {
		this.ridePoint = ridePoint;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}

	//This is required for maps to compare with each other using entrySet. Its not required if we compare maps with keyset
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof RidePointDTO){
			RidePointDTO ridePointDTO = (RidePointDTO) obj;
			return this.ridePoint.equals(ridePointDTO.ridePoint);	
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.ridePoint.hashCode();	
	}

	
}
