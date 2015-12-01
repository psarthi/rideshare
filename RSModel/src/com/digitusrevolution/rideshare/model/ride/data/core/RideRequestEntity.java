package com.digitusrevolution.rideshare.model.ride.data.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

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

import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
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
	@OneToOne
	private PointEntity pickupPoint;
	@OneToOne
	private PointEntity dropPoint;
	private Date dateTime;
	@ManyToOne
	private VehicleCategoryEntity vehicleCategory;
	@ManyToOne
	private VehicleSubCategoryEntity vehicleSubCategory;
	@OneToOne
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
	@OneToOne
	private PointEntity ridePickupPoint;
	@OneToOne
	private PointEntity rideDropPoint;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
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
	public PointEntity getPickupPoint() {
		return pickupPoint;
	}
	public void setPickupPoint(PointEntity pickupPoint) {
		this.pickupPoint = pickupPoint;
	}
	public PointEntity getDropPoint() {
		return dropPoint;
	}
	public void setDropPoint(PointEntity dropPoint) {
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
	public UserEntity getPassenger() {
		return passenger;
	}
	public void setPassenger(UserEntity passenger) {
		this.passenger = passenger;
	}	

}
