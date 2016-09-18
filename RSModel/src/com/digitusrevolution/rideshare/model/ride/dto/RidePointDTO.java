package com.digitusrevolution.rideshare.model.ride.dto;

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

/*	@Override
	public String toString() {
		JSONUtil<RidePointDTO> jsonUtil = new JSONUtil<>(RidePointDTO.class);
		return jsonUtil.getJson(this);
	}
*/	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ridePoint == null) ? 0 : ridePoint.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof RidePointDTO)) {
			return false;
		}
		RidePointDTO other = (RidePointDTO) obj;
		if (ridePoint == null) {
			if (other.ridePoint != null) {
				return false;
			}
		} else if (!ridePoint.equals(other.ridePoint)) {
			return false;
		}
		return true;
	}

	
}
