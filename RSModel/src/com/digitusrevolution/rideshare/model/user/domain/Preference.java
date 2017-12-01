package com.digitusrevolution.rideshare.model.user.domain;

import java.time.LocalTime;

import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class Preference {

	private int id;
	//Ride Request Preference
	private VehicleCategory vehicleCategory;
	private VehicleSubCategory vehicleSubCategory;
	private LocalTime pickupTimeVariation;
	private int pickupPointVariation;
	private int dropPointVariation;
	private int seatRequired;
	private int luggageCapacityRequired;
	
	//Ride Preference
	private int seatOffered;
	private int luggageCapacityOffered;
	private Vehicle defaultVehicle;

	//Common Preference
	private TrustCategory trustCategory;
	private Sex sexPreference;
	private float minProfileRating;
	
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
	public LocalTime getPickupTimeVariation() {
		return pickupTimeVariation;
	}
	public void setPickupTimeVariation(LocalTime pickupTimeVariation) {
		this.pickupTimeVariation = pickupTimeVariation;
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
	public TrustCategory getTrustCategory() {
		return trustCategory;
	}
	public void setTrustCategory(TrustCategory trustCategory) {
		this.trustCategory = trustCategory;
	}
	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
	}
	public float getMinProfileRating() {
		return minProfileRating;
	}
	public void setMinProfileRating(float minProfileRating) {
		this.minProfileRating = minProfileRating;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Vehicle getDefaultVehicle() {
		return defaultVehicle;
	}
	public void setDefaultVehicle(Vehicle defaultVehicle) {
		this.defaultVehicle = defaultVehicle;
	}

}
