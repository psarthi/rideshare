package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;

public class Driver extends User{
	
	private List<Ride> rides;

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}


}
