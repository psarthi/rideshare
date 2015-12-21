package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class RideMapper implements Mapper<Ride, RideEntity>{

	@Override
	public RideEntity getEntityWithOnlyPK(Ride ride){
		RideEntity rideEntity = new RideEntity();
		rideEntity.setId(ride.getId());
		return rideEntity;
	}
	
	@Override
	public RideEntity getEntity(Ride ride) {
		RideEntity rideEntity = new RideEntity();
		rideEntity = getEntityWithOnlyPK(ride);
		rideEntity.setStartTime(ride.getStartTime());
		rideEntity.setSeatOffered(ride.getSeatOffered());
		rideEntity.setLuggageCapacityOffered(ride.getLuggageCapacityOffered());
		rideEntity.setRecur(ride.getRecur());
		rideEntity.setStatus(ride.getStatus());
		rideEntity.setSexPreference(ride.getSexPreference());
		//We need to just map Point ID in Hibernate as we are storing Point in MongoDB
		rideEntity.setStartPointId(ride.getStartPoint().get_id());
		rideEntity.setEndPointId(ride.getEndPoint().get_id());
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = ride.getTrustNetwork();
		rideEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork));

		VehicleMapper vehicleMapper = new VehicleMapper();
		Vehicle vehicle = ride.getVehicle();
		rideEntity.setVehicle(vehicleMapper.getEntityWithOnlyPK(vehicle));
		
		UserMapper userMapper = new UserMapper();
		User user = ride.getDriver();
		rideEntity.setDriver(userMapper.getEntityWithOnlyPK(user));
		
		rideEntity = getEntityChild(ride, rideEntity);
		
		return rideEntity;
	}

	@Override
	public RideEntity getEntityChild(Ride ride, RideEntity rideEntity) {
		
		UserMapper userMapper = new UserMapper();
		Collection<User> users = ride.getPassengers();
		rideEntity.setPassengers(userMapper.getEntitiesWithOnlyPK(users));
		
		return rideEntity;
	}

	@Override
	public Ride getDomainModelWithOnlyPK(RideEntity rideEntity) {
		Ride ride = new Ride();
		ride.setId(rideEntity.getId());	
		return ride;
	}
	
	@Override
	public Ride getDomainModel(RideEntity rideEntity) {
		Ride ride = new Ride();
		ride = getDomainModelWithOnlyPK(rideEntity);
		ride.setStartTime(rideEntity.getStartTime());
		ride.setSeatOffered(rideEntity.getSeatOffered());
		ride.setLuggageCapacityOffered(rideEntity.getLuggageCapacityOffered());
		ride.setRecur(rideEntity.getRecur());
		ride.setStatus(rideEntity.getStatus());
		ride.setSexPreference(rideEntity.getSexPreference());
		
		//We need to just map Point ID from Hibernate as we are storing Point in MongoDB
		ride.getStartPoint().set_id(rideEntity.getStartPointId());
		ride.getEndPoint().set_id(rideEntity.getEndPointId());
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideEntity.getTrustNetwork();
		TrustNetwork trustNetwork = trustNetworkMapper.getDomainModel(trustNetworkEntity);
		trustNetwork = trustNetworkMapper.getDomainModelChild(trustNetwork, trustNetworkEntity);
		ride.setTrustNetwork(trustNetwork);

		VehicleMapper vehicleMapper = new VehicleMapper();
		VehicleEntity vehicleEntity = rideEntity.getVehicle();
		ride.setVehicle(vehicleMapper.getDomainModelWithOnlyPK(vehicleEntity));
		
		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideEntity.getDriver();
		ride.setDriver(userMapper.getDomainModelWithOnlyPK(userEntity));
		
		return ride;
	}


	@Override
	public Ride getDomainModelChild(Ride ride, RideEntity rideEntity) {
		
		UserMapper userMapper = new UserMapper();
		Collection<UserEntity> userEntities = rideEntity.getPassengers();
		ride.setPassengers(userMapper.getDomainModelsWithOnlyPK(userEntities));
		
		return ride;
	}

	@Override
	public Collection<Ride> getDomainModelsWithOnlyPK(Collection<RideEntity> rideEntities) {
		Collection<Ride> rides = new LinkedList<>();
		Ride ride = new Ride();
		for (RideEntity rideEntity : rideEntities) {
			ride = getDomainModelWithOnlyPK(rideEntity);
			rides.add(ride);
		}
		return rides;		

	}
	
	@Override
	public Collection<Ride> getDomainModels(Collection<RideEntity> rideEntities) {
		Collection<Ride> rides = new LinkedList<>();
		Ride ride = new Ride();
		for (RideEntity rideEntity : rideEntities) {
			ride = getDomainModel(rideEntity);
			ride = getDomainModelChild(ride, rideEntity);
			rides.add(ride);
		}
		return rides;		
	}

	@Override
	public Collection<RideEntity> getEntitiesWithOnlyPK(Collection<Ride> rides) {
		Collection<RideEntity> rideEntities = new LinkedList<>();
		for (Ride ride : rides) {
			rideEntities.add(getEntityWithOnlyPK(ride));
		}
		return rideEntities;		
	}
	
	@Override
	public Collection<RideEntity> getEntities(Collection<Ride> rides) {
		Collection<RideEntity> rideEntities = new LinkedList<>();
		for (Ride ride : rides) {
			rideEntities.add(getEntity(ride));
		}
		return rideEntities;		
	}

}
