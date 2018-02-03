package com.digitusrevolution.rideshare.common.mapper.ride.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.BillMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.UserFeedbackMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;
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
	public RideRequestEntity getEntity(RideRequest rideRequest, boolean fetchChild) {

		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity.setId(rideRequest.getId());
		rideRequestEntity.setPickupTime(rideRequest.getPickupTime());
		rideRequestEntity.setPickupTimeVariation(rideRequest.getPickupTimeVariation());
		rideRequestEntity.setSexPreference(rideRequest.getSexPreference());
		rideRequestEntity.setSeatRequired(rideRequest.getSeatRequired());
		rideRequestEntity.setLuggageCapacityRequired(rideRequest.getLuggageCapacityRequired());
		rideRequestEntity.setPickupPointVariation(rideRequest.getPickupPointVariation());
		rideRequestEntity.setDropPointVariation(rideRequest.getDropPointVariation());
		rideRequestEntity.setStatus(rideRequest.getStatus());
		rideRequestEntity.setTravelTime(rideRequest.getTravelTime());
		rideRequestEntity.setTravelDistance(rideRequest.getTravelDistance());
		rideRequestEntity.setRideMode(rideRequest.getRideMode());
		rideRequestEntity.setConfirmationCode(rideRequest.getConfirmationCode());
		

		//We need to just map Point ID in Hibernate as we are storing Point in MongoDB
		rideRequestEntity.setPickupPointId(rideRequest.getPickupPoint().get_id());
		rideRequestEntity.setDropPointId(rideRequest.getDropPoint().get_id());	
		rideRequestEntity.setRidePickupPointId(rideRequest.getRidePickupPoint().get_id());		
		rideRequestEntity.setRideDropPointId(rideRequest.getRideDropPoint().get_id());
		
		rideRequestEntity.setPickupPointAddress(rideRequest.getPickupPointAddress());
		rideRequestEntity.setDropPointAddress(rideRequest.getDropPointAddress());
		rideRequestEntity.setRidePickupPointAddress(rideRequest.getRidePickupPointAddress());
		rideRequestEntity.setRideDropPointAddress(rideRequest.getRideDropPointAddress());
		
		rideRequestEntity.setRidePickupPointDistance(rideRequest.getRidePickupPointDistance());
		rideRequestEntity.setRideDropPointDistance(rideRequest.getRideDropPointDistance());

		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = rideRequest.getVehicleCategory();
		//We can get child as it doesn't have any ride request and will not get into recursive loop
		rideRequestEntity.setVehicleCategory(vehicleCategoryMapper.getEntity(vehicleCategory, fetchChild));

		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = rideRequest.getVehicleSubCategory();
		//We can get child as it doesn't have any ride request and will not get into recursive loop
		if (vehicleSubCategory!=null) rideRequestEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntity(vehicleSubCategory, fetchChild));

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
		//Reason for checking this as during ride request creation, we set this value purposefully to null, so that it doesn't create twice
		if (trustNetwork!=null) rideRequestEntity.setTrustNetwork(trustNetworkMapper.getEntity(trustNetwork, fetchChild));

		UserMapper userMapper = new UserMapper();
		User user = rideRequest.getPassenger();
		//Don't get child of user as user has ride request and ride request has user, so it will get into recursive loop
		rideRequestEntity.setPassenger(userMapper.getEntity(user, false));
		
		rideRequestEntity.setPassengerStatus(rideRequest.getPassengerStatus());

		if (fetchChild){
			rideRequestEntity = getEntityChild(rideRequest, rideRequestEntity);			
		}

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
		Ride ride = rideRequest.getAcceptedRide();
		//Don't get child of ride has ride request and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status
		if (ride!=null) rideRequestEntity.setAcceptedRide(rideMapper.getEntity(ride, false));
		
		BillMapper billMapper = new BillMapper();
		//Intentionally we are getting Bill childs so that we can generate Bills from Ride System and all mapping is in place while creation
		//Imp - Since we are getting child of bills, its very important to ensure we stop the recursive loop by making false in Bill Childs for Ride Request
		//As Ride Request has bill and bill has ride request, but in case of fetching bills child we don't fetch Ride request child again and this helps to stop recursive loop
		if (rideRequest.getBill()!=null) rideRequestEntity.setBill(billMapper.getEntity(rideRequest.getBill(), true));
		
		//Don't fetch Child as Ride Request has Rides and vice versa
		rideRequestEntity.setCancelledRides(rideMapper.getEntities(rideRequestEntity.getCancelledRides(), 
				rideRequest.getCancelledRides(), false));
		
		UserFeedbackMapper userFeedbackMapper = new UserFeedbackMapper();
		//Don't fetch child as User has feedback and feedback has user
		rideRequestEntity.setFeedbacks(userFeedbackMapper.getEntities(rideRequestEntity.getFeedbacks(), 
				rideRequest.getFeedbacks(), false));


		return rideRequestEntity;
	}

	@Override
	public RideRequest getDomainModel(RideRequestEntity rideRequestEntity, boolean fetchChild) {

		RideRequest rideRequest = new RideRequest();
		rideRequest.setId(rideRequestEntity.getId());
		rideRequest.setPickupTime(rideRequestEntity.getPickupTime());
		rideRequest.setPickupTimeVariation(rideRequestEntity.getPickupTimeVariation());
		rideRequest.setSexPreference(rideRequestEntity.getSexPreference());
		rideRequest.setSeatRequired(rideRequestEntity.getSeatRequired());
		rideRequest.setLuggageCapacityRequired(rideRequestEntity.getLuggageCapacityRequired());
		rideRequest.setPickupPointVariation(rideRequestEntity.getPickupPointVariation());
		rideRequest.setDropPointVariation(rideRequestEntity.getDropPointVariation());
		rideRequest.setStatus(rideRequestEntity.getStatus());
		rideRequest.setTravelTime(rideRequestEntity.getTravelTime());
		rideRequest.setTravelDistance(rideRequestEntity.getTravelDistance());
		rideRequest.setRideMode(rideRequestEntity.getRideMode());
		rideRequest.setConfirmationCode(rideRequestEntity.getConfirmationCode());

		//We need to just map Point ID from Hibernate as we are storing Point in MongoDB
		rideRequest.getPickupPoint().set_id(rideRequestEntity.getPickupPointId());
		rideRequest.getDropPoint().set_id(rideRequestEntity.getDropPointId());	
		rideRequest.getRidePickupPoint().set_id(rideRequestEntity.getRidePickupPointId());
		rideRequest.getRideDropPoint().set_id(rideRequestEntity.getRideDropPointId());	
		
		rideRequest.setPickupPointAddress(rideRequestEntity.getPickupPointAddress());
		rideRequest.setDropPointAddress(rideRequestEntity.getDropPointAddress());
		rideRequest.setRidePickupPointAddress(rideRequestEntity.getRidePickupPointAddress());
		rideRequest.setRideDropPointAddress(rideRequestEntity.getRideDropPointAddress());
		
		rideRequest.setRidePickupPointDistance(rideRequestEntity.getRidePickupPointDistance());
		rideRequest.setRideDropPointDistance(rideRequestEntity.getRideDropPointDistance());

		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = rideRequestEntity.getVehicleCategory();
		//We can get child as it doesn't have any ride request and will not get into recursive loop
		rideRequest.setVehicleCategory(vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity, fetchChild));

		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = rideRequestEntity.getVehicleSubCategory();
		//We can get child as it doesn't have any ride request and will not get into recursive loop
		if (vehicleSubCategoryEntity!=null) rideRequest.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity, fetchChild));

		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		TrustNetworkEntity trustNetworkEntity = rideRequestEntity.getTrustNetwork(); 
		//Reason for checking this as during ride request creation, we set this value purposefully to null, so that it doesn't create twice
		if (trustNetworkEntity!=null)  rideRequest.setTrustNetwork(trustNetworkMapper.getDomainModel(trustNetworkEntity, fetchChild));

		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = rideRequestEntity.getPassenger();
		//Don't get child of user as user has ride request and ride request has user, so it will get into recursive loop
		rideRequest.setPassenger(userMapper.getDomainModel(userEntity, false));
		
		rideRequest.setPassengerStatus(rideRequestEntity.getPassengerStatus());
		
		if (fetchChild){
			rideRequest = getDomainModelChild(rideRequest, rideRequestEntity);			
		}
		return rideRequest;

	}

	@Override
	public RideRequest getDomainModelChild(RideRequest rideRequest, RideRequestEntity rideRequestEntity) {
		RideMapper rideMapper = new RideMapper();
		RideEntity rideEntity = rideRequestEntity.getAcceptedRide();
		//Don't get child as ride has ride request and ride request has ride, so it will get into recursive loop
		//Reason for having this in child and not in entity/domain as it will get into recursive loop as entity/domain function 
		//is called irrespective of fetchChild status. Ride request is calling ride domain function and ride is calling ride request domain function
		if (rideEntity!=null) rideRequest.setAcceptedRide(rideMapper.getDomainModel(rideEntity, false));
		
		BillMapper billMapper = new BillMapper();
		//Intentionally we are getting Bill childs so that we can generate Bills from Ride System and all mapping is in place while creation
		//Imp - Since we are getting child of bills, its very important to ensure we stop the recursive loop by making false in Bill Childs for Ride Request
		//As Ride Request has bill and bill has ride request, but in case of fetching bills child we don't fetch Ride request child again and this helps to stop recursive loop
		if (rideRequestEntity.getBill()!=null) rideRequest.setBill(billMapper.getDomainModel(rideRequestEntity.getBill(), true));
		
		//Don't fetch Child as Ride Request has Rides and vice versa
		rideRequest.setCancelledRides(rideMapper.getDomainModels(rideRequest.getCancelledRides(), 
				rideRequestEntity.getCancelledRides(), false));
		
		UserFeedbackMapper userFeedbackMapper = new UserFeedbackMapper();
		//Don't fetch child as User has feedback and feedback has user
		rideRequest.setFeedbacks(userFeedbackMapper.getDomainModels(rideRequest.getFeedbacks(), 
				rideRequestEntity.getFeedbacks(), false));

		return rideRequest;
	}

	@Override
	public Collection<RideRequest> getDomainModels(Collection<RideRequest> rideRequests, Collection<RideRequestEntity> rideRequestEntities, boolean fetchChild) {
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			RideRequest rideRequest = new RideRequest();
			rideRequest = getDomainModel(rideRequestEntity, fetchChild);
			rideRequests.add(rideRequest);
		}
		return rideRequests;		
	}


	@Override
	public Collection<RideRequestEntity> getEntities(Collection<RideRequestEntity> rideRequestEntities, Collection<RideRequest> rideRequests, boolean fetchChild) {
		for (RideRequest rideRequest : rideRequests) {
			rideRequestEntities.add(getEntity(rideRequest, fetchChild));
		}
		return rideRequestEntities;		
	}

}
