package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.PointMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
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
	public RideRequestEntity getEntity(RideRequest rideRequest) {
		
		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity.setId(rideRequest.getId());
		rideRequestEntity.setDateTime(rideRequest.getDateTime());
		rideRequestEntity.setSeatRequired(rideRequest.getSeatRequired());
		rideRequestEntity.setLuggageCapacityRequired(rideRequest.getLuggageCapacityRequired());
		rideRequestEntity.setPickupPointVariation(rideRequest.getPickupPointVariation());
		rideRequestEntity.setDropPointVariation(rideRequest.getDropPointVariation());
		rideRequestEntity.setStatus(rideRequest.getStatus());
		rideRequestEntity.setRidePreference(rideRequest.getRidePreference());

		PointMapper pointMapper = new PointMapper();
		Point point = rideRequest.getPickupPoint();
		rideRequestEntity.setPickupPoint(pointMapper.getEntity(point));
		point = rideRequest.getDropPoint();
		rideRequestEntity.setDropPoint(pointMapper.getEntity(point));
		point = rideRequest.getRidePickupPoint();
		rideRequestEntity.setRidePickupPoint(pointMapper.getEntity(point));
		point = rideRequest.getRideDropPoint();
		rideRequestEntity.setRideDropPoint(pointMapper.getEntity(point));	
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = rideRequest.getVehicleCategory();
		rideRequestEntity.setVehicleCategory(vehicleCategoryMapper.getEntity(vehicleCategory));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = rideRequest.getVehicleSubCategory();
		rideRequestEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntity(vehicleSubCategory));
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
		rideRequestEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork));

		UserMapper userMapper = new UserMapper();
		User user = rideRequest.getPassenger();
		rideRequestEntity.setPassenger(userMapper.getEntity(user));
		
		RideMapper rideMapper = new RideMapper();
		Ride ride = rideRequest.getAcceptedRide();
		rideRequestEntity.setAcceptedRide(rideMapper.getEntity(ride));
		
		rideRequestEntity = getEntityChild(rideRequest, rideRequestEntity);

		return rideRequestEntity;
		
	}

	@Override
	public RideRequestEntity getEntityChild(RideRequest rideRequest, RideRequestEntity rideRequestEntity) {

		RideMapper rideMapper = new RideMapper();
		Collection<Ride> rides = rideRequest.getPreferredRides();
		rideRequestEntity.setPreferredRides(rideMapper.getEntities(rides));
		
		return rideRequestEntity;
	}

	@Override
	public RideRequest getDomainModel(RideRequestEntity rideRequestEntity) {
		
		RideRequest rideRequest = new RideRequest();
		rideRequest.setId(rideRequestEntity.getId());
		rideRequest.setDateTime(rideRequestEntity.getDateTime());
		rideRequest.setSeatRequired(rideRequestEntity.getSeatRequired());
		rideRequest.setLuggageCapacityRequired(rideRequestEntity.getLuggageCapacityRequired());
		rideRequest.setPickupPointVariation(rideRequestEntity.getPickupPointVariation());
		rideRequest.setDropPointVariation(rideRequestEntity.getDropPointVariation());
		rideRequest.setStatus(rideRequestEntity.getStatus());
		rideRequest.setRidePreference(rideRequestEntity.getRidePreference());

		PointMapper pointMapper = new PointMapper();
		PointEntity pointEntity = rideRequestEntity.getPickupPoint();
		rideRequest.setPickupPoint(pointMapper.getDomainModel(pointEntity));
		pointEntity = rideRequestEntity.getDropPoint();
		rideRequest.setDropPoint(pointMapper.getDomainModel(pointEntity));
		pointEntity = rideRequestEntity.getRidePickupPoint();
		rideRequest.setRidePickupPoint(pointMapper.getDomainModel(pointEntity));
		pointEntity = rideRequestEntity.getRideDropPoint();
		rideRequest.setRideDropPoint(pointMapper.getDomainModel(pointEntity));	
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = rideRequestEntity.getVehicleCategory();
		rideRequest.setVehicleCategory(vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = rideRequestEntity.getVehicleSubCategory();
		rideRequest.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity));
		
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideRequestEntity.getTrustNetwork();
		rideRequest.setTrustNetwork(trustNetworkMapper.getDomainModel(trustNetworkEntity));

		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideRequestEntity.getPassenger();
		rideRequest.setPassenger(userMapper.getDomainModel(userEntity));
		
		RideMapper rideMapper = new RideMapper();
		RideEntity rideEntity = rideRequestEntity.getAcceptedRide();
		rideRequest.setAcceptedRide(rideMapper.getDomainModel(rideEntity));
		
		return rideRequest;

	}

	@Override
	public RideRequest getDomainModelChild(RideRequest rideRequest, RideRequestEntity rideRequestEntity) {
		RideMapper rideMapper = new RideMapper();
		Collection<RideEntity> rideEntities = rideRequestEntity.getPreferredRides();
		rideRequest.setPreferredRides(rideMapper.getDomainModels(rideEntities));
		
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
	public Collection<RideRequestEntity> getEntities(Collection<RideRequest> rideRequests) {
		Collection<RideRequestEntity> rideRequestEntities = new LinkedList<>();
		for (RideRequest rideRequest : rideRequests) {
			rideRequestEntities.add(getEntity(rideRequest));
		}
		return rideRequestEntities;		
	}

}
