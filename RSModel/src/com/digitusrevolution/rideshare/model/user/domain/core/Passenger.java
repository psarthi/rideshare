package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

public class Passenger extends User{

	private int passengerId;
	private List<RideRequest> rideRequests;
	private List<Bill> bills;
	private List<Ride> rides;
	private Point pickupPoint;
	private Point dropPoint;
	
	public int getPassengerId() {
		return passengerId;
	}
	public void setPassengerId(int passengerId) {
		this.passengerId = passengerId;
	}
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
	public Point getPickupPoint() {
		return pickupPoint;
	}
	public void setPickupPoint(Point pickupPoint) {
		this.pickupPoint = pickupPoint;
	}
	public Point getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(Point dropPoint) {
		this.dropPoint = dropPoint;
	}	
}
