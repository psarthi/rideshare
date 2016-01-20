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
	public RideEntity getEntity(Ride ride, boolean fetchChild) {
		RideEntity rideEntity = new RideEntity();
		rideEntity.setId(ride.getId());
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
		rideEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork, fetchChild));

		VehicleMapper vehicleMapper = new VehicleMapper();
		Vehicle vehicle = ride.getVehicle();
		//You can fetchChild of vehicle as it will not get into recursive loop but we would have fetchChild as false
		//Reason for making this false, as we want category and sub-category only
		//We don't want sub-categories of category which would come if fetchChild is true
		rideEntity.setVehicle(vehicleMapper.getEntity(vehicle, false));

		UserMapper userMapper = new UserMapper();
		User user = ride.getDriver();
		//Don't fetch child of user as it will get into recursive loop as driver has ride and ride has driver
		rideEntity.setDriver(userMapper.getEntity(user, false));
		
		if (fetchChild){
			rideEntity = getEntityChild(ride, rideEntity);
		}

		/*
		 * Pending -
		 * 
		 * - RecurringDetail
		 * - bills
		 * 
		 */

		return rideEntity;
	}

	@Override
	public RideEntity getEntityChild(Ride ride, RideEntity rideEntity) {
		
		//This is a custom mapper class which doesn't implement standard Mapper interface, that's why method signature is different below
		//And we are passing rideEntity as well which is not there in RidePassenger but required in RidePassengerEntity
		//Reason for not fetching child, as ride passenger has user and user has ride which in turn has ride passenger, 
		//so this will get into recursive loop
		RidePassengerMapper ridePassengerMapper = new RidePassengerMapper();
		rideEntity.setPassengers(ridePassengerMapper.getEntities(rideEntity.getPassengers(), ride.getPassengers(), ride, false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status
		Collection<RideRequestEntity> acceptedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getAcceptedRideRequests(),
				ride.getAcceptedRideRequests(), false);
		rideEntity.setAcceptedRideRequests(acceptedRideRequestEntities);

		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status
		Collection<RideRequestEntity> rejectedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getRejectedRideRequests(), 
				ride.getRejectedRideRequests(), false);
		rideEntity.setRejectedRideRequests(rejectedRideRequestEntities);

		return rideEntity;
	}

	@Override
	public Ride getDomainModel(RideEntity rideEntity, boolean fetchChild) {
		Ride ride = new Ride();
		ride.setId(rideEntity.getId());
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
		TrustNetwork trustNetwork = trustNetworkMapper.getDomainModel(trustNetworkEntity, fetchChild);
		ride.setTrustNetwork(trustNetwork);

		VehicleMapper vehicleMapper = new VehicleMapper();
		VehicleEntity vehicleEntity = rideEntity.getVehicle();
		//You can fetchChild of vehicle as it will not get into recursive loop but we would have fetchChild as false
		//Reason for making this false, as we want category and sub-category only
		//We don't want sub-categories of category which would come if fetchChild is true
		ride.setVehicle(vehicleMapper.getDomainModel(vehicleEntity, false));

		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideEntity.getDriver();
		//Don't fetch child of user as it will get into recursive loop as driver has ride and ride has driver
		ride.setDriver(userMapper.getDomainModel(userEntity, false));
				
		if (fetchChild){
			ride = getDomainModelChild(ride, rideEntity);
		}

		return ride;
	}


	@Override
	public Ride getDomainModelChild(Ride ride, RideEntity rideEntity) {
		
		//This is a custom mapper class which doesn't implement standard Mapper interface, that's why method signature is different below
		//And we are passing rideEntity as well which is not there in RidePassenger but required in RidePassengerEntity
		//Reason for not fetching child, as ride passenger has user and user has ride which in turn has ride passenger, 
		//so this will get into recursive loop
		RidePassengerMapper ridePassengerMapper = new RidePassengerMapper();
		ride.setPassengers(ridePassengerMapper.getDomainModels(ride.getPassengers(), rideEntity.getPassengers(), false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status. Ride is calling ride request domain function and ride request is calling ride domain function
		Collection<RideRequest> acceptedRideRequests = rideRequestMapper.getDomainModels(ride.getAcceptedRideRequests(), 
				rideEntity.getAcceptedRideRequests(), false);
		ride.setAcceptedRideRequests(acceptedRideRequests);

		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status. Ride is calling ride request domain function and ride request is calling ride domain function
		Collection<RideRequest> rejectedRideRequests = rideRequestMapper.getDomainModels(ride.getRejectedRideRequests(), 
				rideEntity.getRejectedRideRequests(), false);
		ride.setRejectedRideRequests(rejectedRideRequests);

		return ride;
	}

	@Override
	public Collection<Ride> getDomainModels(Collection<Ride> rides, Collection<RideEntity> rideEntities, boolean fetchChild) {
		for (RideEntity rideEntity : rideEntities) {
			Ride ride = new Ride();
			ride = getDomainModel(rideEntity, fetchChild);
			rides.add(ride);
		}
		return rides;		
	}

	@Override
	public Collection<RideEntity> getEntities(Collection<RideEntity> rideEntities, Collection<Ride> rides, boolean fetchChild) {
		for (Ride ride : rides) {
			rideEntities.add(getEntity(ride, fetchChild));
		}
		return rideEntities;		
	}

}
