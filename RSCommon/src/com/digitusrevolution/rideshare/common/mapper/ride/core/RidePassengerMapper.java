package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;

/*
 * This is a custom mapper class which doesn't implement Mapper interface,
 * and its used only by Ride Mapper 
 * 
 */
public class RidePassengerMapper{

	public RidePassengerEntity getEntity(RidePassenger ridePassenger, Ride ride, boolean fetchChild) {
		RidePassengerEntity ridePassengerEntity = new RidePassengerEntity();
		UserMapper userMapper = new UserMapper();
		ridePassengerEntity.setPassenger(userMapper.getEntity(ridePassenger.getPassenger(), fetchChild));
		RideMapper rideMapper = new RideMapper();
		//Reason for getting ride instead of rideEntity, as rideEntity contains passenger and passenger contain ride entity and so on..
		//i.e. recursive loop. So to avoid that we get Ride and then get rideEntity by not fetching child, so we get ride entity without passenger
		ridePassengerEntity.setRide(rideMapper.getEntity(ride, fetchChild));
		ridePassengerEntity.setStatus(ridePassenger.getStatus());		
		return ridePassengerEntity;
	}

	public RidePassenger getDomainModel(RidePassengerEntity ridePassengerEntity, boolean fetchChild) {
		RidePassenger ridePassenger = new RidePassenger();
		UserMapper userMapper = new UserMapper();
		ridePassenger.setPassenger(userMapper.getDomainModel(ridePassengerEntity.getPassenger(), fetchChild));
		ridePassenger.setStatus(ridePassengerEntity.getStatus());		
		return ridePassenger;
	}

	public Collection<RidePassenger> getDomainModels(Collection<RidePassenger> ridePassengers,
			Collection<RidePassengerEntity> ridePassengerEntities, boolean fetchChild) {
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			RidePassenger ridePassenger = new RidePassenger();
			ridePassenger = getDomainModel(ridePassengerEntity, fetchChild);
			ridePassengers.add(ridePassenger);
		}
		return ridePassengers;
	}

	public Collection<RidePassengerEntity> getEntities(Collection<RidePassengerEntity> ridePassengerEntities,
			Collection<RidePassenger> ridePassengers, Ride ride, boolean fetchChild) {
		for (RidePassenger ridePassenger : ridePassengers) {
			RidePassengerEntity ridePassengerEntity = new RidePassengerEntity();
			ridePassengerEntity = getEntity(ridePassenger, ride, fetchChild);
			ridePassengerEntities.add(ridePassengerEntity);
		}
		return ridePassengerEntities;
	}

}
