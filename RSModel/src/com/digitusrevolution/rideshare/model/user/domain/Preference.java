package com.digitusrevolution.rideshare.model.user.domain;

import java.time.LocalTime;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.data.PreferenceEntity;

public class Preference implements DomainModel{

	private PreferenceEntity entity = new PreferenceEntity();
	@SuppressWarnings("unused")
	private int id;
	//Ride Request Preference
	private VehicleCategory vehicleCategory = new VehicleCategory();
	private VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
	@SuppressWarnings("unused")
	private LocalTime pickupTimeVariation;
	@SuppressWarnings("unused")
	private int pickupPointVariation;
	@SuppressWarnings("unused")
	private int dropPointVariation;
	@SuppressWarnings("unused")
	private int seatRequired;
	@SuppressWarnings("unused")
	private int luggageCapacityRequired;
	
	//Ride Preference
	@SuppressWarnings("unused")
	private int seatOffered;
	@SuppressWarnings("unused")
	private int luggageCapacityOffered;

	//Common Preference
	private TrustNetwork trustNetwork = new TrustNetwork();
	@SuppressWarnings("unused")
	private Sex sexPreference;
	@SuppressWarnings("unused")
	private float minProfileRating;
	
	public VehicleCategory getVehicleCategory() {
		vehicleCategory.setEntity(entity.getVehicleCategory());
		return vehicleCategory;
	}
	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
		entity.setVehicleCategory(vehicleCategory.getEntity());
	}
	public VehicleSubCategory getVehicleSubCategory() {
		vehicleSubCategory.setEntity(entity.getVehicleSubCategory());
		return vehicleSubCategory;
	}
	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
		entity.setVehicleSubCategory(vehicleSubCategory.getEntity());
	}
	public LocalTime getPickupTimeVariation() {
		return entity.getPickupTimeVariation();
	}
	public void setPickupTimeVariation(LocalTime pickupTimeVariation) {
		this.pickupTimeVariation = pickupTimeVariation;
		entity.setPickupTimeVariation(pickupTimeVariation);
	}
	public int getPickupPointVariation() {
		return entity.getPickupPointVariation();
	}
	public void setPickupPointVariation(int pickupPointVariation) {
		this.pickupPointVariation = pickupPointVariation;
		entity.setPickupPointVariation(pickupPointVariation);
	}
	public int getDropPointVariation() {
		return entity.getDropPointVariation();
	}
	public void setDropPointVariation(int dropPointVariation) {
		this.dropPointVariation = dropPointVariation;
		entity.setDropPointVariation(dropPointVariation);
	}
	public int getSeatRequired() {
		return entity.getSeatRequired();
	}
	public void setSeatRequired(int seatRequired) {
		this.seatRequired = seatRequired;
		entity.setSeatRequired(seatRequired);
	}
	public int getLuggageCapacityRequired() {
		return entity.getLuggageCapacityRequired();
	}
	public void setLuggageCapacityRequired(int luggageCapacityRequired) {
		this.luggageCapacityRequired = luggageCapacityRequired;
		entity.setSeatRequired(luggageCapacityRequired);
	}
	public int getSeatOffered() {
		return entity.getSeatOffered();
	}
	public void setSeatOffered(int seatOffered) {
		this.seatOffered = seatOffered;
		entity.setSeatOffered(seatOffered);
	}
	public int getLuggageCapacityOffered() {
		return entity.getLuggageCapacityOffered();
	}
	public void setLuggageCapacityOffered(int luggageCapacityOffered) {
		this.luggageCapacityOffered = luggageCapacityOffered;
		entity.setLuggageCapacityOffered(luggageCapacityOffered);
	}
	public TrustNetwork getTrustNetwork() {
		trustNetwork.setEntity(entity.getTrustNetwork());
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		entity.setTrustNetwork(trustNetwork.getEntity());
	}
	public Sex getSexPreference() {
		return entity.getSexPreference();
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
		entity.setSexPreference(sexPreference);
	}
	public float getMinProfileRating() {
		return entity.getMinProfileRating();
	}
	public void setMinProfileRating(float minProfileRating) {
		this.minProfileRating = minProfileRating;
		entity.setMinProfileRating(minProfileRating);
	}
	public int getId() {
		return entity.getId();
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	@Override
	public void setUniqueInstanceVariable() {
		// TODO Auto-generated method stub		
	}
	public PreferenceEntity getEntity() {
		return entity;
	}
	public void setEntity(PreferenceEntity entity) {
		this.entity = entity;
	}

}
