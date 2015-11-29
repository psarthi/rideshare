package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleMapper {
	

	public VehicleEntity getVehicleEntity(Vehicle vehicle){
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setId(vehicle.getId());
		return vehicleEntity;
	}
	

	public Vehicle getVehicle(VehicleEntity vehicleEntity){
		Vehicle vehicle = new Vehicle();
		vehicle.setId(vehicleEntity.getId());
		return vehicle;
	}

	
	public Collection<VehicleEntity> getVehicleEntities(Collection<Vehicle> vehicles){
		
		UserEntity userEntity = new UserEntity();
		Collection<VehicleEntity> vehicleEntities = userEntity.getVehicles();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(getVehicleEntity(vehicle));
		}
		return vehicleEntities;
	}


	public Collection<Vehicle> getVehicles(Collection<VehicleEntity> vehicleEntities){
		User user = new User();
		Collection<Vehicle> vehicles = user.getVehicles();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicles.add(getVehicle(vehicleEntity));	
		}
		return vehicles;
	}


}
