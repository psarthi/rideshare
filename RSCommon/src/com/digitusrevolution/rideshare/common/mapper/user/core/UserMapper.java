package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserMapper implements Mapper<User, UserEntity> {

	@Override
	public UserEntity getEntity(User user, boolean fetchChild){
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		
		CityMapper cityMapper = new CityMapper();
		City city = user.getCity();
		userEntity.setCity(cityMapper.getEntity(city,fetchChild));
		
		if (fetchChild){
			userEntity = getEntityChild(user, userEntity);			
		} 

		
		/*
		 * Pending -
		 * 
		 * - photo
		 * - groups
		 * - friends
		 * - accounts
		 * - profileRating
		 * - bills
		 *  
		 */
		
		return userEntity;				
	}
	
	@Override
	public UserEntity getEntityChild(User user, UserEntity userEntity){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		userEntity.setVehicles(vehicleMapper.getEntities(userEntity.getVehicles(), user.getVehicles(), true));
		
		RoleMapper roleMapper = new RoleMapper();
		userEntity.setRoles(roleMapper.getEntities(userEntity.getRoles(), user.getRoles(), true));
		
		RideMapper rideMapper = new RideMapper();
		//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
		userEntity.setRidesOffered(rideMapper.getEntities(userEntity.getRidesOffered(), user.getRidesOffered(), false));
				
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't get childs of ride request as it will get into recursive loop as ride request has passenger and passenger has rides
		userEntity.setRideRequests(rideRequestMapper.getEntities(userEntity.getRideRequests(), user.getRideRequests(), false));
		
		//Note - We are not setting ridesTaken as that require ride entity which is not available in User and we don't have any requirement 
		//of setting the ride through user entity
		
		return userEntity;
		
	}
	
	@Override
	public User getDomainModel(UserEntity userEntity, boolean fetchChild){
		User user = new User();
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());

		CityMapper cityMapper = new CityMapper();
		CityEntity cityEntity = userEntity.getCity();
		user.setCity(cityMapper.getDomainModel(cityEntity, fetchChild));
		
		if (fetchChild){
			user = getDomainModelChild(user, userEntity);
		}
		
		return user;
	}
			
	@Override
	public User getDomainModelChild(User user, UserEntity userEntity){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		user.setVehicles(vehicleMapper.getDomainModels(user.getVehicles(), userEntity.getVehicles(), true));

		RoleMapper roleMapper = new RoleMapper();
		user.setRoles(roleMapper.getDomainModels(user.getRoles(), userEntity.getRoles(), true));
		
		RideMapper rideMapper = new RideMapper();
		//Don't get childs of rides as it will get into recursive loop as ride has driver and driver has rides
		user.setRidesOffered(rideMapper.getDomainModels(user.getRidesOffered(), userEntity.getRidesOffered(), false));
		
		//We are setting the ride entity here itself instead of calling Mapper class, 
		//as RidePassenger and RidePassengerEntity is not standard classes, Entity is having all fields required for hibernate and DB mapping
		//But domain model class (RidePassenger) is as per requirement of model only and we can't use RidePassenger mapper here
		//as user is holding Rides instead of RidePassenger
		Collection<RidePassengerEntity> ridePassengerEntities = userEntity.getRidesTaken();
		Collection<RideEntity> rideEntities = new HashSet<>();
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			rideEntities.add(ridePassengerEntity.getRide());
		}		
		user.setRidesTaken(rideMapper.getDomainModels(user.getRidesTaken(), rideEntities, false));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		//Don't get childs of ride request as it will get into recursive loop as ride request has passenger and passenger has rides
		user.setRideRequests(rideRequestMapper.getDomainModels(user.getRideRequests(), userEntity.getRideRequests(), false));
		
		return user;
	}
	
	@Override
	public Collection<User> getDomainModels(Collection<User> users, Collection<UserEntity> userEntities, boolean fetchChild){
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			user = getDomainModel(userEntity, fetchChild);
			users.add(user);
		}
		return users;		
	}
	
	@Override
	public Collection<UserEntity> getEntities(Collection<UserEntity> userEntities, Collection<User> users, boolean fetchChild){
		for (User user : users) {
			userEntities.add(getEntity(user, fetchChild));
		}
		return userEntities;		
	}		
}
