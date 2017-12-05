package com.digitusrevolution.rideshare.model.user.data;

import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table(name="user_preference")
public class PreferenceEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	//Unable to find how to create PK as user id, so adding a new id but logically it would be right to have user id as PK
	private int id;
	//Ride Request Preference
	@OneToOne
	private VehicleCategoryEntity vehicleCategory;
	@OneToOne
	private VehicleSubCategoryEntity vehicleSubCategory;
	private LocalTime pickupTimeVariation;
	private int pickupPointVariation;
	private int dropPointVariation;
	private int seatRequired;
	private int luggageCapacityRequired;
	
	//Ride Preference
	private int seatOffered;
	private int luggageCapacityOffered;
	@OneToOne
	private VehicleEntity defaultVehicle;

	//Common Preference
	@OneToOne
	private TrustCategoryEntity trustCategory;
	private Sex sexPreference;
	private float minProfileRating;
	@Column (name="rideMode")
	@Enumerated(EnumType.STRING)
	private RideMode rideMode;
	

	public VehicleCategoryEntity getVehicleCategory() {
		return vehicleCategory;
	}
	public void setVehicleCategory(VehicleCategoryEntity vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}
	public VehicleSubCategoryEntity getVehicleSubCategory() {
		return vehicleSubCategory;
	}
	public void setVehicleSubCategory(VehicleSubCategoryEntity vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
	}
	public TrustCategoryEntity getTrustCategory() {
		return trustCategory;
	}
	public void setTrustCategory(TrustCategoryEntity trustCategory) {
		this.trustCategory = trustCategory;
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
	public VehicleEntity getDefaultVehicle() {
		return defaultVehicle;
	}
	public void setDefaultVehicle(VehicleEntity defaultVehicle) {
		this.defaultVehicle = defaultVehicle;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
	}
}
