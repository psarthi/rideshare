package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;

@Entity
@Table(name="passenger")
public class PassengerEntity extends UserEntity{
	@OneToMany
	private List<RideRequestEntity> rideRequests;
	@OneToMany
	private List<BillEntity> bills;
	@ManyToMany(mappedBy="passengers")
	private List<RideEntity> rides;
	@OneToOne
	private PointEntity pickupPoint;
	@OneToOne
	private PointEntity dropPoint;
	public List<RideRequestEntity> getRideRequests() {
		return rideRequests;
	}
	public void setRideRequests(List<RideRequestEntity> rideRequests) {
		this.rideRequests = rideRequests;
	}
	public List<BillEntity> getBills() {
		return bills;
	}
	public void setBills(List<BillEntity> bills) {
		this.bills = bills;
	}
	public List<RideEntity> getRides() {
		return rides;
	}
	public void setRides(List<RideEntity> rides) {
		this.rides = rides;
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
	
}
