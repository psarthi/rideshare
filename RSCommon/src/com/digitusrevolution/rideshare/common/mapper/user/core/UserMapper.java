package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserMapper implements Mapper<User, UserEntity> {

	@Override
	public UserEntity getEntityWithOnlyPK(User user){
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		return userEntity;
	}
	
	@Override
	public UserEntity getEntity(User user){
		UserEntity userEntity = new UserEntity();
		userEntity = getEntityWithOnlyPK(user);
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		
		CityMapper cityMapper = new CityMapper();
		City city = user.getCity();
		userEntity.setCity(cityMapper.getEntity(city));
		
		userEntity = getEntityChild(user, userEntity);
		
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
		userEntity.setVehicles(vehicleMapper.getEntities(userEntity.getVehicles(), user.getVehicles()));
		
		RoleMapper roleMapper = new RoleMapper();
		userEntity.setRoles(roleMapper.getEntities(userEntity.getRoles(), user.getRoles()));
		
		RideMapper rideMapper = new RideMapper();
		userEntity.setRidesOffered(rideMapper.getEntitiesWithOnlyPK(userEntity.getRidesOffered(), user.getRidesOffered()));
		
		userEntity.setRidesTaken(rideMapper.getEntitiesWithOnlyPK(userEntity.getRidesTaken(), user.getRidesTaken()));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		userEntity.setRideRequests(rideRequestMapper.getEntitiesWithOnlyPK(userEntity.getRideRequests(), user.getRideRequests()));
		
		return userEntity;
		
	}
	
	@Override
	public User getDomainModelWithOnlyPK(UserEntity userEntity){
		User user = new User();
		user.setId(userEntity.getId());
		return user;
	}
	
	@Override
	public User getDomainModel(UserEntity userEntity){
		User user = new User();
		user = getDomainModelWithOnlyPK(userEntity);
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());

		CityMapper cityMapper = new CityMapper();
		CityEntity cityEntity = userEntity.getCity();
		user.setCity(cityMapper.getDomainModel(cityEntity));
		
		return user;
	}
	
	@Override
	public User getDomainModelChild(User user, UserEntity userEntity){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		user.setVehicles(vehicleMapper.getDomainModels(user.getVehicles(), userEntity.getVehicles()));

		RoleMapper roleMapper = new RoleMapper();
		user.setRoles(roleMapper.getDomainModels(user.getRoles(), userEntity.getRoles()));
		
		RideMapper rideMapper = new RideMapper();
		user.setRidesOffered(rideMapper.getDomainModelsWithOnlyPK(user.getRidesOffered(), userEntity.getRidesOffered()));
		
		user.setRidesTaken(rideMapper.getDomainModelsWithOnlyPK(user.getRidesTaken(), userEntity.getRidesTaken()));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		user.setRideRequests(rideRequestMapper.getDomainModelsWithOnlyPK(user.getRideRequests(), userEntity.getRideRequests()));
		
		return user;
	}
	
	@Override
	public Collection<User> getDomainModelsWithOnlyPK(Collection<User> users, Collection<UserEntity> userEntities){
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			user = getDomainModelWithOnlyPK(userEntity);
			users.add(user);
		}
		return users;		
	}
	
	@Override
	public Collection<User> getDomainModels(Collection<User> users, Collection<UserEntity> userEntities){
		for (UserEntity userEntity : userEntities) {
			User user = new User();
			user = getDomainModel(userEntity);
			user = getDomainModelChild(user, userEntity);
			users.add(user);
		}
		return users;		
	}
	
	@Override
	public Collection<UserEntity> getEntitiesWithOnlyPK(Collection<UserEntity> userEntities, Collection<User> users){
		for (User user : users) {
			userEntities.add(getEntityWithOnlyPK(user));
		}
		return userEntities;		

	}
	
	@Override
	public Collection<UserEntity> getEntities(Collection<UserEntity> userEntities, Collection<User> users){
		for (User user : users) {
			userEntities.add(getEntity(user));
		}
		return userEntities;		
	}
		
}
