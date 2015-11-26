package com.digitusrevolution.rideshare.user.domain.core;

import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleDO {

	private Vehicle vehicle;
	private VehicleEntity vehicleEntity;
	
	public VehicleDO(){
		vehicle = new Vehicle();
		vehicleEntity = new VehicleEntity();		
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		mapDomainModelToDataModel();
	}

	public VehicleEntity getVehicleEntity() {
		return vehicleEntity;
	}

	public void setVehicleEntity(VehicleEntity vehicleEntity) {
		this.vehicleEntity = vehicleEntity;
		mapDataModelToDomainModel();
	}
	
	public void mapDomainModelToDataModel(){
		vehicleEntity.setId(vehicle.getId());	
	}
	
	public void mapDataModelToDomainModel(){
		vehicle.setId(vehicleEntity.getId());
	}

	
	
}
