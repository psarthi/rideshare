package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

public class Passenger extends User{

	private Collection<RideRequest> rideRequests = new ArrayList<RideRequest>();
	private Collection<Bill> bills = new ArrayList<Bill>();
	private Collection<Ride> rides = new ArrayList<Ride>();
	
	public Collection<RideRequest> getRideRequests() {
		return rideRequests;
	}
	public void setRideRequests(Collection<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}
	public Collection<Bill> getBills() {
		return bills;
	}
	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}
	public Collection<Ride> getRides() {
		return rides;
	}
	public void setRides(Collection<Ride> rides) {
		this.rides = rides;
	}
	

}
