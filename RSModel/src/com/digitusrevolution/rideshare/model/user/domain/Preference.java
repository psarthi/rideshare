package com.digitusrevolution.rideshare.model.user.domain;

import java.time.LocalTime;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.data.PreferenceEntity;

public class Preference implements DomainModel{

	private PreferenceEntity entity = new PreferenceEntity();
	private int id;
	//Ride Request Preference
	private VehicleCategory vehicleCategory = new VehicleCategory();
	private VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
	private LocalTime pickupTimeVariation;
	private int pickupPointVariation;
	private int dropPointVariation;
	private int seatRequired;
	private int luggageCapacityRequired;
	
	//Ride Preference
	private int seatOffered;
	private int luggageCapacityOffered;

	//Common Preference
	private TrustNetwork trustNetwork = new TrustNetwork();
	private Sex sexPreference;
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
		return pickupTimeVariation;
	}
	public void setPickupTimeVariation(LocalTime pickupTimeVariation) {
		this.pickupTimeVariation = pickupTimeVariation;
		entity.setPickupTimeVariation(pickupTimeVariation);
	}
	public int getPickupPointVariation() {
		return pickupPointVariation;
	}
	public void setPickupPointVariation(int pickupPointVariation) {
		this.pickupPointVariation = pickupPointVariation;
		entity.setPickupPointVariation(pickupPointVariation);
	}
	public int getDropPointVariation() {
		return dropPointVariation;
	}
	public void setDropPointVariation(int dropPointVariation) {
		this.dropPointVariation = dropPointVariation;
		entity.setDropPointVariation(dropPointVariation);
	}
	public int getSeatRequired() {
		return seatRequired;
	}
	public void setSeatRequired(int seatRequired) {
		this.seatRequired = seatRequired;
		entity.setSeatRequired(seatRequired);
	}
	public int getLuggageCapacityRequired() {
		return luggageCapacityRequired;
	}
	public void setLuggageCapacityRequired(int luggageCapacityRequired) {
		this.luggageCapacityRequired = luggageCapacityRequired;
		entity.setSeatRequired(luggageCapacityRequired);
	}
	public int getSeatOffered() {
		return seatOffered;
	}
	public void setSeatOffered(int seatOffered) {
		this.seatOffered = seatOffered;
		entity.setSeatOffered(seatOffered);
	}
	public int getLuggageCapacityOffered() {
		return luggageCapacityOffered;
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
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
		entity.setSexPreference(sexPreference);
	}
	public float getMinProfileRating() {
		return minProfileRating;
	}
	public void setMinProfileRating(float minProfileRating) {
		this.minProfileRating = minProfileRating;
		entity.setMinProfileRating(minProfileRating);
	}
	public int getId() {
		return id; 
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public PreferenceEntity getEntity() {
		return entity;
	}
	public void setEntity(PreferenceEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		dropPointVariation = entity.getDropPointVariation();
		id = entity.getId();
		luggageCapacityOffered = entity.getLuggageCapacityOffered();
		luggageCapacityOffered = entity.getLuggageCapacityRequired();
		minProfileRating = entity.getMinProfileRating();
		pickupPointVariation = entity.getPickupPointVariation();
		pickupTimeVariation = entity.getPickupTimeVariation();
		seatOffered = entity.getSeatOffered();		
		seatRequired = entity.getSeatRequired();
		sexPreference = entity.getSexPreference();
	}
	@Override
	public void fetchReferenceVariable() {
		// TODO Auto-generated method stub
		
	}

}
