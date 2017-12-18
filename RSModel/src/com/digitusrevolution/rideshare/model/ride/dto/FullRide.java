package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;

public class FullRide extends BasicRide{

	private Route route;
	private boolean recur;
	private RecurringDetail recurringDetail;

	private Collection<BasicRideRequest> acceptedRideRequests = new HashSet<BasicRideRequest>();
	private Collection<BasicRideRequest> rejectedRideRequests = new HashSet<BasicRideRequest>();
	private Collection<BasicRideRequest> cancelledRideRequests = new HashSet<BasicRideRequest>();
	

	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public boolean isRecur() {
		return recur;
	}
	public void setRecur(boolean recur) {
		this.recur = recur;
	}
	public RecurringDetail getRecurringDetail() {
		return recurringDetail;
	}
	public void setRecurringDetail(RecurringDetail recurringDetail) {
		this.recurringDetail = recurringDetail;
	}
	public Collection<BasicRideRequest> getAcceptedRideRequests() {
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<BasicRideRequest> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
	}
	public Collection<BasicRideRequest> getRejectedRideRequests() {
		return rejectedRideRequests;
	}
	public void setRejectedRideRequests(Collection<BasicRideRequest> rejectedRideRequests) {
		this.rejectedRideRequests = rejectedRideRequests;
	}
	public Collection<BasicRideRequest> getCancelledRideRequests() {
		return cancelledRideRequests;
	}
	public void setCancelledRideRequests(Collection<BasicRideRequest> cancelledRideRequests) {
		this.cancelledRideRequests = cancelledRideRequests;
	}
}
