package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.ArrayList;
import java.util.Collection;

public class Route {
	
	private Collection<RidePoint> ridePoints = new ArrayList<RidePoint>();

	public Collection<RidePoint> getRidePoints() {
		return ridePoints;
	}
	
	public void setRidePoints(Collection<RidePoint> ridePoints) {
		this.ridePoints = ridePoints;
	}
}
