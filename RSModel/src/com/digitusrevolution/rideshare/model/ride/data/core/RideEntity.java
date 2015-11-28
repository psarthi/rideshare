package com.digitusrevolution.rideshare.model.ride.data.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.Embedded;
import javax.persistence.Entity;
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
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;
import com.digitusrevolution.rideshare.model.ride.data.RouteEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.user.data.core.DriverEntity;
import com.digitusrevolution.rideshare.model.user.data.core.PassengerEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;

@Entity
@Table(name="ride")
public class RideEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private Date dateTime;
	@OneToOne
	private PointEntity startPoint;
	@OneToOne
	private PointEntity endPoint;
	private int seatOffered;
	private int luggageCapacityOffered;
	@OneToOne
	private TrustNetworkEntity trustNetwork;
	@Embedded
	private RouteEntity route;
	private boolean recur;
	@OneToOne
	private RecurringDetailEntity recurringDetail;
	private String status;
	@ManyToOne
	private VehicleEntity vehicle;
	@ManyToOne
	private DriverEntity driver;
	@ManyToMany
	private Collection<PassengerEntity> passengers = new ArrayList<PassengerEntity>();
	@OneToMany(mappedBy="ride")
	private Collection<BillEntity> bills = new ArrayList<BillEntity>();
	@OneToMany(mappedBy="acceptedRide")
	private Collection<RideRequestEntity> acceptedRideRequests = new ArrayList<RideRequestEntity>();
	@ManyToMany
	@JoinTable(name="ride_rideRequest",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RideRequestEntity> rejectedRideRequests = new ArrayList<RideRequestEntity>();
	
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
	public boolean isRecur() {
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
	public PointEntity getStartPoint() {
		return startPoint;
	}
	public void setStartPoint(PointEntity startPoint) {
		this.startPoint = startPoint;
	}
	public PointEntity getEndPoint() {
		return endPoint;
	}
	public void setEndPoint(PointEntity endPoint) {
		this.endPoint = endPoint;
	}
	public TrustNetworkEntity getTrustNetwork() {
		return trustNetwork;
	}
	public void setTrustNetwork(TrustNetworkEntity trustNetwork) {
		this.trustNetwork = trustNetwork;
	}
	public RouteEntity getRoute() {
		return route;
	}
	public void setRoute(RouteEntity route) {
		this.route = route;
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
	public DriverEntity getDriver() {
		return driver;
	}
	public void setDriver(DriverEntity driver) {
		this.driver = driver;
	}
	public Collection<PassengerEntity> getPassengers() {
		return passengers;
	}
	public void setPassengers(Collection<PassengerEntity> passengers) {
		this.passengers = passengers;
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

}
