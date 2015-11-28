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
import com.digitusrevolution.rideshare.user.domain.CityDO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;

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
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());

		CityDO cityDO = new CityDO();
		cityDO.setCity(user.getCity());
		userEntity.setCity(cityDO.getCityEntity());
		
	}

	@Override
	public void mapDataModelToDomainModel(){	
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());

		CityDO cityDO = new CityDO();
		cityDO.setCityEntity(userEntity.getCity());
		user.setCity(cityDO.getCity());
		

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

		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (Vehicle vehicle : vehicles) {
			VehicleDO vehicleDO = new VehicleDO();
			vehicleDO.setVehicle(vehicle);
			vehicleEntities.add(vehicleDO.getVehicleEntity());	
		}

		userEntity.setVehicles(vehicleEntities);		

	}


	private void mapVehicleDataModelToDomainModel(){
		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			VehicleDO vehicleDO = new VehicleDO();
			vehicleDO.setVehicleEntity(vehicleEntity);
			vehicles.add(vehicleDO.getVehicle());	
		}

		user.setVehicles(vehicles);

	}

	private void mapRoleDomainModelToDataModel(){

		Collection<Role> roles = user.getRoles();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		for (Role role : roles) {
			RoleDO roleDO = new RoleDO();
			roleDO.setRole(role);
			roleEntities.add(roleDO.getRoleEntity());
		}

		userEntity.setRoles(roleEntities);		

	}

	private void mapRoleDataModelToDomainModel(){

		Collection<Role> roles = user.getRoles();
		Collection<RoleEntity> roleEntities = userEntity.getRoles();
		for (RoleEntity roleEntity : roleEntities) {
			RoleDO roleDO = new RoleDO();
			roleDO.setRoleEntity(roleEntity);
			roles.add(roleDO.getRole());
		}

		user.setRoles(roles);		


	}

	public void addVehicle(Vehicle vehicle){
		UserDomainService userService = new UserDomainService();
		user.getVehicles().add(vehicle);
		userService.update(user);
	}
}
