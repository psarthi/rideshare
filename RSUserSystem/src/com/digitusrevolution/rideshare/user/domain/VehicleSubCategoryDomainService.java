package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainService;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.user.data.VehicleSubCategoryDAO;

public class VehicleSubCategoryDomainService implements DomainService<VehicleSubCategory>{
	
	private static final Logger logger = LogManager.getLogger(VehicleSubCategoryDomainService.class.getName());
	private final VehicleSubCategoryDAO vehicleSubCategoryDAO;
	
	public VehicleSubCategoryDomainService() {
		vehicleSubCategoryDAO = new VehicleSubCategoryDAO();
	}

	@Override
	public int create(VehicleSubCategory vehicleSubCategory) {
		logger.entry();
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		vehicleSubCategoryDO.setVehicleSubCategory(vehicleSubCategory);
		int id = vehicleSubCategoryDAO.create(vehicleSubCategoryDO.getVehicleSubCategoryEntity());
		logger.exit();
		return id;
	}

	@Override
	public VehicleSubCategory get(int id) {
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity = vehicleSubCategoryDAO.get(id);
		if (vehicleSubCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleSubCategoryDO.setVehicleSubCategoryEntity(vehicleSubCategoryEntity);
		return vehicleSubCategoryDO.getVehicleSubCategory();
	}

	@Override
	public VehicleSubCategory getChild(int id) {
		 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity = vehicleSubCategoryDAO.get(id);
		if (vehicleSubCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleSubCategoryDO.setVehicleSubCategoryEntity(vehicleSubCategoryEntity);
		vehicleSubCategoryDO.mapChildDataModelToDomainModel();
		return vehicleSubCategoryDO.getVehicleSubCategory();
	}

	@Override
	public List<VehicleSubCategory> getAll() {
		List<VehicleSubCategoryEntity> vehicleSubCategoryEntities = new ArrayList<>();
		List<VehicleSubCategory> vehicleSubCategories = new ArrayList<>();
		vehicleSubCategoryEntities = vehicleSubCategoryDAO.getAll();
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
			vehicleSubCategoryDO.setVehicleSubCategoryEntity(vehicleSubCategoryEntity);
			vehicleSubCategories.add(vehicleSubCategoryDO.getVehicleSubCategory());
		}
		return vehicleSubCategories;
	}

	@Override
	public void update(VehicleSubCategory vehicleSubCategory) {
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		vehicleSubCategoryDO.setVehicleSubCategory(vehicleSubCategory);
		vehicleSubCategoryDAO.update(vehicleSubCategoryDO.getVehicleSubCategoryEntity());
	}

	@Override
	public void delete(VehicleSubCategory vehicleSubCategory) {
		VehicleSubCategoryDO vehicleSubCategoryDO = new VehicleSubCategoryDO();
		vehicleSubCategoryDO.setVehicleSubCategory(vehicleSubCategory);
		vehicleSubCategoryDAO.delete(vehicleSubCategoryDO.getVehicleSubCategoryEntity());		
	}

}
