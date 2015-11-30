package com.digitusrevolution.rideshare.user.domain.core;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleDO implements DomainObject{

	private Vehicle vehicle;
	private VehicleEntity vehicleEntity;
	private VehicleMapper vehicleMapper;
	
	public VehicleDO(){
		vehicle = new Vehicle();
		vehicleEntity = new VehicleEntity();
		vehicleMapper = new VehicleMapper();
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		vehicleEntity = vehicleMapper.getEntity(vehicle);
	}

	public VehicleEntity getVehicleEntity() {
		return vehicleEntity;
	}

	public void setVehicleEntity(VehicleEntity vehicleEntity) {
		this.vehicleEntity = vehicleEntity;
		vehicle = vehicleMapper.getDomainModel(vehicleEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}
	
}
