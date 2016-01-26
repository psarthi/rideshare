package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;

public class VehicleCategoryDO implements DomainObjectPKInteger<VehicleCategory>{
	
	private VehicleCategory vehicleCategory;
	private final GenericDAO<VehicleCategoryEntity, Integer> genericDAO;
	
	public VehicleCategoryDO() {
		vehicleCategory = new VehicleCategory();
		genericDAO = new GenericDAOImpl<>(VehicleCategoryEntity.class);
	}
	
	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	@Override
	public void fetchChild(){

	}

	@Override
	public int create(VehicleCategory vehicleCategory) {
		int id = genericDAO.create(vehicleCategory.getEntity());
		return id;
	}

	@Override
	public VehicleCategory get(int id) {
		VehicleCategoryEntity vehicleCategoryEntity = genericDAO.get(id);
		if (vehicleCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleCategory.setEntity(vehicleCategoryEntity);
		return vehicleCategory;
	}

	@Override
	public VehicleCategory getChild(int id) {
		get(id);
		fetchChild();
		return vehicleCategory;
	}

	@Override
	public List<VehicleCategory> getAll() {
		List<VehicleCategory> vehicleCategories = new ArrayList<>();
		List<VehicleCategoryEntity> vehicleCategoryEntities = genericDAO.getAll();
		for (VehicleCategoryEntity vehicleCategoryEntity : vehicleCategoryEntities) {
			VehicleCategory vehicleCategory = new VehicleCategory();
			vehicleCategory.setEntity(vehicleCategoryEntity);
			vehicleCategories.add(vehicleCategory);
		}
		return vehicleCategories;
	}

	@Override
	public void update(VehicleCategory vehicleCategory) {
		if (vehicleCategory.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key "+vehicleCategory.getId());
		}
		genericDAO.update(vehicleCategory.getEntity());		
	}

	@Override
	public void delete(int id) {
		vehicleCategory = get(id);
		genericDAO.delete(vehicleCategory.getEntity());		
	}

}
