package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.user.VehicleSubCategoryMapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class VehicleMapper implements Mapper<Vehicle, VehicleEntity>{
	
	@Override
	public VehicleEntity getEntity(Vehicle vehicle, boolean fetchChild){
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setId(vehicle.getId());
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
		vehicleEntity.setVehicleCategory(vehicleCategoryMapper.getEntity(vehicleCategory, fetchChild));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = vehicle.getVehicleSubCategory();
		vehicleEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntity(vehicleSubCategory, fetchChild));

		return vehicleEntity;
	}
	
	@Override
	public VehicleEntity getEntityChild(Vehicle vehicle, VehicleEntity vehicleEntity){
		return vehicleEntity;
	}
	
	@Override
	public Vehicle getDomainModel(VehicleEntity vehicleEntity, boolean fetchChild){
		Vehicle vehicle = new Vehicle();
		vehicle.setId(vehicleEntity.getId());
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = vehicleEntity.getVehicleCategory();
		vehicle.setVehicleCategory(vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity, fetchChild));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = vehicleEntity.getVehicleSubCategory();
		vehicle.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity, fetchChild));
		
		return vehicle;
	}
	
	@Override
	public Vehicle getDomainModelChild(Vehicle vehicle, VehicleEntity vehicleEntity){		
		return vehicle;		
	}
	
	@Override
	public Collection<VehicleEntity> getEntities(Collection<VehicleEntity> vehicleEntities, Collection<Vehicle> vehicles, boolean fetchChild){		
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(getEntity(vehicle,fetchChild));
		}
		return vehicleEntities;
	}

	@Override
	public Collection<Vehicle> getDomainModels(Collection<Vehicle> vehicles, Collection<VehicleEntity> vehicleEntities, boolean fetchChild){
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			Vehicle vehicle = new Vehicle();
			vehicle = getDomainModel(vehicleEntity,fetchChild);
			vehicles.add(vehicle);	
		}
		return vehicles;
	}
}
