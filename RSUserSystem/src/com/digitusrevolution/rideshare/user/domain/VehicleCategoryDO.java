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
	private VehicleCategoryEntity vehicleCategoryEntity;
	private VehicleCategoryMapper vehicleCategoryMapper;
	private final GenericDAO<VehicleCategoryEntity, Integer> genericDAO;
	
	public VehicleCategoryDO() {
		vehicleCategory = new VehicleCategory();
		vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryMapper = new VehicleCategoryMapper();
		genericDAO = new GenericDAOImpl<>(VehicleCategoryEntity.class);
	}
	
	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
		vehicleCategoryEntity = vehicleCategoryMapper.getEntity(vehicleCategory);
	}

	private void setVehicleCategoryEntity(VehicleCategoryEntity vehicleCategoryEntity) {
		this.vehicleCategoryEntity = vehicleCategoryEntity;
		vehicleCategory = vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity);
	}

	@Override
	public void fetchChild(){
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		vehicleCategory = vehicleCategoryMapper.getDomainModelChild(vehicleCategory, vehicleCategoryEntity);
		
	}

	@Override
	public int create(VehicleCategory vehicleCategory) {
		setVehicleCategory(vehicleCategory);
		int id = genericDAO.create(vehicleCategoryEntity);
		return id;
	}

	@Override
	public VehicleCategory get(int id) {
		vehicleCategoryEntity = genericDAO.get(id);
		if (vehicleCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setVehicleCategoryEntity(vehicleCategoryEntity);
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
			setVehicleCategoryEntity(vehicleCategoryEntity);
			vehicleCategories.add(vehicleCategory);
		}
		return vehicleCategories;
	}

	@Override
	public void update(VehicleCategory vehicleCategory) {
		if (vehicleCategory.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key "+vehicleCategory.getId());
		}
		setVehicleCategory(vehicleCategory);
		genericDAO.update(vehicleCategoryEntity);		
	}

	@Override
	public void delete(VehicleCategory vehicleCategory) {
		setVehicleCategory(vehicleCategory);
		genericDAO.delete(vehicleCategoryEntity);		
	}

}
