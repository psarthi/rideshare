package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RecurringDetail;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//This can help in getting just id instead of object but its causing issue while deserialization, so for now lets park it.
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class Ride {

	//id data type needs to be finalized later, whether to use int, long, string
	private int id;
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	private RidePoint startPoint = new RidePoint();
	private RidePoint endPoint = new RidePoint();
	private String startPointAddress;
	private String endPointAddress;
	private int seatOffered;
	private int luggageCapacityOffered;
	private Sex sexPreference;
	private TrustNetwork trustNetwork;
	private Route route;
	private boolean recur;
	private RecurringDetail recurringDetail;
	private RideStatus status;
	private RideSeatStatus seatStatus;
	private Vehicle vehicle;
	//@JsonIdentityReference(alwaysAsId=true)
	private User driver;
	private Collection<RidePassenger> ridePassengers = new HashSet<RidePassenger>();
	private Collection<Bill> bills = new HashSet<Bill>();
	private Collection<RideRequest> acceptedRideRequests = new HashSet<RideRequest>();
	private Collection<RideRequest> rejectedRideRequests = new HashSet<RideRequest>();
	private Collection<RideRequest> cancelledRideRequests = new HashSet<RideRequest>();
	private int travelDistance;
	private RideMode rideMode;
	
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
	public RideStatus getStatus() {
		return status;
	}
	public void setStatus(RideStatus status) {
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
	public Collection<RidePassenger> getRidePassengers() {
		return ridePassengers;
	}
	public void setRidePassengers(Collection<RidePassenger> ridePassengers) {
		this.ridePassengers = ridePassengers;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + id;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Ride)) {
			return false;
		}
		Ride other = (Ride) obj;
		if (driver == null) {
			if (other.driver != null) {
				return false;
			}
		} else if (!driver.equals(other.driver)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (startTime == null) {
			if (other.startTime != null) {
				return false;
			}
		} else if (!startTime.equals(other.startTime)) {
			return false;
		}
		if (vehicle == null) {
			if (other.vehicle != null) {
				return false;
			}
		} else if (!vehicle.equals(other.vehicle)) {
			return false;
		}
		return true;
	}
	public Collection<RideRequest> getCancelledRideRequests() {
		return cancelledRideRequests;
	}
	public void setCancelledRideRequests(Collection<RideRequest> cancelledRideRequests) {
		this.cancelledRideRequests = cancelledRideRequests;
	}
	public RideSeatStatus getSeatStatus() {
		return seatStatus;
	}
	public void setSeatStatus(RideSeatStatus seatStatus) {
		this.seatStatus = seatStatus;
	}
	public RideRequest getRideRequestOfPassenger(int passengerId){
		Collection<RideRequest> acceptedRideRequests = getAcceptedRideRequests();
		for (RideRequest rideRequest : acceptedRideRequests) {
			if (rideRequest.getPassenger().getId() == passengerId){
				return rideRequest;
			}
		}
		throw new RuntimeException("Ride Request not found for passenger id:"+passengerId);
	}
	public RidePassenger getRidePassenger(int passengerId){
		Collection<RidePassenger> passengers = getRidePassengers();
		for (RidePassenger ridePassenger : passengers) {
			if (ridePassenger.getPassenger().getId() == passengerId){
				return ridePassenger;
			}
		}
		throw new RuntimeException("No passenger found with id:"+passengerId);
	}
	public ZonedDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
	}
	public String getStartPointAddress() {
		return startPointAddress;
	}
	public void setStartPointAddress(String startPointAddress) {
		this.startPointAddress = startPointAddress;
	}
	public String getEndPointAddress() {
		return endPointAddress;
	}
	public void setEndPointAddress(String endPointAddress) {
		this.endPointAddress = endPointAddress;
	}

	
}
