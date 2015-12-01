package com.digitusrevolution.rideshare.ride.domain.core;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;

public class RideDO implements DomainObject{
	
	private Ride ride;
	private RideEntity rideEntity;
	private RideMapper rideMapper;
	
	public RideDO() {
		ride = new Ride();
		rideEntity = new RideEntity();
		rideMapper = new RideMapper();
	}
	
	public Ride getRide() {
		return ride;
	}

	public void setRide(Ride ride) {
		this.ride = ride;
		rideEntity = rideMapper.getEntity(ride);
	}

	public RideEntity getRideEntity() {
		return rideEntity;
	}

	public void setRideEntity(RideEntity rideEntity) {
		this.rideEntity = rideEntity;
		ride = rideMapper.getDomainModel(rideEntity);
	}

	@Override
	public void fetchChild() {
		ride = rideMapper.getDomainModelChild(ride, rideEntity);
	}

}
