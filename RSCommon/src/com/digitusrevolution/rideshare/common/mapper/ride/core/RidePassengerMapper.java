package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;

public class RidePassengerMapper implements Mapper<RidePassenger, RidePassengerEntity>{


	@Override
	public RidePassengerEntity getEntity(RidePassenger ridePassenger, boolean fetchChild) {
		RidePassengerEntity ridePassengerEntity = new RidePassengerEntity();
		ridePassengerEntity.setId(ridePassenger.getId());
		ridePassengerEntity.setStatus(ridePassenger.getStatus());
	
		//Its important not to fetch child as this will get into recursive loop
		//as ride has ride passenger and ride passenger has ride
		//Don't move this into child, otherwise we won't be able to set ride
		//as caller would not fetch child
		RideMapper rideMapper = new RideMapper();
		ridePassengerEntity.setRide(rideMapper.getEntity(ridePassenger.getRide(), false));

		//Its important not to fetch child as this will get into recursive loop
		//as user has ride passenger and ride passenger has user
		//Don't move this into child, otherwise we won't be able to set user
		//as caller would not fetch child
		UserMapper userMapper = new UserMapper();
		ridePassengerEntity.setPassenger(userMapper.getEntity(ridePassenger.getPassenger(), false));

		return ridePassengerEntity;
	}

	@Override
	public RidePassengerEntity getEntityChild(RidePassenger ridePassenger, RidePassengerEntity ridePassengerEntity) {
		return ridePassengerEntity;
	}

	@Override
	public RidePassenger getDomainModel(RidePassengerEntity ridePassengerEntity, boolean fetchChild) {
		RidePassenger ridePassenger = new RidePassenger();
		ridePassenger.setId(ridePassengerEntity.getId());
		ridePassenger.setStatus(ridePassengerEntity.getStatus());	
		
		//Its important not to fetch child as this will get into recursive loop
		//as ride has ride passenger and ride passenger has ride
		//Don't move this into child, otherwise we won't be able to set ride
		//as caller would not fetch child
		RideMapper rideMapper = new RideMapper();
		ridePassenger.setRide(rideMapper.getDomainModel(ridePassengerEntity.getRide(), false));
		
		//Its important not to fetch child as this will get into recursive loop
		//as user has ride passenger and ride passenger has user
		//Don't move this into child, otherwise we won't be able to set user
		//as caller would not fetch child
		UserMapper userMapper = new UserMapper();
		ridePassenger.setPassenger(userMapper.getDomainModel(ridePassengerEntity.getPassenger(), false));

		return ridePassenger;
	}


	@Override
	public RidePassenger getDomainModelChild(RidePassenger ridePassenger, RidePassengerEntity ridePassengerEntity) {

		return ridePassenger;
	}

	@Override
	public Collection<RidePassengerEntity> getEntities(Collection<RidePassengerEntity> ridePassengerEntities,
			Collection<RidePassenger> ridePassengers, boolean fetchChild) {
		for (RidePassenger ridePassenger : ridePassengers) {
			RidePassengerEntity ridePassengerEntity = new RidePassengerEntity();
			ridePassengerEntity = getEntity(ridePassenger, fetchChild);
			ridePassengerEntities.add(ridePassengerEntity);
		}
		return ridePassengerEntities;
	}

	@Override
	public Collection<RidePassenger> getDomainModels(Collection<RidePassenger> ridePassengers,
			Collection<RidePassengerEntity> ridePassengerEntities, boolean fetchChild) {
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			RidePassenger ridePassenger = new RidePassenger();
			ridePassenger = getDomainModel(ridePassengerEntity, fetchChild);
			ridePassengers.add(ridePassenger);
		}
		return ridePassengers;
	}

}























