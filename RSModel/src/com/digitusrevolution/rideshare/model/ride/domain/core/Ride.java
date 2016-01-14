package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class Ride {

	//id data type needs to be finalized later, whether to use int, long, string
	private int id;
	private ZonedDateTime startTime;
	private RidePoint startPoint = new RidePoint();
	private RidePoint endPoint = new RidePoint();
	private int seatOffered;
	private int luggageCapacityOffered;
	private Sex sexPreference;
	private TrustNetwork trustNetwork;
	private Route route;
	private boolean recur;
	private RecurringDetail recurringDetail;
	private String status;
	private Vehicle vehicle;
	private User driver;
	private Collection<User> passengers = new ArrayList<User>();
	private Collection<Bill> bills = new ArrayList<Bill>();
	private Collection<RideRequest> acceptedRideRequests = new ArrayList<RideRequest>();
	private Collection<RideRequest> rejectedRideRequests = new ArrayList<RideRequest>();
	private int travelDistance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ZonedDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
	}
	public RidePoint getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(RidePoint startPoint) {
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
	public RidePoint getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(RidePoint endPoint) {
		this.endPoint = endPoint;
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
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
	public Collection<User> getPassengers() {
		return passengers;
	}
	public void setPassengers(Collection<User> passengers) {
		this.passengers = passengers;
	}
	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
	}
	public int getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(int travelDistance) {
		this.travelDistance = travelDistance;
	}
	
}
