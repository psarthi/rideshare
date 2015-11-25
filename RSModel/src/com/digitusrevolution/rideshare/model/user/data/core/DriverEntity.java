package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;

@Entity
@Table(name="driver")
public class DriverEntity extends UserEntity{
	@OneToMany(mappedBy="driver")
	private Collection<RideEntity> rides = new ArrayList<RideEntity>();

	public Collection<RideEntity> getRides() {
		return rides;
	}
	public void setRides(Collection<RideEntity> rides) {
		this.rides = rides;
	}

}
