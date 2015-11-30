package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;

public class VehicleCategoryDO implements DomainObject{
	
	private VehicleCategory vehicleCategory;
	private VehicleCategoryEntity vehicleCategoryEntity;
	private VehicleCategoryMapper vehicleCategoryMapper;
	
	public VehicleCategoryDO() {
		vehicleCategory = new VehicleCategory();
		vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryMapper = new VehicleCategoryMapper();
	}
	
	public VehicleCategory getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategory vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
		vehicleCategoryEntity = vehicleCategoryMapper.getEntity(vehicleCategory);
	}


	public VehicleCategoryEntity getVehicleCategoryEntity() {
		return vehicleCategoryEntity;
	}



	public void setVehicleCategoryEntity(VehicleCategoryEntity vehicleCategoryEntity) {
		this.vehicleCategoryEntity = vehicleCategoryEntity;
		vehicleCategory = vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity);
	}

	@Override
	public void fetchChild(){
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		vehicleCategory = vehicleCategoryMapper.getDomainModelChild(vehicleCategory, vehicleCategoryEntity);
		
	}

}
