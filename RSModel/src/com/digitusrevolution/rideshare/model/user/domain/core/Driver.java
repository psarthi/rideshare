package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;

public class Driver extends User{
	
	private Collection<Ride> rides = new ArrayList<Ride>();

	public Collection<Ride> getRides() {
		return rides;
	}

	public void setRides(Collection<Ride> rides) {
		this.rides = rides;
	}


}
