package com.digitusrevolution.rideshare.model.ride.data;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;

@Embeddable
public class RouteEntity {
	
	@Embedded
	@ElementCollection
	@JoinTable(name="route_ridepoint",joinColumns=@JoinColumn(name="ride_id"))
	private Collection<RidePointEntity> ridePointEntities = new ArrayList<RidePointEntity>();

	public Collection<RidePointEntity> getRidePointEntities() {
		return ridePointEntities;
	}

	public void setRidePointEntities(Collection<RidePointEntity> ridePointEntities) {
		this.ridePointEntities = ridePointEntities;
	}

}
