package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.VehicleDAO;

public class VehicleDomainService {
	
	private static final Logger logger = LogManager.getLogger(VehicleDomainService.class.getName());

	public int create(Vehicle vehicle){
		logger.entry();
		VehicleDO vehicleDO = new VehicleDO();
		VehicleDAO vehicleDAO = new VehicleDAO();
		vehicleDO.setVehicle(vehicle);
		int id = vehicleDAO.create(vehicleDO.getVehicleEntity());
		logger.exit();
		return id;
	}

	public Vehicle get(int id){
		VehicleDO vehicleDO = new VehicleDO();
		VehicleDAO vehicleDAO = new VehicleDAO();
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity = vehicleDAO.get(id);
		if (vehicleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleDO.setVehicleEntity(vehicleEntity);
		return vehicleDO.getVehicle();
	}
	
	public Vehicle getChild(int id){
		
	 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		VehicleDO vehicleDO = new VehicleDO();
		VehicleDAO vehicleDAO = new VehicleDAO();
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity = vehicleDAO.get(id);
		if (vehicleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleDO.setVehicleEntity(vehicleEntity);		
		vehicleDO.mapChildDataModelToDomainModel();
		return vehicleDO.getVehicle();
	}
	
	public List<Vehicle> getAll(){
		VehicleDAO vehicleDAO = new VehicleDAO();
		List<VehicleEntity> vehicleEntities = new ArrayList<>();
		List<Vehicle> vehicles = new ArrayList<>();
		vehicleEntities = vehicleDAO.getAll();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			VehicleDO vehicleDO = new VehicleDO();
			vehicleDO.setVehicleEntity(vehicleEntity);
			vehicles.add(vehicleDO.getVehicle());
		}
		return vehicles;
	}
	
	public void update(Vehicle vehicle){
		VehicleDO vehicleDO = new VehicleDO();
		VehicleDAO vehicleDAO = new VehicleDAO();
		vehicleDO.setVehicle(vehicle);
		vehicleDAO.update(vehicleDO.getVehicleEntity());
	}
	
	public void delete(Vehicle vehicle){
		VehicleDO vehicleDO = new VehicleDO();
		VehicleDAO vehicleDAO = new VehicleDAO();
		vehicleDO.setVehicle(vehicle);
		vehicleDAO.delete(vehicleDO.getVehicleEntity());
	}

		
}
