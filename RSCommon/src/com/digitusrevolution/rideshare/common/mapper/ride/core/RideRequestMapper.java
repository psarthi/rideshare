package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RideRequestMapper implements Mapper<RideRequest, RideRequestEntity>{

	@Override
	public RideRequestEntity getEntityWithOnlyPK(RideRequest rideRequest) {
		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity.setId(rideRequest.getId());
		return rideRequestEntity;
	}

	@Override
	public RideRequestEntity getEntity(RideRequest rideRequest) {
		
		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity = getEntityWithOnlyPK(rideRequest);
		rideRequestEntity.setPickupTime(rideRequest.getPickupTime());
		rideRequestEntity.setPickupTimeVariation(rideRequest.getPickupTimeVariation());
		rideRequestEntity.setSexPreference(rideRequest.getSexPreference());
		rideRequestEntity.setSeatRequired(rideRequest.getSeatRequired());
		rideRequestEntity.setLuggageCapacityRequired(rideRequest.getLuggageCapacityRequired());
		rideRequestEntity.setPickupPointVariation(rideRequest.getPickupPointVariation());
		rideRequestEntity.setDropPointVariation(rideRequest.getDropPointVariation());
		rideRequestEntity.setStatus(rideRequest.getStatus());
		rideRequestEntity.setRidePreference(rideRequest.getRidePreference());
		rideRequestEntity.setTravelTime(rideRequest.getTravelTime());
		rideRequestEntity.setTravelDistance(rideRequest.getTravelDistance());

		

		//We need to just map Point ID in Hibernate as we are storing Point in MongoDB
		rideRequestEntity.setPickupPointId(rideRequest.getPickupPoint().get_id());
		rideRequestEntity.setDropPointId(rideRequest.getDropPoint().get_id());	
		rideRequestEntity.setRidePickupPointId(rideRequest.getRidePickupPoint().get_id());		
		rideRequestEntity.setRideDropPointId(rideRequest.getRideDropPoint().get_id());	

		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = rideRequest.getVehicleCategory();
		rideRequestEntity.setVehicleCategory(vehicleCategoryMapper.getEntityWithOnlyPK(vehicleCategory));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = rideRequest.getVehicleSubCategory();
		if (vehicleSubCategory!=null) rideRequestEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntityWithOnlyPK(vehicleSubCategory));

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
		rideRequestEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork));

		UserMapper userMapper = new UserMapper();
		User user = rideRequest.getPassenger();
		rideRequestEntity.setPassenger(userMapper.getEntityWithOnlyPK(user));
		
		RideMapper rideMapper = new RideMapper();
		Ride ride = rideRequest.getAcceptedRide();
		if (ride!=null) rideRequestEntity.setAcceptedRide(rideMapper.getEntityWithOnlyPK(ride));
		
		rideRequestEntity = getEntityChild(rideRequest, rideRequestEntity);
		
		/*
		 * Pending -
		 * 
		 * - preferredRides
		 * 
		 */

		return rideRequestEntity;
		
	}

	@Override
	public RideRequestEntity getEntityChild(RideRequest rideRequest, RideRequestEntity rideRequestEntity) {

		RideMapper rideMapper = new RideMapper();
		Collection<Ride> rides = rideRequest.getPreferredRides();
		rideRequestEntity.setPreferredRides(rideMapper.getEntitiesWithOnlyPK(rides));
		
		return rideRequestEntity;
	}

	@Override
	public RideRequest getDomainModelWithOnlyPK(RideRequestEntity rideRequestEntity) {
		RideRequest rideRequest = new RideRequest();
		rideRequest.setId(rideRequestEntity.getId());
		return rideRequest;
	}
	
	@Override
	public RideRequest getDomainModel(RideRequestEntity rideRequestEntity) {
		
		RideRequest rideRequest = new RideRequest();
		rideRequest = getDomainModelWithOnlyPK(rideRequestEntity);
		rideRequest.setPickupTime(rideRequestEntity.getPickupTime());
		rideRequest.setPickupTimeVariation(rideRequestEntity.getPickupTimeVariation());
		rideRequest.setSexPreference(rideRequestEntity.getSexPreference());
		rideRequest.setSeatRequired(rideRequestEntity.getSeatRequired());
		rideRequest.setLuggageCapacityRequired(rideRequestEntity.getLuggageCapacityRequired());
		rideRequest.setPickupPointVariation(rideRequestEntity.getPickupPointVariation());
		rideRequest.setDropPointVariation(rideRequestEntity.getDropPointVariation());
		rideRequest.setStatus(rideRequestEntity.getStatus());
		rideRequest.setRidePreference(rideRequestEntity.getRidePreference());
		rideRequest.setTravelTime(rideRequestEntity.getTravelTime());
		rideRequest.setTravelDistance(rideRequestEntity.getTravelDistance());
		
		//We need to just map Point ID from Hibernate as we are storing Point in MongoDB
		rideRequest.getPickupPoint().set_id(rideRequestEntity.getPickupPointId());
		rideRequest.getDropPoint().set_id(rideRequestEntity.getDropPointId());	
		rideRequest.getRidePickupPoint().set_id(rideRequestEntity.getRidePickupPointId());
		rideRequest.getRideDropPoint().set_id(rideRequestEntity.getRideDropPointId());	
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = rideRequestEntity.getVehicleCategory();
		rideRequest.setVehicleCategory(vehicleCategoryMapper.getDomainModelWithOnlyPK(vehicleCategoryEntity));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = rideRequestEntity.getVehicleSubCategory();
		if (vehicleSubCategoryEntity!=null) rideRequest.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModelWithOnlyPK(vehicleSubCategoryEntity));

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideRequestEntity.getTrustNetwork();
		TrustNetwork trustNetwork = trustNetworkMapper.getDomainModel(trustNetworkEntity);
		trustNetwork = trustNetworkMapper.getDomainModelChild(trustNetwork, trustNetworkEntity);
		rideRequest.setTrustNetwork(trustNetwork);

		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideRequestEntity.getPassenger();
		rideRequest.setPassenger(userMapper.getDomainModelWithOnlyPK(userEntity));
		
		RideMapper rideMapper = new RideMapper();
		RideEntity rideEntity = rideRequestEntity.getAcceptedRide();
		if (rideEntity!=null) rideRequest.setAcceptedRide(rideMapper.getDomainModelWithOnlyPK(rideEntity));
		return rideRequest;

	}

	@Override
	public RideRequest getDomainModelChild(RideRequest rideRequest, RideRequestEntity rideRequestEntity) {
		RideMapper rideMapper = new RideMapper();
		Collection<RideEntity> rideEntities = rideRequestEntity.getPreferredRides();
		rideRequest.setPreferredRides(rideMapper.getDomainModelsWithOnlyPK(rideEntities));
		
		return rideRequest;
	}

	@Override
	public Collection<RideRequest> getDomainModels(Collection<RideRequestEntity> rideRequestEntities) {
		Collection<RideRequest> rideRequests = new LinkedList<>();
		RideRequest rideRequest = new RideRequest();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			rideRequest = getDomainModel(rideRequestEntity);
			rideRequest = getDomainModelChild(rideRequest, rideRequestEntity);
			rideRequests.add(rideRequest);
		}
		return rideRequests;		
	}

	@Override
	public Collection<RideRequest> getDomainModelsWithOnlyPK(Collection<RideRequestEntity> rideRequestEntities) {
		Collection<RideRequest> rideRequests = new LinkedList<>();
		RideRequest rideRequest = new RideRequest();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			rideRequest = getDomainModelWithOnlyPK(rideRequestEntity);
			rideRequests.add(rideRequest);
		}
		return rideRequests;		
	}


	@Override
	public Collection<RideRequestEntity> getEntities(Collection<RideRequest> rideRequests) {
		Collection<RideRequestEntity> rideRequestEntities = new LinkedList<>();
		for (RideRequest rideRequest : rideRequests) {
			rideRequestEntities.add(getEntity(rideRequest));
		}
		return rideRequestEntities;		
	}

	@Override
	public Collection<RideRequestEntity> getEntitiesWithOnlyPK(Collection<RideRequest> rideRequests) {
		Collection<RideRequestEntity> rideRequestEntities = new LinkedList<>();
		for (RideRequest rideRequest : rideRequests) {
			rideRequestEntities.add(getEntityWithOnlyPK(rideRequest));
		}
		return rideRequestEntities;		
	}

}
