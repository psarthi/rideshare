package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryDO implements DomainObjectPKInteger<VehicleSubCategory>{
	
	private VehicleSubCategory vehicleSubCategory;
	private VehicleSubCategoryEntity vehicleSubCategoryEntity;
	private VehicleSubCategoryMapper vehicleSubCategoryMapper;
	private final GenericDAO<VehicleSubCategoryEntity, Integer> genericDAO;
	
	public VehicleSubCategoryDO() {
		vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		genericDAO = new GenericDAOImpl<>(VehicleSubCategoryEntity.class);
	}

	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
		vehicleSubCategoryEntity = vehicleSubCategoryMapper.getEntity(vehicleSubCategory);
	}

	private void setVehicleSubCategoryEntity(VehicleSubCategoryEntity vehicleSubCategoryEntity) {
		this.vehicleSubCategoryEntity = vehicleSubCategoryEntity;
		vehicleSubCategory = vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int create(VehicleSubCategory vehicleSubCategory) {
		setVehicleSubCategory(vehicleSubCategory);
		int id = genericDAO.create(vehicleSubCategoryEntity);
		return id;
	}

	@Override
	public VehicleSubCategory get(int id) {
		vehicleSubCategoryEntity = genericDAO.get(id);
		if (vehicleSubCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setVehicleSubCategoryEntity(vehicleSubCategoryEntity);
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
			setVehicleSubCategoryEntity(vehicleSubCategoryEntity);
			vehicleSubCategories.add(vehicleSubCategory);
		}
		return vehicleSubCategories;
	}

	@Override
	public void update(VehicleSubCategory vehicleSubCategory) {
		setVehicleSubCategory(vehicleSubCategory);
		genericDAO.update(vehicleSubCategoryEntity);				
	}

	@Override
	public void delete(VehicleSubCategory vehicleSubCategory) {
		setVehicleSubCategory(vehicleSubCategory);
		genericDAO.delete(vehicleSubCategoryEntity);			
	}
}
