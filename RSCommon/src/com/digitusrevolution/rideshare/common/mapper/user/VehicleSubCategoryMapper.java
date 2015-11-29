package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryMapper {
	
	public VehicleSubCategoryEntity getVehicleSubCategoryEntity(VehicleSubCategory vehicleSubCategory){
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity.setId(vehicleSubCategory.getId());
		vehicleSubCategoryEntity.setAirConditioner(vehicleSubCategory.getAirConditioner());
		vehicleSubCategoryEntity.setName(vehicleSubCategory.getName());
		return vehicleSubCategoryEntity;
	}

	
	public VehicleSubCategory getVehicleSubCategory(VehicleSubCategoryEntity vehicleSubCategoryEntity){
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setId(vehicleSubCategoryEntity.getId());
		vehicleSubCategory.setAirConditioner(vehicleSubCategoryEntity.getAirConditioner());
		vehicleSubCategory.setName(vehicleSubCategoryEntity.getName());
		return vehicleSubCategory;
	}
	
	public Collection<VehicleSubCategory> getVehicleSubCategories(Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities){
		
		Collection<VehicleSubCategory> vehicleSubCategories = new LinkedList<>();
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			vehicleSubCategories.add(getVehicleSubCategory(vehicleSubCategoryEntity));
		}
		return vehicleSubCategories;

	}
	
	public Collection<VehicleSubCategoryEntity> getVehicleSubCategoryEntities(Collection<VehicleSubCategory> vehicleSubCategories){
		
		Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities = new LinkedList<>();
		for (VehicleSubCategory vehicleSubCategory : vehicleSubCategories) {
			vehicleSubCategoryEntities.add(getVehicleSubCategoryEntity(vehicleSubCategory));
		}
		return vehicleSubCategoryEntities;

	}
	
}
