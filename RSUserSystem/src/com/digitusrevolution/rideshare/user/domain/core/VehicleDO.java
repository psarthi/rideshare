package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.VehicleDAO;

public class VehicleDO implements DomainObjectPKInteger<Vehicle>{

	private Vehicle vehicle;
	private final VehicleDAO vehicleDAO;
	private static final Logger logger = LogManager.getLogger(VehicleDO.class.getName());
	
	public VehicleDO(){
		vehicle = new Vehicle();
		vehicleDAO = new VehicleDAO();
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	@Override
	public int create(Vehicle vehicle) {
		int id = vehicleDAO.create(vehicle.getEntity());
		return id;
	}

	@Override
	public Vehicle get(int id) {
		VehicleEntity vehicleEntity = vehicleDAO.get(id);
		if (vehicleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicle.setEntity(vehicleEntity);
		return vehicle;
	}

	@Override
	public List<Vehicle> getAll() {
		List<Vehicle> vehicles = new ArrayList<>();
		List<VehicleEntity> vehicleEntities = vehicleDAO.getAll();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			Vehicle vehicle = new Vehicle();
			vehicle.setEntity(vehicleEntity);
			vehicles.add(vehicle);
		}
		return vehicles;
	}

	@Override
	public void update(Vehicle vehicle) {
		if (vehicle.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+vehicle.getId());
		}
		vehicleDAO.update(vehicle.getEntity());		
	}

	@Override
	public void delete(int id) {
		vehicle = get(id);
		vehicleDAO.delete(vehicle.getEntity());		
	}
	
}
