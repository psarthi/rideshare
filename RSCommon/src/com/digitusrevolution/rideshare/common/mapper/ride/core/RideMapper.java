package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
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
		//We need to just map Point ID in Hibernate as we are storing Point in MongoDB
		rideEntity.setStartPointId(ride.getStartPoint().get_id());
		rideEntity.setEndPointId(ride.getEndPoint().get_id());

		rideEntity.setSeatOffered(ride.getSeatOffered());
		rideEntity.setLuggageCapacityOffered(ride.getLuggageCapacityOffered());
		rideEntity.setRecur(ride.getRecur());
		rideEntity.setStatus(ride.getStatus());
		rideEntity.setSexPreference(ride.getSexPreference());
		rideEntity.setTravelDistance(ride.getTravelDistance());

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = ride.getTrustNetwork();
		rideEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork));

		VehicleMapper vehicleMapper = new VehicleMapper();
		Vehicle vehicle = ride.getVehicle();
		rideEntity.setVehicle(vehicleMapper.getEntityWithOnlyPK(vehicle));

		UserMapper userMapper = new UserMapper();
		User user = ride.getDriver();
		rideEntity.setDriver(userMapper.getEntityWithOnlyPK(user));

		rideEntity.setPassengers(userMapper.getEntitiesWithOnlyPK(rideEntity.getPassengers(), ride.getPassengers()));

		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		Collection<RideRequestEntity> acceptedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getAcceptedRideRequests(),
				ride.getAcceptedRideRequests());
		rideEntity.setAcceptedRideRequests(acceptedRideRequestEntities);

		Collection<RideRequestEntity> rejectedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getRejectedRideRequests(), 
				ride.getRejectedRideRequests());
		rideEntity.setRejectedRideRequests(rejectedRideRequestEntities);

		/*
		 * Pending -
		 * 
		 * - RecurringDetail
		 * - bills
		 * 
		 */

		rideEntity = getEntityChild(ride, rideEntity);

		return rideEntity;
	}

	@Override
	public RideEntity getEntityChild(Ride ride, RideEntity rideEntity) {

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
		//We need to just map Point ID from Hibernate as we are storing Point in MongoDB
		ride.getStartPoint().set_id(rideEntity.getStartPointId());
		ride.getEndPoint().set_id(rideEntity.getEndPointId());

		ride.setSeatOffered(rideEntity.getSeatOffered());
		ride.setLuggageCapacityOffered(rideEntity.getLuggageCapacityOffered());
		ride.setRecur(rideEntity.getRecur());
		ride.setStatus(rideEntity.getStatus());
		ride.setSexPreference(rideEntity.getSexPreference());
		ride.setTravelDistance(rideEntity.getTravelDistance());


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

		ride.setPassengers(userMapper.getDomainModelsWithOnlyPK(ride.getPassengers(), rideEntity.getPassengers()));

		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		Collection<RideRequest> acceptedRideRequests = rideRequestMapper.getDomainModels(ride.getAcceptedRideRequests(), 
				rideEntity.getAcceptedRideRequests());
		ride.setAcceptedRideRequests(acceptedRideRequests);

		Collection<RideRequest> rejectedRideRequests = rideRequestMapper.getDomainModels(ride.getRejectedRideRequests(), 
				rideEntity.getRejectedRideRequests());
		ride.setRejectedRideRequests(rejectedRideRequests);

		return ride;
	}


	@Override
	public Ride getDomainModelChild(Ride ride, RideEntity rideEntity) {

		return ride;
	}

	@Override
	public Collection<Ride> getDomainModelsWithOnlyPK(Collection<Ride> rides, Collection<RideEntity> rideEntities) {
		for (RideEntity rideEntity : rideEntities) {
			Ride ride = new Ride();
			ride = getDomainModelWithOnlyPK(rideEntity);
			rides.add(ride);
		}
		return rides;		

	}

	@Override
	public Collection<Ride> getDomainModels(Collection<Ride> rides, Collection<RideEntity> rideEntities) {
		for (RideEntity rideEntity : rideEntities) {
			Ride ride = new Ride();
			ride = getDomainModel(rideEntity);
			ride = getDomainModelChild(ride, rideEntity);
			rides.add(ride);
		}
		return rides;		
	}

	@Override
	public Collection<RideEntity> getEntitiesWithOnlyPK(Collection<RideEntity> rideEntities, Collection<Ride> rides) {
		for (Ride ride : rides) {
			rideEntities.add(getEntityWithOnlyPK(ride));
		}
		return rideEntities;		
	}

	@Override
	public Collection<RideEntity> getEntities(Collection<RideEntity> rideEntities, Collection<Ride> rides) {
		for (Ride ride : rides) {
			rideEntities.add(getEntity(ride));
		}
		return rideEntities;		
	}

}
