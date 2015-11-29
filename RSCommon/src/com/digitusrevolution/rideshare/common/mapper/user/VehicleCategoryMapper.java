package com.digitusrevolution.rideshare.common.mapper.user;

import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;

public class VehicleCategoryMapper {

	public VehicleCategoryEntity getVehicleCategoryEntity(VehicleCategory vehicleCategory){
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity.setId(vehicleCategory.getId());
		vehicleCategoryEntity.setName(vehicleCategory.getName());
		return vehicleCategoryEntity;
	}

	public VehicleCategory getVehicleCategory(VehicleCategoryEntity vehicleCategoryEntity){
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setId(vehicleCategoryEntity.getId());
		vehicleCategory.setName(vehicleCategoryEntity.getName());
		return vehicleCategory;
	}
	
	
}
