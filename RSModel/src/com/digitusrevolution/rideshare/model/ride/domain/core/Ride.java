package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.core.Driver;
import com.digitusrevolution.rideshare.model.user.domain.core.Passenger;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class Ride {

	private int id;
	private Date dateTime;
	private Point startPoint;
	private Point endPoint;
	private int seatOffered;
	private int luggageCapacityOffered;
	private TrustNetwork trustNetwork;
	private Route route;
	private boolean recur;
	private RecurringDetail recurringDetail;
	private String status;
	private Vehicle vehicle;
	private Driver driver;
	private Collection<Passenger> passengers = new ArrayList<Passenger>();
	private Collection<Bill> bills = new ArrayList<Bill>();
	private Collection<RideRequest> acceptedRideRequests = new ArrayList<RideRequest>();
	private Collection<RideRequest> rejectedRideRequests = new ArrayList<RideRequest>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public Point getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}
	public int getSeatOffered() {
		return seatOffered;
	}
	public void setSeatOffered(int seatOffered) {
		this.seatOffered = seatOffered;
	}
	public int getLuggageCapacityOffered() {
		return luggageCapacityOffered;
	}
	public void setLuggageCapacityOffered(int luggageCapacityOffered) {
		this.luggageCapacityOffered = luggageCapacityOffered;
	}
	public boolean getRecur() {
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public TrustNetwork getTrustNetwork() {
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
	}
	public Route getRoute() {
		return route;
	}
	public void setRoute(Route route) {
		this.route = route;
	}
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Driver getDriver() {
		return driver;
	}
	public void setDriver(Driver driver) {
		this.driver = driver;
	}
	public Point getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}
	public Collection<Passenger> getPassengers() {
		return passengers;
	}
	public void setPassengers(Collection<Passenger> passengers) {
		this.passengers = passengers;
	}
	public Collection<Bill> getBills() {
		return bills;
	}
	public void setBills(Collection<Bill> bills) {
		this.bills = bills;
	}
	public Collection<RideRequest> getAcceptedRideRequests() {
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<RideRequest> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
	}
	public Collection<RideRequest> getRejectedRideRequests() {
		return rejectedRideRequests;
	}
	public void setRejectedRideRequests(Collection<RideRequest> rejectedRideRequests) {
		this.rejectedRideRequests = rejectedRideRequests;
	}
	
}
