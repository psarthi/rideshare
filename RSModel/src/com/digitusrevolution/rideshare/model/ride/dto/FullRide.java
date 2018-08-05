package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class FullRide extends BasicRide{

	private Route route;
	
	private Collection<BasicRidePassenger> ridePassengers = new HashSet<BasicRidePassenger>();
	private Collection<FullRideRequest> acceptedRideRequests = new HashSet<FullRideRequest>();
	private Collection<FullRideRequest> cancelledRideRequests = new HashSet<FullRideRequest>();
	

	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Collection<BasicRidePassenger> getRidePassengers() {
		return ridePassengers;
	}
	public void setRidePassengers(Collection<BasicRidePassenger> ridePassengers) {
		this.ridePassengers = ridePassengers;
	}
	public Collection<FullRideRequest> getAcceptedRideRequests() {
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<FullRideRequest> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
	}
	public Collection<FullRideRequest> getCancelledRideRequests() {
		return cancelledRideRequests;
	}
	public void setCancelledRideRequests(Collection<FullRideRequest> cancelledRideRequests) {
		this.cancelledRideRequests = cancelledRideRequests;
	}
}
