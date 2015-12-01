package com.digitusrevolution.rideshare.ride.domain.core;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

public class RideRequestDO implements DomainObject{
	
	private RideRequest rideRequest;
	private RideRequestEntity rideRequestEntity;
	private RideRequestMapper rideRequestMapper;
	
	public RideRequestDO() {
		rideRequest = new RideRequest();
		rideRequestEntity = new RideRequestEntity();
		rideRequestMapper = new RideRequestMapper();
	}

	public RideRequest getRideRequest() {
		return rideRequest;
	}

	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
		rideRequestEntity = rideRequestMapper.getEntity(rideRequest);
	}

	public RideRequestEntity getRideRequestEntity() {
		return rideRequestEntity;
	}

	public void setRideRequestEntity(RideRequestEntity rideRequestEntity) {
		this.rideRequestEntity = rideRequestEntity;
		rideRequest = rideRequestMapper.getDomainModel(rideRequestEntity);
	}

	public RideRequestMapper getRideRequestMapper() {
		return rideRequestMapper;
	}

	public void setRideRequestMapper(RideRequestMapper rideRequestMapper) {
		this.rideRequestMapper = rideRequestMapper;
	}

	@Override
	public void fetchChild() {
		rideRequest = rideRequestMapper.getDomainModelChild(rideRequest, rideRequestEntity);
		
	}
	

}
