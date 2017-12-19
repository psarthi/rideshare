package com.digitusrevolution.rideshare.model.ride.dto;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;

public class FullRideRequest extends BasicRideRequest{

	private FullRide acceptedRide;
	private Bill bill;
	private Collection<FullRide> preferredRides = new HashSet<FullRide>();
	private Collection<FullRide> cancelledRides = new HashSet<FullRide>();

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
	public Collection<FullRide> getPreferredRides() {
		return preferredRides;
	}
	public void setPreferredRides(Collection<FullRide> preferredRides) {
		this.preferredRides = preferredRides;
	}
	public Collection<FullRide> getCancelledRides() {
		return cancelledRides;
	}
	public void setCancelledRides(Collection<FullRide> cancelledRides) {
		this.cancelledRides = cancelledRides;
	}
}
