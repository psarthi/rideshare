package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;

public class FullRideRequest extends BasicRideRequest{

	private FullRide acceptedRide;
	private Bill bill;
	private Collection<FullRide> cancelledRides = new HashSet<FullRide>();
	private Collection<UserFeedback> feedbacks = new LinkedList<UserFeedback>();

	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	public FullRide getAcceptedRide() {
		return acceptedRide;
	}
	public void setAcceptedRide(FullRide acceptedRide) {
		this.acceptedRide = acceptedRide;
	}
	public Collection<FullRide> getCancelledRides() {
		return cancelledRides;
	}
	public void setCancelledRides(Collection<FullRide> cancelledRides) {
		this.cancelledRides = cancelledRides;
	}
	public Collection<UserFeedback> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Collection<UserFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
}
