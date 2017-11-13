package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.Collection;
import java.util.HashSet;

public class FullRideRequest extends BasicRideRequest{

	private Collection<BasicRide> preferredRides = new HashSet<BasicRide>();
	private Collection<BasicRide> cancelledRides = new HashSet<BasicRide>();
	

	public Collection<BasicRide> getPreferredRides() {
		return preferredRides;
	}
	public void setPreferredRides(Collection<BasicRide> preferredRides) {
		this.preferredRides = preferredRides;
	}
	public Collection<BasicRide> getCancelledRides() {
		return cancelledRides;
	}
	public void setCancelledRides(Collection<BasicRide> cancelledRides) {
		this.cancelledRides = cancelledRides;
	}

}
