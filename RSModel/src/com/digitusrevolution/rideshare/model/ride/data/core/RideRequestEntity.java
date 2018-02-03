package com.digitusrevolution.rideshare.model.ride.data.core;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.user.data.UserFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;

@Entity
@Table(name="rideRequest")
public class RideRequestEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	//We need to store just rideRequestPointId in Hibernate as rideRequestPoint is getting stored in MonogoDB
	private String pickupPointId;
	private String dropPointId;
	private String pickupPointAddress;
	private String dropPointAddress;
	private ZonedDateTime pickupTime;
	private LocalTime pickupTimeVariation;
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
	@Column (name="status")
	@Enumerated(EnumType.STRING)
	private RideRequestStatus status;
	@ManyToOne
	private UserEntity passenger;
	@Column (name="passengerStatus")
	@Enumerated(EnumType.STRING)
	private PassengerStatus passengerStatus;
	public PassengerStatus getPassengerStatus() {
		return passengerStatus;
	}
	public void setPassengerStatus(PassengerStatus passengerStatus) {
		this.passengerStatus = passengerStatus;
	}
	@ManyToOne
	private RideEntity acceptedRide;
	//We need to store just ridePointId in Hibernate as ridepoint is getting stored in MonogoDB
	private String ridePickupPointId;
	private String rideDropPointId;
	private String ridePickupPointAddress;
	private String rideDropPointAddress;
	private double ridePickupPointDistance;
	private double rideDropPointDistance;
	private int travelTime;
	private int travelDistance;
	@ManyToMany (mappedBy="cancelledRideRequests")
	private Collection<RideEntity> cancelledRides = new HashSet<RideEntity>();
	@Column (name="rideMode")
	@Enumerated(EnumType.STRING)
	private RideMode rideMode;
	@OneToOne (cascade=CascadeType.ALL)
	private BillEntity bill;
	private String confirmationCode;
	@OneToMany (mappedBy="rideRequest", cascade=CascadeType.PERSIST)
	private Collection<UserFeedbackEntity> feedbacks = new HashSet<UserFeedbackEntity>();
	
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
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
	public RideEntity getAcceptedRide() {
		return acceptedRide;
	}
	public void setAcceptedRide(RideEntity acceptedRide) {
		this.acceptedRide = acceptedRide;
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
	public UserEntity getPassenger() {
		return passenger;
	}
	public void setPassenger(UserEntity passenger) {
		this.passenger = passenger;
	}
	public String getPickupPointId() {
		return pickupPointId;
	}
	public void setPickupPointId(String pickupPointId) {
		this.pickupPointId = pickupPointId;
	}
	public String getDropPointId() {
		return dropPointId;
	}
	public void setDropPointId(String dropPointId) {
		this.dropPointId = dropPointId;
	}
	public String getRidePickupPointId() {
		return ridePickupPointId;
	}
	public void setRidePickupPointId(String ridePickupPointId) {
		this.ridePickupPointId = ridePickupPointId;
	}
	public String getRideDropPointId() {
		return rideDropPointId;
	}
	public void setRideDropPointId(String rideDropPointId) {
		this.rideDropPointId = rideDropPointId;
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
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof RideRequestEntity)) {
			return false;
		}
		RideRequestEntity other = (RideRequestEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public Collection<RideEntity> getCancelledRides() {
		return cancelledRides;
	}
	public void setCancelledRides(Collection<RideEntity> cancelledRides) {
		this.cancelledRides = cancelledRides;
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
	public BillEntity getBill() {
		return bill;
	}
	public void setBill(BillEntity bill) {
		this.bill = bill;
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
	public String getConfirmationCode() {
		return confirmationCode;
	}
	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}
	public Collection<UserFeedbackEntity> getFeedbacks() {
		return feedbacks;
	}
	public void setFeedbacks(Collection<UserFeedbackEntity> feedbacks) {
		this.feedbacks = feedbacks;
	}


}
