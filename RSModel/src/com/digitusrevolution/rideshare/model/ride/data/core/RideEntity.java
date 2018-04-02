package com.digitusrevolution.rideshare.model.ride.data.core;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.InvoiceEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Invoice;
import com.digitusrevolution.rideshare.model.ride.data.RecurringDetailEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Sex;

@Entity
@Table(name="ride")
public class RideEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private ZonedDateTime startTime;
	private ZonedDateTime endTime;
	//We need to store just ridePointId in Hibernate as ridepoint is getting stored in MonogoDB
	//Apart from that, we don't have Route details here as its stored in MongoDB, instead of Hibernate
	private String startPointId;
	private String endPointId;
	private String startPointAddress;
	private String endPointAddress;
	private int seatOffered;
	private int luggageCapacityOffered;
	@Enumerated(EnumType.STRING)
	private Sex sexPreference;
	@OneToOne
	private TrustNetworkEntity trustNetwork;
	private boolean recur;
	@OneToOne(cascade=CascadeType.ALL)
	private RecurringDetailEntity recurringDetail;
	@Column (name="status")
	@Enumerated(EnumType.STRING)
	private RideStatus status;
	@Column (name="seatStatus")
	@Enumerated(EnumType.STRING)
	private RideSeatStatus seatStatus;
	//Reason for Many to One relationship, one vehicle can offer many rides
	@ManyToOne
	private VehicleEntity vehicle;
	//Reason for Many to one relationship, one user who is driver can offer any number of rides
	@ManyToOne
	private UserEntity driver;
	//Reason for Many to many relationship, as one ride can have many users who is passenger 
	//and one user can be part of many rides as different passenger
	//We have broken Many to Many relationship to OneToMany so that we can have mapping of ride and users
	//we could have used ride request to find the passenger details but originally this option was used to store passenger
	//instead of storing in ride request, so need to think through properly if this has to be removed
	//Apart from that, it would also help you to get the list of passenger without fetching the ride request and 
	//then fetching passenger, this can also be handy data which is relevant many times, so just keep it as it is 
	@OneToMany(mappedBy="ride", cascade=CascadeType.ALL)
	private Collection<RidePassengerEntity> ridePassengers = new HashSet<RidePassengerEntity>();
	//Reason for one to many relationship, as one ride can accept many ride requests 
	//but one ride request can be accepted by only one ride
	@OneToMany(mappedBy="acceptedRide")
	private Collection<RideRequestEntity> acceptedRideRequests = new HashSet<RideRequestEntity>();
	@ManyToMany
	@JoinTable(name="ride_cancelledRideRequest",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RideRequestEntity> cancelledRideRequests = new HashSet<RideRequestEntity>();
	private int travelDistance;
	@Column (name="rideMode")
	@Enumerated(EnumType.STRING)
	private RideMode rideMode;
	@OneToOne(cascade=CascadeType.MERGE)
	private InvoiceEntity invoice;
	

	public RideMode getRideMode() {
		return rideMode;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
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
	public RideStatus getStatus() {
		return status;
	}
	public void setStatus(RideStatus status) {
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
	public Collection<RideRequestEntity> getAcceptedRideRequests() {
		return acceptedRideRequests;
	}
	public void setAcceptedRideRequests(Collection<RideRequestEntity> acceptedRideRequests) {
		this.acceptedRideRequests = acceptedRideRequests;
	}
	public UserEntity getDriver() {
		return driver;
	}
	public void setDriver(UserEntity driver) {
		this.driver = driver;
	}
	public Collection<RidePassengerEntity> getRidePassengers() {
		return ridePassengers;
	}
	public void setRidePassengers(Collection<RidePassengerEntity> ridePassengers) {
		this.ridePassengers = ridePassengers;
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
		if (!(obj instanceof RideEntity)) {
			return false;
		}
		RideEntity other = (RideEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public Collection<RideRequestEntity> getCancelledRideRequests() {
		return cancelledRideRequests;
	}
	public void setCancelledRideRequests(Collection<RideRequestEntity> cancelledRideRequests) {
		this.cancelledRideRequests = cancelledRideRequests;
	}
	public RideSeatStatus getSeatStatus() {
		return seatStatus;
	}
	public void setSeatStatus(RideSeatStatus seatStatus) {
		this.seatStatus = seatStatus;
	}
	public ZonedDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(ZonedDateTime endTime) {
		this.endTime = endTime;
	}
	public String getStartPointAddress() {
		return startPointAddress;
	}
	public void setStartPointAddress(String startPointAddress) {
		this.startPointAddress = startPointAddress;
	}
	public String getEndPointAddress() {
		return endPointAddress;
	}
	public void setEndPointAddress(String endPointAddress) {
		this.endPointAddress = endPointAddress;
	}
	public InvoiceEntity getInvoice() {
		return invoice;
	}
	public void setInvoice(InvoiceEntity invoice) {
		this.invoice = invoice;
	}
	

}
