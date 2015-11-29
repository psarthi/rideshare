package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
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
		
		Collection<VehicleEntity> vehicleEntities = new LinkedList<>();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(getVehicleEntity(vehicle));
		}
		return vehicleEntities;
	}


	public Collection<Vehicle> getVehicles(Collection<VehicleEntity> vehicleEntities){
		
		Collection<Vehicle> vehicles = new LinkedList<>();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicles.add(getVehicle(vehicleEntity));	
		}
		return vehicles;
	}


}
