package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;

@Entity
@Table(name="passenger")
public class PassengerEntity extends UserEntity{
	@OneToMany(mappedBy="passenger")
	private Collection<RideRequestEntity> rideRequests = new ArrayList<RideRequestEntity>();
	@OneToMany(mappedBy="passenger")
	private Collection<BillEntity> bills = new ArrayList<BillEntity>();
	@ManyToMany(mappedBy="passengers")
	private Collection<RideEntity> rides = new ArrayList<RideEntity>();
	
	public Collection<RideRequestEntity> getRideRequests() {
		return rideRequests;
	}
	public void setRideRequests(Collection<RideRequestEntity> rideRequests) {
		this.rideRequests = rideRequests;
	}
	public Collection<BillEntity> getBills() {
		return bills;
	}
	public void setBills(Collection<BillEntity> bills) {
		this.bills = bills;
	}
	public Collection<RideEntity> getRides() {
		return rides;
	}
	public void setRides(Collection<RideEntity> rides) {
		this.rides = rides;
	}

	
}
