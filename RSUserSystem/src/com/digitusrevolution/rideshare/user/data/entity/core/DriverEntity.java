package com.digitusrevolution.rideshare.user.data.entity.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;

public class DriverEntity extends UserEntity{
	
	private List<Ride> rides;

	public List<Ride> getRides() {
		return rides;
	}

	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}


}
