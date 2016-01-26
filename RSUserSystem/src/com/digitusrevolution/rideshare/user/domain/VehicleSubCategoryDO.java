package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryDO implements DomainObjectPKInteger<VehicleSubCategory>{
	
	private VehicleSubCategory vehicleSubCategory;
	private final GenericDAO<VehicleSubCategoryEntity, Integer> genericDAO;
	
	public VehicleSubCategoryDO() {
		vehicleSubCategory = new VehicleSubCategory();
		genericDAO = new GenericDAOImpl<>(VehicleSubCategoryEntity.class);
	}

	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub		
	}

	@Override
	public int create(VehicleSubCategory vehicleSubCategory) {
		int id = genericDAO.create(vehicleSubCategory.getEntity());
		return id;
	}

	@Override
	public VehicleSubCategory get(int id) {
		VehicleSubCategoryEntity vehicleSubCategoryEntity = genericDAO.get(id);
		if (vehicleSubCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		vehicleSubCategory.setEntity(vehicleSubCategoryEntity);
		return vehicleSubCategory;
	}

	@Override
	public VehicleSubCategory getChild(int id) {
		get(id);
		fetchChild();
		return vehicleSubCategory;
	}

	@Override
	public List<VehicleSubCategory> getAll() {
		List<VehicleSubCategory> vehicleSubCategories = new ArrayList<>();
		List<VehicleSubCategoryEntity> vehicleSubCategoryEntities = genericDAO.getAll();
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
			vehicleSubCategory.setEntity(vehicleSubCategoryEntity);
			vehicleSubCategories.add(vehicleSubCategory);
		}
		return vehicleSubCategories;
	}

	@Override
	public void update(VehicleSubCategory vehicleSubCategory) {
		if (vehicleSubCategory.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+vehicleSubCategory.getId());
		}
		genericDAO.update(vehicleSubCategory.getEntity());				
	}

	@Override
	public void delete(int id) {
		vehicleSubCategory = get(id);
		genericDAO.delete(vehicleSubCategory.getEntity());			
	}
}
