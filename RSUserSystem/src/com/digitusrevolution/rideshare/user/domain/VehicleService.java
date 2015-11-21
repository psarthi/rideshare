package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.VehicleDAO;

public class VehicleService {

	private VehicleDAO vehicleDAO;
	
	public VehicleService(){
		vehicleDAO = new VehicleDAO();
	}
	
	public boolean checkAnyVehicleExist(int userId){
		return false;
	}
		
	public void createVehicle(Vehicle vehicle){
		VehicleDO vehicleDO = new VehicleDO();
		vehicleDO.setVehicle(vehicle);
		vehicleDAO.create(vehicleDO.getVehicleEntity());
	}
}
