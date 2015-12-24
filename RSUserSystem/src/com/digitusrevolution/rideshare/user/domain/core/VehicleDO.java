package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.VehicleDAO;

public class VehicleDO implements DomainObjectPKInteger<Vehicle>{

	private Vehicle vehicle;
	private VehicleEntity vehicleEntity;
	private VehicleMapper vehicleMapper;
	private final VehicleDAO vehicleDAO;
	private static final Logger logger = LogManager.getLogger(VehicleDO.class.getName());
	
	public VehicleDO(){
		vehicle = new Vehicle();
		vehicleEntity = new VehicleEntity();
		vehicleMapper = new VehicleMapper();
		vehicleDAO = new VehicleDAO();
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		vehicleEntity = vehicleMapper.getEntity(vehicle);
	}

	private void setVehicleEntity(VehicleEntity vehicleEntity) {
		this.vehicleEntity = vehicleEntity;
		vehicle = vehicleMapper.getDomainModel(vehicleEntity);
	}

	@Override
	public void fetchChild() {
		vehicle = vehicleMapper.getDomainModelChild(vehicle, vehicleEntity);
		
	}

	@Override
	public int create(Vehicle vehicle) {
		logger.entry();
		setVehicle(vehicle);
		int id = vehicleDAO.create(vehicleEntity);
		logger.exit();
		return id;
	}

	@Override
	public Vehicle get(int id) {
		vehicleEntity = vehicleDAO.get(id);
		if (vehicleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setVehicleEntity(vehicleEntity);
		return vehicle;
	}

	@Override
	public Vehicle getChild(int id) {
		get(id);
		fetchChild();
		return vehicle;
	}

	@Override
	public List<Vehicle> getAll() {
		List<Vehicle> vehicles = new ArrayList<>();
		List<VehicleEntity> vehicleEntities = vehicleDAO.getAll();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			setVehicleEntity(vehicleEntity);
			vehicles.add(vehicle);
		}
		return vehicles;
	}

	@Override
	public void update(Vehicle vehicle) {
		if (vehicle.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+vehicle.getId());
		}
		setVehicle(vehicle);
		vehicleDAO.update(vehicleEntity);		
	}

	@Override
	public void delete(Vehicle vehicle) {
		setVehicle(vehicle);
		vehicleDAO.delete(vehicleEntity);		
	}
	
}
