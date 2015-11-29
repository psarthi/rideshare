package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryDO implements DomainObject{
	
	private VehicleSubCategory vehicleSubCategory;
	private VehicleSubCategoryEntity vehicleSubCategoryEntity;
	
	public VehicleSubCategoryDO() {
		vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
	}

	public VehicleSubCategory getVehicleSubCategory() {
		return vehicleSubCategory;
	}


	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
		mapDomainModelToDataModel();
	}


	public VehicleSubCategoryEntity getVehicleSubCategoryEntity() {
		return vehicleSubCategoryEntity;
	}


	public void setVehicleSubCategoryEntity(VehicleSubCategoryEntity vehicleSubCategoryEntity) {
		this.vehicleSubCategoryEntity = vehicleSubCategoryEntity;
		mapDataModelToDomainModel();
	}


	@Override
	public void mapDomainModelToDataModel() {
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		vehicleSubCategoryEntity = vehicleSubCategoryMapper.getVehicleSubCategoryEntity(vehicleSubCategory);
	}

	@Override
	public void mapDataModelToDomainModel() {
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		vehicleSubCategory = vehicleSubCategoryMapper.getVehicleSubCategory(vehicleSubCategoryEntity);
		
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

}
