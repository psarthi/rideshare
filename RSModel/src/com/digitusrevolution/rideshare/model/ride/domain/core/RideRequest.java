package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RideRequest {

	private int id;
	private RideRequestPoint pickupPoint;
	private RideRequestPoint dropPoint;
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
	private String status;
	private User passenger;
	private boolean ridePreference;
	private Collection<Ride> preferredRides = new ArrayList<Ride>();
	private Ride acceptedRide;
	private RidePoint ridePickupPoint;
	private RidePoint rideDropPoint;
	
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
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

	
}
