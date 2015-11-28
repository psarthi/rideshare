package com.digitusrevolution.rideshare.user.domain.core;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleDO implements DomainObject{

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
	@Override
	public void mapDomainModelToDataModel(){
		vehicleEntity.setId(vehicle.getId());	
	}
	
	@Override
	public void mapDataModelToDomainModel(){
		vehicle.setId(vehicleEntity.getId());
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}

	
	
}
