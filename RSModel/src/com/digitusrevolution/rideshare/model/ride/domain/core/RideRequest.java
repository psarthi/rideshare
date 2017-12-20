package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.data.UserFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

//This can help in getting just id instead of object but its causing issue while deserialization, so for now lets park it.
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property="id")
public class RideRequest implements Comparable<RideRequest>{

	private int id;
	private RideRequestPoint pickupPoint = new RideRequestPoint();
	private RideRequestPoint dropPoint = new RideRequestPoint();
	private String pickupPointAddress;
	private String dropPointAddress;
	private ZonedDateTime pickupTime;
	private LocalTime pickupTimeVariation;
	private VehicleCategory vehicleCategory;
	private VehicleSubCategory vehicleSubCategory;
	private TrustNetwork trustNetwork;
	private Sex sexPreference;
	private int seatRequired;
	private int luggageCapacityRequired;
	private int pickupPointVariation;
	private int dropPointVariation;
	private RideRequestStatus status;
	//@JsonIdentityReference(alwaysAsId=true)
	private User passenger;
	private PassengerStatus passengerStatus;
	private boolean ridePreference;
	private Collection<Ride> preferredRides = new HashSet<Ride>();
	private Ride acceptedRide;
	private RidePoint ridePickupPoint = new RidePoint();
	private RidePoint rideDropPoint = new RidePoint();
	private String ridePickupPointAddress;
	private String rideDropPointAddress;
	private double ridePickupPointDistance;
	private double rideDropPointDistance;
	private int travelTime;
	private int travelDistance;
	private Collection<Ride> cancelledRides = new HashSet<Ride>();
	private RideMode rideMode;
	private Bill bill;
	private String confirmationCode;
	private Collection<UserFeedback> feedbacks = new HashSet<UserFeedback>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public RideRequestPoint getPickupPoint() {
		return pickupPoint;
	}
	public void setPickupPoint(RideRequestPoint pickupPoint) {
		this.pickupPoint = pickupPoint;
	}
	public RideRequestPoint getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(RideRequestPoint dropPoint) {
		this.dropPoint = dropPoint;
	}
	public ZonedDateTime getPickupTime() {
		return pickupTime;
	}
	public void setPickupTime(ZonedDateTime pickupTime) {
		this.pickupTime = pickupTime;
	}
	public LocalTime getPickupTimeVariation() {
		return pickupTimeVariation;
	}
	public void setPickupTimeVariation(LocalTime pickupTimeVariation) {
		this.pickupTimeVariation = pickupTimeVariation;
	}
	public VehicleCategory getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public VehicleSubCategory getVehicleSubCategory() {
		return vehicleSubCategory;
	}
	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
	}
	public TrustNetwork getTrustNetwork() {
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
	}
	public int getSeatRequired() {
		return seatRequired;
	}
	public void setSeatRequired(int seatRequired) {
		this.seatRequired = seatRequired;
	}
	public int getLuggageCapacityRequired() {
		return luggageCapacityRequired;
	}
	public void setLuggageCapacityRequired(int luggageCapacityRequired) {
		this.luggageCapacityRequired = luggageCapacityRequired;
	}
	public RideRequestStatus getStatus() {
		return status;
	}
	public void setStatus(RideRequestStatus status) {
		this.status = status;
	}
	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
	}
	public int getPickupPointVariation() {
		return pickupPointVariation;
	}
	public void setPickupPointVariation(int pickupPointVariation) {
		this.pickupPointVariation = pickupPointVariation;
	}
	public int getDropPointVariation() {
		return dropPointVariation;
	}
	public void setDropPointVariation(int dropPointVariation) {
		this.dropPointVariation = dropPointVariation;
	}
	public User getPassenger() {
		return passenger;
	}
	public void setPassenger(User passenger) {
		this.passenger = passenger;
	}
	public Collection<Ride> getPreferredRides() {
		return preferredRides;
	}
	public void setPreferredRides(Collection<Ride> preferredRides) {
		this.preferredRides = preferredRides;
	}
	public boolean getRidePreference() {
		return ridePreference;
	}
	public void setRidePreference(boolean ridePreference) {
		this.ridePreference = ridePreference;
	}
	public Ride getAcceptedRide() {
		return acceptedRide;
	}
	public void setAcceptedRide(Ride acceptedRide) {
		this.acceptedRide = acceptedRide;
	}
	public RidePoint getRidePickupPoint() {
		return ridePickupPoint;
	}
	public void setRidePickupPoint(RidePoint ridePickupPoint) {
		this.ridePickupPoint = ridePickupPoint;
	}
	public RidePoint getRideDropPoint() {
		return rideDropPoint;
	}
	public void setRideDropPoint(RidePoint rideDropPoint) {
		this.rideDropPoint = rideDropPoint;
	}
	public int getTravelTime() {
		return travelTime;
	}
	public void setTravelTime(int travelTime) {
		this.travelTime = travelTime;
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
		result = prime * result + id;
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
		if (!(obj instanceof RideRequest)) {
			return false;
		}
		RideRequest other = (RideRequest) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public Collection<Ride> getCancelledRides() {
		return cancelledRides;
	}
	public void setCancelledRides(Collection<Ride> cancelledRides) {
		this.cancelledRides = cancelledRides;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
	}
	public String getPickupPointAddress() {
		return pickupPointAddress;
	}
	public void setPickupPointAddress(String pickupPointAddress) {
		this.pickupPointAddress = pickupPointAddress;
	}
	public String getDropPointAddress() {
		return dropPointAddress;
	}
	public void setDropPointAddress(String dropPointAddress) {
		this.dropPointAddress = dropPointAddress;
	}
	public String getRidePickupPointAddress() {
		return ridePickupPointAddress;
	}
	public void setRidePickupPointAddress(String ridePickupPointAddress) {
		this.ridePickupPointAddress = ridePickupPointAddress;
	}
	public String getRideDropPointAddress() {
		return rideDropPointAddress;
	}
	public void setRideDropPointAddress(String rideDropPointAddress) {
		this.rideDropPointAddress = rideDropPointAddress;
	}
	public Bill getBill() {
		return bill;
	}
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	public PassengerStatus getPassengerStatus() {
		return passengerStatus;
	}
	public void setPassengerStatus(PassengerStatus passengerStatus) {
		this.passengerStatus = passengerStatus;
	}
	public double getRidePickupPointDistance() {
		return ridePickupPointDistance;
	}
	public void setRidePickupPointDistance(double ridePickupPointDistance) {
		this.ridePickupPointDistance = ridePickupPointDistance;
	}
	public double getRideDropPointDistance() {
		return rideDropPointDistance;
	}
	public void setRideDropPointDistance(double rideDropPointDistance) {
		this.rideDropPointDistance = rideDropPointDistance;
	}
	@Override
	public int compareTo(RideRequest rideRequest) {
		//ascending order
		//return this.id - rideRequest.id;

		//descending order
		return rideRequest.id - this.id;
	}
	public String getConfirmationCode() {
		return confirmationCode;
	}
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	public Collection<UserFeedback> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Collection<UserFeedback> feedbacks) {
		this.feedbacks = feedbacks;
	}
	
}
