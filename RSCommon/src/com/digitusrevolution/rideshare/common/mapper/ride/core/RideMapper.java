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
		rideEntity.setEndTime(ride.getEndTime());
		//We need to just map Point ID in Hibernate as we are storing Point in MongoDB
		rideEntity.setStartPointId(ride.getStartPoint().get_id());
		rideEntity.setEndPointId(ride.getEndPoint().get_id());
		rideEntity.setStartPointAddress(ride.getStartPointAddress());
		rideEntity.setEndPointAddress(ride.getEndPointAddress());

		rideEntity.setSeatOffered(ride.getSeatOffered());
		rideEntity.setLuggageCapacityOffered(ride.getLuggageCapacityOffered());
		rideEntity.setSexPreference(ride.getSexPreference());
		rideEntity.setRecur(ride.getRecur());
		rideEntity.setStatus(ride.getStatus());
		rideEntity.setSeatStatus(ride.getSeatStatus());
		rideEntity.setTravelDistance(ride.getTravelDistance());
		rideEntity.setRideMode(ride.getRideMode());

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = ride.getTrustNetwork();
		//Reason for checking this as during ride offer, we set this value purposefully to null, so that it doesn't create twice
		if (trustNetwork!=null) rideEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork, fetchChild));

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
		 * 
		 */

		return rideEntity;
	}

	@Override
	public RideEntity getEntityChild(Ride ride, RideEntity rideEntity) {
		
		//Don't move this into entity/model function as this will call ride passenger entity and which has ride, 
		//so that would call ride mapper entity/model and it will get into recursive loop 
		RidePassengerMapper ridePassengerMapper = new RidePassengerMapper();
		if (rideEntity.getRidePassengers()!=null) rideEntity.setRidePassengers(ridePassengerMapper.getEntities(rideEntity.getRidePassengers(), ride.getRidePassengers(), false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Intentionally we are getting child of ride requests, so that we can get bill details as well from Ride
		//however we need to ensure that from Ride Request we don't fetch child of Ride otherwise it will get into recursive loop
		Collection<RideRequestEntity> acceptedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getAcceptedRideRequests(),
				ride.getAcceptedRideRequests(), true);
		rideEntity.setAcceptedRideRequests(acceptedRideRequestEntities);

		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status
		Collection<RideRequestEntity> rejectedRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getRejectedRideRequests(), 
				ride.getRejectedRideRequests(), false);
		rideEntity.setRejectedRideRequests(rejectedRideRequestEntities);
		
		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status
		Collection<RideRequestEntity> cancelledRideRequestEntities = rideRequestMapper.getEntities(rideEntity.getCancelledRideRequests(), 
				ride.getCancelledRideRequests(), false);
		rideEntity.setCancelledRideRequests(cancelledRideRequestEntities);

		return rideEntity;
	}

	@Override
	public Ride getDomainModel(RideEntity rideEntity, boolean fetchChild) {
		Ride ride = new Ride();
		ride.setId(rideEntity.getId());
		ride.setStartTime(rideEntity.getStartTime());
		ride.setEndTime(rideEntity.getEndTime());
		//We need to just map Point ID from Hibernate as we are storing Point in MongoDB
		ride.getStartPoint().set_id(rideEntity.getStartPointId());
		ride.getEndPoint().set_id(rideEntity.getEndPointId());
		ride.setStartPointAddress(rideEntity.getStartPointAddress());
		ride.setEndPointAddress(rideEntity.getEndPointAddress());

		ride.setSeatOffered(rideEntity.getSeatOffered());
		ride.setLuggageCapacityOffered(rideEntity.getLuggageCapacityOffered());
		ride.setSexPreference(rideEntity.getSexPreference());
		ride.setRecur(rideEntity.getRecur());
		ride.setStatus(rideEntity.getStatus());
		ride.setSeatStatus(rideEntity.getSeatStatus());
		ride.setTravelDistance(rideEntity.getTravelDistance());
		ride.setRideMode(rideEntity.getRideMode());


		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideEntity.getTrustNetwork(); 
		//Reason for checking this as during ride offer, we set this value purposefully to null, so that it doesn't create twice
		if (trustNetworkEntity!=null) ride.setTrustNetwork(trustNetworkMapper.getDomainModel(trustNetworkEntity, fetchChild));

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
		
		//Don't move this into entity/model function as this will call ride passenger entity and which has ride, 
		//so that would call ride mapper entity/model and it will get into recursive loop 
		RidePassengerMapper ridePassengerMapper = new RidePassengerMapper();
		if (ride.getRidePassengers()!=null)  ride.setRidePassengers(ridePassengerMapper.getDomainModels(ride.getRidePassengers(), rideEntity.getRidePassengers(), false));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't move this into entity/model function otherwise we will get into recursive loop
		//Intentionally we are getting child of ride requests, so that we can get bill details as well from Ride
		//however we need to ensure that from Ride Request we don't fetch child of Ride otherwise it will get into recursive loop
		Collection<RideRequest> acceptedRideRequests = rideRequestMapper.getDomainModels(ride.getAcceptedRideRequests(), 
				rideEntity.getAcceptedRideRequests(), true);
		ride.setAcceptedRideRequests(acceptedRideRequests);
				

		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status. Ride is calling ride request domain function and ride request is calling ride domain function
		Collection<RideRequest> rejectedRideRequests = rideRequestMapper.getDomainModels(ride.getRejectedRideRequests(), 
				rideEntity.getRejectedRideRequests(), false);
		ride.setRejectedRideRequests(rejectedRideRequests);

		//Don't fetch child as ride has ride requests and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status. Ride is calling ride request domain function and ride request is calling ride domain function
		Collection<RideRequest> cancelledRideRequests = rideRequestMapper.getDomainModels(ride.getCancelledRideRequests(), 
				rideEntity.getCancelledRideRequests(), false);
		ride.setCancelledRideRequests(cancelledRideRequests);

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
