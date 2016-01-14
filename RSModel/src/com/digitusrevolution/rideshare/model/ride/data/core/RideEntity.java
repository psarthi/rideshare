package com.digitusrevolution.rideshare.model.ride.data.core;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table(name="ride")
public class RideEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private ZonedDateTime startTime;
	//We need to store just ridePointId in Hibernate as ridepoint is getting stored in MonogoDB
	//Apart from that, we don't have Route details here as its stored in MongoDB, instead of Hibernate
	private String startPointId;
	private String endPointId;
	private int seatOffered;
	private int luggageCapacityOffered;
	@Enumerated(EnumType.STRING)
	private Sex sexPreference;
	@OneToOne(cascade=CascadeType.ALL)
	private TrustNetworkEntity trustNetwork;
	private boolean recur;
	@OneToOne(cascade=CascadeType.ALL)
	private RecurringDetailEntity recurringDetail;
	private String status;
	@ManyToOne
	private VehicleEntity vehicle;
	@ManyToOne
	private UserEntity driver;
	@ManyToMany
	@JoinTable(name="ride_passenger", joinColumns=@JoinColumn(name="ride_id"))
	private Collection<UserEntity> passengers = new ArrayList<UserEntity>();
	@OneToMany(mappedBy="ride")
	private Collection<BillEntity> bills = new ArrayList<BillEntity>();
	@OneToMany(mappedBy="acceptedRide")
	private Collection<RideRequestEntity> acceptedRideRequests = new ArrayList<RideRequestEntity>();
	@ManyToMany
	@JoinTable(name="ride_rideRequest",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RideRequestEntity> rejectedRideRequests = new ArrayList<RideRequestEntity>();
	private int travelDistance;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public ZonedDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(ZonedDateTime startTime) {
		this.startTime = startTime;
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
	public boolean getRecur() {
		return recur;
	}
	public void setRecur(boolean recur) {
		this.recur = recur;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getStartPointId() {
		return startPointId;
	}
	public void setStartPointId(String startPointId) {
		this.startPointId = startPointId;
	}
	public String getEndPointId() {
		return endPointId;
	}
	public void setEndPointId(String endPointId) {
		this.endPointId = endPointId;
	}
	public TrustNetworkEntity getTrustNetwork() {
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetworkEntity trustNetwork) {
		this.trustNetwork = trustNetwork;
	}
	public RecurringDetailEntity getRecurringDetail() {
		return recurringDetail;
	}
	public void setRecurringDetail(RecurringDetailEntity recurringDetail) {
		this.recurringDetail = recurringDetail;
	}
	public VehicleEntity getVehicle() {
		return vehicle;
	}
	public void setVehicle(VehicleEntity vehicle) {
		this.vehicle = vehicle;
	}
	public Collection<BillEntity> getBills() {
		return bills;
	}
	public void setBills(Collection<BillEntity> bills) {
		this.bills = bills;
	}
	public Collection<RideRequestEntity> getAcceptedRideRequests() {
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<RideRequestEntity> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
	}
	public Collection<RideRequestEntity> getRejectedRideRequests() {
		return rejectedRideRequests;
	}
	public void setRejectedRideRequests(Collection<RideRequestEntity> rejectedRideRequests) {
		this.rejectedRideRequests = rejectedRideRequests;
	}
	public UserEntity getDriver() {
		return driver;
	}
	public void setDriver(UserEntity driver) {
		this.driver = driver;
	}
	public Collection<UserEntity> getPassengers() {
		return passengers;
	}
	public void setPassengers(Collection<UserEntity> passengers) {
		this.passengers = passengers;
	}
	public Sex getSexPreference() {
		return sexPreference;
	}
	public void setSexPreference(Sex sexPreference) {
		this.sexPreference = sexPreference;
	}
	public int getTravelDistance() {
		return travelDistance;
	}
	public void setTravelDistance(int travelDistance) {
		this.travelDistance = travelDistance;
	}

}
