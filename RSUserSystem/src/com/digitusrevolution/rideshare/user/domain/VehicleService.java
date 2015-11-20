package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.impl.VehicleDAOImpl;
import com.digitusrevolution.rideshare.user.data.inf.IVehicleDAO;

public class VehicleService {

	private IVehicleDAO vehicleDAO;
	private VehicleDO vehicleDO;
	
	public VehicleService(){
		vehicleDAO = new VehicleDAOImpl();
		vehicleDO = new VehicleDO();
	}
	
	public boolean checkAnyVehicleExist(int userId){
		return false;
	}
	
	public VehicleDO getVehicleDO() {
		return vehicleDO;
	}

	public void setVehicleDO(VehicleDO vehicleDO) {
		this.vehicleDO = vehicleDO;
	}
	
	public void createVehicle(Vehicle vehicle){
		vehicleDO.setVehicle(vehicle);
		vehicleDAO.createVehicle(vehicleDO.getVehicleEntity());
		System.out.println("Vehicle Entity is - " + vehicleDO.getVehicleEntity().getId());
	}
}
