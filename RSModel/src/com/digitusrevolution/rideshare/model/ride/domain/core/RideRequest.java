package com.digitusrevolution.rideshare.model.ride.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Passenger;

public class RideRequest {

	private int id;
	private Point pickupPoint;
	private Point dropPoint;
	private Date dateTime;
	private VehicleCategory vehicleCategory;
	private VehicleSubCategory vehicleSubCategory;
	private TrustNetwork trustNetwork;
	private Sex sexPreference;
	private int seatRequired;
	private int luggageCapacityRequired;
	private int pickupPointVariation;
	private int dropPointVariation;
	private String status;
	private Passenger passenger;
	private boolean ridePreference;
	private Collection<Ride> preferredRides = new ArrayList<Ride>();
	private Ride acceptedRide;
	private PointEntity ridePickupPoint;
	private PointEntity rideDropPoint;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
	public Passenger getPassenger() {
		return passenger;
	}
	public void setPassenger(Passenger passenger) {
		this.passenger = passenger;
	}
	public Collection<Ride> getPreferredRides() {
		return preferredRides;
	}
	public void setPreferredRides(Collection<Ride> preferredRides) {
		this.preferredRides = preferredRides;
	}
	public boolean isRidePreference() {
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
	public PointEntity getRidePickupPoint() {
		return ridePickupPoint;
	}
	public void setRidePickupPoint(PointEntity ridePickupPoint) {
		this.ridePickupPoint = ridePickupPoint;
	}
	public PointEntity getRideDropPoint() {
		return rideDropPoint;
	}
	public void setRideDropPoint(PointEntity rideDropPoint) {
		this.rideDropPoint = rideDropPoint;
	}	

}
