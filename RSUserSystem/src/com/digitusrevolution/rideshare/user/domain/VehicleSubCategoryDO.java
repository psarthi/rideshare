package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryDO implements DomainObject{
	
	private VehicleSubCategory vehicleSubCategory;
	private VehicleSubCategoryEntity vehicleSubCategoryEntity;
	private VehicleSubCategoryMapper vehicleSubCategoryMapper;
	
	public VehicleSubCategoryDO() {
		vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
	}

	public VehicleSubCategory getVehicleSubCategory() {
		return vehicleSubCategory;
	}


	public void setVehicleSubCategory(VehicleSubCategory vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
		vehicleSubCategoryEntity = vehicleSubCategoryMapper.getEntity(vehicleSubCategory);
	}


	public VehicleSubCategoryEntity getVehicleSubCategoryEntity() {
		return vehicleSubCategoryEntity;
	}


	public void setVehicleSubCategoryEntity(VehicleSubCategoryEntity vehicleSubCategoryEntity) {
		this.vehicleSubCategoryEntity = vehicleSubCategoryEntity;
		vehicleSubCategory = vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}
}
