package com.digitusrevolution.rideshare.model.ride.data.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.RidePointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RideRequestPointEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table(name="ride_request")
public class RideRequestEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@OneToOne(cascade=CascadeType.ALL)
	private RideRequestPointEntity pickupPoint;
	@OneToOne(cascade=CascadeType.ALL)
	private RideRequestPointEntity dropPoint;
	private ZonedDateTime pickupTime;
	private LocalTime pickupTimeVariation;
	@ManyToOne
	private VehicleCategoryEntity vehicleCategory;
	@ManyToOne
	private VehicleSubCategoryEntity vehicleSubCategory;
	@OneToOne(cascade=CascadeType.ALL)
	private TrustNetworkEntity trustNetwork;
	@Enumerated(EnumType.STRING)
	private Sex sexPreference;
	private int seatRequired;
	private int luggageCapacityRequired;
	private int pickupPointVariation;
	private int dropPointVariation;
	private String status;
	@ManyToOne
	private UserEntity passenger;
	private boolean ridePreference;
	@ManyToMany
	@JoinTable(name="ride_request_preferred_ride",joinColumns=@JoinColumn(name="ride_request_id"))
	private Collection<RideEntity> preferredRides = new ArrayList<RideEntity>();
	@ManyToOne
	private RideEntity acceptedRide;
	@OneToOne(cascade=CascadeType.ALL)
	private RidePointEntity ridePickupPoint;
	@OneToOne(cascade=CascadeType.ALL)
	private RidePointEntity rideDropPoint;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Collection<RideEntity> getPreferredRides() {
		return preferredRides;
	}
	public void setPreferredRides(Collection<RideEntity> preferredRides) {
		this.preferredRides = preferredRides;
	}
	public boolean getRidePreference() {
		return ridePreference;
	}
	public void setRidePreference(boolean ridePreference) {
		this.ridePreference = ridePreference;
	}
	public RideEntity getAcceptedRide() {
		return acceptedRide;
	}
	public void setAcceptedRide(RideEntity acceptedRide) {
		this.acceptedRide = acceptedRide;
	}
	public RideRequestPointEntity getPickupPoint() {
		return pickupPoint;
	}
	public void setPickupPoint(RideRequestPointEntity pickupPoint) {
		this.pickupPoint = pickupPoint;
	}
	public RideRequestPointEntity getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(RideRequestPointEntity dropPoint) {
		this.dropPoint = dropPoint;
	}
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
	public TrustNetworkEntity getTrustNetwork() {
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetworkEntity trustNetwork) {
		this.trustNetwork = trustNetwork;
	}
	public RidePointEntity getRidePickupPoint() {
		return ridePickupPoint;
	}
	public void setRidePickupPoint(RidePointEntity ridePickupPoint) {
		this.ridePickupPoint = ridePickupPoint;
	}
	public RidePointEntity getRideDropPoint() {
		return rideDropPoint;
	}
	public void setRideDropPoint(RidePointEntity rideDropPoint) {
		this.rideDropPoint = rideDropPoint;
	}
	public UserEntity getPassenger() {
		return passenger;
	}
	public void setPassenger(UserEntity passenger) {
		this.passenger = passenger;
	}	

}
