package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

public class Passenger extends User{

	private List<RideRequest> rideRequests;
	private List<Bill> bills;
	private List<Ride> rides;
	
	public List<RideRequest> getRideRequests() {
		return rideRequests;
	}
	public void setRideRequests(List<RideRequest> rideRequests) {
		this.rideRequests = rideRequests;
	}
	public List<Bill> getBills() {
		return bills;
	}
	public void setBills(List<Bill> bills) {
		this.bills = bills;
	}
	public List<Ride> getRides() {
		return rides;
	}
	public void setRides(List<Ride> rides) {
		this.rides = rides;
	}
}
