package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class UserMapper implements Mapper<User, UserEntity> {

	@Override
	public UserEntity getEntity(User user){
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
		userEntity.setCity(cityMapper.getEntity(city));
		
		userEntity = getEntityChild(user, userEntity);
		
		return userEntity;				
	}
	
	@Override
	public UserEntity getEntityChild(User user, UserEntity userEntity){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		Collection<Vehicle> vehicles = user.getVehicles();
		userEntity.setVehicles(vehicleMapper.getEntities(vehicles));
		
		RoleMapper roleMapper = new RoleMapper();
		Collection<Role> roles = user.getRoles();
		userEntity.setRoles(roleMapper.getEntities(roles));
		
		RideMapper rideMapper = new RideMapper();
		Collection<Ride> ridesOffered = user.getRidesOffered();
		userEntity.setRidesOffered(rideMapper.getEntities(ridesOffered));
		
		Collection<Ride> ridesTaken = user.getRidesTaken();
		userEntity.setRidesTaken(rideMapper.getEntities(ridesTaken));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		Collection<RideRequest> rideRequests = user.getRideRequests();
		userEntity.setRideRequests(rideRequestMapper.getEntities(rideRequests));

		
		return userEntity;
		
	}
	
	@Override
	public User getDomainModel(UserEntity userEntity){
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
		user.setCity(cityMapper.getDomainModel(cityEntity));
		
		return user;
	}
	
	@Override
	public User getDomainModelChild(User user, UserEntity userEntity){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		user.setVehicles(vehicleMapper.getDomainModels(vehicleEntities));

		RoleMapper roleMapper = new RoleMapper();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		user.setRoles(roleMapper.getDomainModels(roleEntities));
		
		RideMapper rideMapper = new RideMapper();
		Collection<RideEntity> ridesOfferedEntities = userEntity.getRidesOffered();
		user.setRidesOffered(rideMapper.getDomainModels(ridesOfferedEntities));
		
		Collection<RideEntity> ridesTakenEntities = userEntity.getRidesTaken();
		user.setRidesTaken(rideMapper.getDomainModels(ridesTakenEntities));
		
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		Collection<RideRequestEntity> rideRequestEntities = userEntity.getRideRequests();
		user.setRideRequests(rideRequestMapper.getDomainModels(rideRequestEntities));
		
		return user;
	}
	
	@Override
	public Collection<User> getDomainModels(Collection<UserEntity> userEntities){
		Collection<User> users = new LinkedList<>();
		User user = new User();
		for (UserEntity userEntity : userEntities) {
			user = getDomainModel(userEntity);
			user = getDomainModelChild(user, userEntity);
			users.add(user);
		}
		return users;		
	}
	
	@Override
	public Collection<UserEntity> getEntities(Collection<User> users){
		Collection<UserEntity> userEntities = new LinkedList<>();
		for (User user : users) {
			userEntities.add(getEntity(user));
		}
		return userEntities;		
	}
		
}
