package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;

@Entity
@Table(name="driver")
public class DriverEntity extends UserEntity{
	@OneToMany(mappedBy="driver")
	private List<RideEntity> rides;

	public List<RideEntity> getRides() {
		return rides;
	}

	public void setRides(List<RideEntity> rides) {
		this.rides = rides;
	}



}
