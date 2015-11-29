package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainService;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.user.data.VehicleCategoryDAO;

public class VehicleCategoryDomainService implements DomainService<VehicleCategory>{
	
	private static final Logger logger = LogManager.getLogger(VehicleCategoryDomainService.class.getName());
	private VehicleCategoryDAO vehicleCategoryDAO;
	
	public VehicleCategoryDomainService() {
		vehicleCategoryDAO = new VehicleCategoryDAO();
	}

	@Override
	public int create(VehicleCategory vehicleCategory) {
		logger.entry();
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		vehicleCategoryDO.setVehicleCategory(vehicleCategory);
		int id = vehicleCategoryDAO.create(vehicleCategoryDO.getVehicleCategoryEntity());
		logger.exit();
		return id;
	}

	@Override
	public VehicleCategory get(int id) {
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity = vehicleCategoryDAO.get(id);
		if (vehicleCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleCategoryDO.setVehicleCategoryEntity(vehicleCategoryEntity);
		return vehicleCategoryDO.getVehicleCategory();
	}

	@Override
	public VehicleCategory getChild(int id) {
		 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity = vehicleCategoryDAO.get(id);
		if (vehicleCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleCategoryDO.setVehicleCategoryEntity(vehicleCategoryEntity);
		vehicleCategoryDO.mapChildDataModelToDomainModel();
		return vehicleCategoryDO.getVehicleCategory();
	}

	@Override
	public List<VehicleCategory> getAll() {
		List<VehicleCategoryEntity> vehicleCategoryEntities = new ArrayList<>();
		List<VehicleCategory> vehicleCategories = new ArrayList<>();
		vehicleCategoryEntities = vehicleCategoryDAO.getAll();
		for (VehicleCategoryEntity vehicleCategoryEntity : vehicleCategoryEntities) {
			VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
			vehicleCategoryDO.setVehicleCategoryEntity(vehicleCategoryEntity);
			vehicleCategories.add(vehicleCategoryDO.getVehicleCategory());
		}
		return vehicleCategories;
	}

	@Override
	public void update(VehicleCategory vehicleCategory) {
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		vehicleCategoryDO.setVehicleCategory(vehicleCategory);
		vehicleCategoryDAO.update(vehicleCategoryDO.getVehicleCategoryEntity());
		
	}

	@Override
	public void delete(VehicleCategory vehicleCategory) {
		VehicleCategoryDO vehicleCategoryDO = new VehicleCategoryDO();
		vehicleCategoryDO.setVehicleCategory(vehicleCategory);
		vehicleCategoryDAO.delete(vehicleCategoryDO.getVehicleCategoryEntity());
		
	}

}
