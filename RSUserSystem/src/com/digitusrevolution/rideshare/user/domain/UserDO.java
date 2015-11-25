package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.UserDAO;;

public class UserDO {
	
	private User user;
	private UserEntity userEntity;

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

	public void mapDomainModelToDataModel(){	
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setEmail(user.getEmail());

		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (Vehicle vehicle : vehicles) {
			VehicleDO vehicleDO = new VehicleDO();
			vehicleDO.setVehicle(vehicle);
			vehicleEntities.add(vehicleDO.getVehicleEntity());	
		}

		userEntity.setVehicles(vehicleEntities);		

	}
	
	public void mapDataModelToDomainModel(){	
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setEmail(userEntity.getEmail());		

		Collection<Vehicle> vehicles = user.getVehicles();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			VehicleDO vehicleDO = new VehicleDO();
			vehicleDO.setVehicleEntity(vehicleEntity);
			vehicles.add(vehicleDO.getVehicle());			
		}
		
		user.setVehicles(vehicles);
		
	}
	public Vehicle addVehicle(Vehicle vehicle){
		VehicleService vehicleService = new VehicleService();
		vehicle = vehicleService.createVehicle(vehicle);
		return vehicle;
	}
}
