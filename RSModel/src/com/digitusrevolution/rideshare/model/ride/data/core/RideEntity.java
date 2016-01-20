package com.digitusrevolution.rideshare.model.ride.data.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

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
	//Reason for Many to One relationship, one vehicle can offer many rides
	@ManyToOne
	private VehicleEntity vehicle;
	//Reason for Many to one relationship, one user who is driver can offer any number of rides
	@ManyToOne
	private UserEntity driver;
	//Reason for Many to many relationship, as one ride can have many users who is passenger 
	//and one user can be part of many rides as different passenger
	@OneToMany(mappedBy="primaryKey.ride",cascade=CascadeType.ALL)
	private Collection<RidePassengerEntity> passengers = new HashSet<RidePassengerEntity>();
	@OneToMany(mappedBy="ride")
	private Collection<BillEntity> bills = new HashSet<BillEntity>();
	//Reason for one to many relationship, as one ride can accept many ride requests 
	//but one ride request can be accepted by only one ride
	@OneToMany(mappedBy="acceptedRide")
	private Collection<RideRequestEntity> acceptedRideRequests = new HashSet<RideRequestEntity>();
	//Reason for many to many relationship, as one ride can reject multiple ride request and 
	//one ride request can be rejected by multiple rides
	@ManyToMany
	@JoinTable(name="ride_rideRequest",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RideRequestEntity> rejectedRideRequests = new HashSet<RideRequestEntity>();
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
	public Collection<RidePassengerEntity> getPassengers() {
		return passengers;
	}
	public void setPassengers(Collection<RidePassengerEntity> passengers) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driver == null) ? 0 : driver.hashCode());
		result = prime * result + id;
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		result = prime * result + ((vehicle == null) ? 0 : vehicle.hashCode());
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
		if (!(obj instanceof RideEntity)) {
			return false;
		}
		RideEntity other = (RideEntity) obj;
		if (driver == null) {
			if (other.driver != null) {
				return false;
			}
		} else if (!driver.equals(other.driver)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (startTime == null) {
			if (other.startTime != null) {
				return false;
			}
		} else if (!startTime.equals(other.startTime)) {
			return false;
		}
		if (vehicle == null) {
			if (other.vehicle != null) {
				return false;
			}
		} else if (!vehicle.equals(other.vehicle)) {
			return false;
		}
		return true;
	}

}
