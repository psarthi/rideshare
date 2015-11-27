package com.digitusrevolution.rideshare.user.domain.core;

import java.util.Collection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.domain.UserUtil;

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
		UserUtil userUtil = new UserUtil();
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());
		if (user.getCity()!=null){
		userEntity.setCity(userUtil.getCityEntity(user.getCity()));			
		}
	}
	
	@Override
	public void mapDataModelToDomainModel(){	
		UserUtil userUtil = new UserUtil();
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());
		user.setCity(userUtil.getCity(userEntity.getCity()));			

	}
	
	@Override
	public void mapChildDomainModelToDataModel(){
		
		mapVehicleDomainModelToDataModel();
		mapRoleDomainModelToDataModel();
	}
	
	@Override
	public void mapChildDataModelToDomainModel(){
	
		mapVehicleDataModelToDomainModel();
		mapRoleDataModelToDomainModel();
		
	}
	
	private void mapVehicleDomainModelToDataModel(){

		UserUtil userUtil = new UserUtil();
		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(userUtil.getVehicleEntity(vehicle));	
		}

		userEntity.setVehicles(vehicleEntities);		

	}
	

	private void mapVehicleDataModelToDomainModel(){
		UserUtil userUtil = new UserUtil();
		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicles.add(userUtil.getVehicle(vehicleEntity));			
		}
		
		user.setVehicles(vehicles);

	}
	
	private void mapRoleDomainModelToDataModel(){
		
		UserUtil userUtil = new UserUtil();
		Collection<Role> roles = user.getRoles();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		for (Role role : roles) {
			roleEntities.add(userUtil.getRoleEntity(role));
		}
		
		userEntity.setRoles(roleEntities);		
		
	}
	
	private void mapRoleDataModelToDomainModel(){
		
		UserUtil userUtil = new UserUtil();
		Collection<Role> roles = user.getRoles();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		for (RoleEntity roleEntity : roleEntities) {
			roles.add(userUtil.getRole(roleEntity));
		}
		
		user.setRoles(roles);		

		
	}
	
	public void addVehicle(Vehicle vehicle){
		user.getVehicles().add(vehicle);
		UserService userService = new UserService();
		userService.update(user);
	}
}
