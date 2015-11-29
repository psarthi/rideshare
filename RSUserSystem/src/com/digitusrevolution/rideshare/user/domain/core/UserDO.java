package com.digitusrevolution.rideshare.user.domain.core;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class UserDO implements DomainObject {

	private User user;
	private UserEntity userEntity;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		mapDomainModelToDataModel();
		mapChildDomainModelToDataModel();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		mapDataModelToDomainModel();
	}

	@Override
	public void mapDomainModelToDataModel(){

		UserMapper userMapper = new UserMapper();
		userEntity = userMapper.getUserEntity(user);
			
	}

	@Override
	public void mapDataModelToDomainModel(){	
	
		UserMapper userMapper = new UserMapper();
		user = userMapper.getUser(userEntity);
	
	}

	@Override
	public void mapChildDomainModelToDataModel(){

		VehicleMapper vehicleMapper = new VehicleMapper();
		Collection<Vehicle> vehicles = user.getVehicles();
		userEntity.setVehicles(vehicleMapper.getVehicleEntities(vehicles));
		
		RoleMapper roleMapper = new RoleMapper();
		Collection<Role> roles = user.getRoles();
		userEntity.setRoles(roleMapper.getRoleEntities(roles));
		
	}

	@Override
	public void mapChildDataModelToDomainModel(){
		
		VehicleMapper vehicleMapper = new VehicleMapper();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		user.setVehicles(vehicleMapper.getVehicles(vehicleEntities));

		RoleMapper roleMapper = new RoleMapper();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		user.setRoles(roleMapper.getRoles(roleEntities));
	}

	public void addVehicle(Vehicle vehicle){
		logger.entry();
		UserDomainService userDomainService = new UserDomainService();
		user.getVehicles().add(vehicle);
		userDomainService.update(user);
		logger.exit();
	}
}
