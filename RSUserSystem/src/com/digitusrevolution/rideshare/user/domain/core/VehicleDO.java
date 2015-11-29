package com.digitusrevolution.rideshare.user.domain.core;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.VehicleMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleDO implements DomainObject{

	private Vehicle vehicle;
	private VehicleEntity vehicleEntity;
	
	public VehicleDO(){
		vehicle = new Vehicle();
		vehicleEntity = new VehicleEntity();		
	}

	public Vehicle getVehicle() {
		return vehicle;
	}

	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
		mapDomainModelToDataModel();
	}

	public VehicleEntity getVehicleEntity() {
		return vehicleEntity;
	}

	public void setVehicleEntity(VehicleEntity vehicleEntity) {
		this.vehicleEntity = vehicleEntity;
		mapDataModelToDomainModel();
	}
	@Override
	public void mapDomainModelToDataModel(){
		VehicleMapper vehicleMapper = new VehicleMapper();
		vehicleEntity = vehicleMapper.getVehicleEntity(vehicle);

		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
		vehicleEntity.setVehicleCategory(vehicleCategoryMapper.getVehicleCategoryEntity(vehicleCategory));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = vehicle.getVehicleSubCategory();
		vehicleEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getVehicleSubCategoryEntity(vehicleSubCategory));

	}
	
	@Override
	public void mapDataModelToDomainModel(){
		VehicleMapper vehicleMapper = new VehicleMapper();
		vehicle = vehicleMapper.getVehicle(vehicleEntity);

		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = vehicleEntity.getVehicleCategory();
		vehicle.setVehicleCategory(vehicleCategoryMapper.getVehicleCategory(vehicleCategoryEntity));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = vehicleEntity.getVehicleSubCategory();
		vehicle.setVehicleSubCategory(vehicleSubCategoryMapper.getVehicleSubCategory(vehicleSubCategoryEntity));
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
