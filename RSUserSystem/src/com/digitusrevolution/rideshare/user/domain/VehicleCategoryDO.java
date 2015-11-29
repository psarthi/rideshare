package com.digitusrevolution.rideshare.user.domain;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleCategoryDO implements DomainObject{
	
	private VehicleCategory vehicleCategory;
	private VehicleCategoryEntity vehicleCategoryEntity;
	
	public VehicleCategoryDO() {
		vehicleCategory = new VehicleCategory();
		vehicleCategoryEntity = new VehicleCategoryEntity();
	}
	
	public VehicleCategory getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
		mapDomainModelToDataModel();
		mapChildDomainModelToDataModel();
	}


	public VehicleCategoryEntity getVehicleCategoryEntity() {
		return vehicleCategoryEntity;
	}



	public void setVehicleCategoryEntity(VehicleCategoryEntity vehicleCategoryEntity) {
		this.vehicleCategoryEntity = vehicleCategoryEntity;
		mapDataModelToDomainModel();
	}



	@Override
	public void mapDomainModelToDataModel() {
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		vehicleCategoryEntity = vehicleCategoryMapper.getVehicleCategoryEntity(vehicleCategory);
	}

	@Override
	public void mapDataModelToDomainModel() {
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		vehicleCategory = vehicleCategoryMapper.getVehicleCategory(vehicleCategoryEntity);
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		Collection<VehicleSubCategory> vehicleSubCategories = vehicleCategory.getSubCategories();
		vehicleCategoryEntity.setSubCategories(vehicleSubCategoryMapper.getVehicleSubCategoryEntities(vehicleSubCategories));		
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities = vehicleCategoryEntity.getSubCategories();
		vehicleCategory.setSubCategories(vehicleSubCategoryMapper.getVehicleSubCategories(vehicleSubCategoryEntities));		
	}

}
