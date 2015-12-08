package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;
import java.util.LinkedList;

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
	public VehicleEntity getEntityWithOnlyPK(Vehicle vehicle) {
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity.setId(vehicle.getId());
		return vehicleEntity;
	}

	@Override
	public VehicleEntity getEntity(Vehicle vehicle){
		VehicleEntity vehicleEntity = new VehicleEntity();
		vehicleEntity = getEntityWithOnlyPK(vehicle);
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategory vehicleCategory = vehicle.getVehicleCategory();
		vehicleEntity.setVehicleCategory(vehicleCategoryMapper.getEntity(vehicleCategory));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategory vehicleSubCategory = vehicle.getVehicleSubCategory();
		vehicleEntity.setVehicleSubCategory(vehicleSubCategoryMapper.getEntity(vehicleSubCategory));
		
		return vehicleEntity;
	}
	
	@Override
	public VehicleEntity getEntityChild(Vehicle vehicle, VehicleEntity vehicleEntity){
		return null;
	}
	
	@Override
	public Vehicle getDomainModelWithOnlyPK(VehicleEntity vehicleEntity) {
		Vehicle vehicle = new Vehicle();
		vehicle.setId(vehicleEntity.getId());
		return vehicle;
	}

	@Override
	public Vehicle getDomainModel(VehicleEntity vehicleEntity){
		Vehicle vehicle = new Vehicle();
		vehicle = getDomainModelWithOnlyPK(vehicleEntity);
		
		VehicleCategoryMapper vehicleCategoryMapper = new VehicleCategoryMapper();
		VehicleCategoryEntity vehicleCategoryEntity = vehicleEntity.getVehicleCategory();
		vehicle.setVehicleCategory(vehicleCategoryMapper.getDomainModel(vehicleCategoryEntity));
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		VehicleSubCategoryEntity vehicleSubCategoryEntity = vehicleEntity.getVehicleSubCategory();
		vehicle.setVehicleSubCategory(vehicleSubCategoryMapper.getDomainModel(vehicleSubCategoryEntity));
		
		return vehicle;
	}
	
	@Override
	public Vehicle getDomainModelChild(Vehicle vehicle, VehicleEntity vehicleEntity){		
		return null;		
	}
	
	@Override
	public Collection<VehicleEntity> getEntities(Collection<Vehicle> vehicles){		
		Collection<VehicleEntity> vehicleEntities = new LinkedList<>();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(getEntity(vehicle));
		}
		return vehicleEntities;
	}

	@Override
	public Collection<VehicleEntity> getEntitiesWithOnlyPK(Collection<Vehicle> vehicles) {
		Collection<VehicleEntity> vehicleEntities = new LinkedList<>();
		for (Vehicle vehicle : vehicles) {
			vehicleEntities.add(getEntityWithOnlyPK(vehicle));
		}
		return vehicleEntities;
	}

	@Override
	public Collection<Vehicle> getDomainModels(Collection<VehicleEntity> vehicleEntities){
		Collection<Vehicle> vehicles = new LinkedList<>();
		Vehicle vehicle = new Vehicle();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicle = getDomainModel(vehicleEntity);
			vehicles.add(vehicle);	
		}
		return vehicles;
	}

	@Override
	public Collection<Vehicle> getDomainModelsWithOnlyPK(Collection<VehicleEntity> vehicleEntities) {
		Collection<Vehicle> vehicles = new LinkedList<>();
		Vehicle vehicle = new Vehicle();
		for (VehicleEntity vehicleEntity : vehicleEntities) {
			vehicle = getDomainModelWithOnlyPK(vehicleEntity);
			vehicles.add(vehicle);	
		}
		return vehicles;
	}

}
