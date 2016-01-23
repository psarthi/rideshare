package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryMapper implements Mapper<VehicleSubCategory, VehicleSubCategoryEntity>{


	@Override
	public VehicleSubCategoryEntity getEntity(VehicleSubCategory vehicleSubCategory, boolean fetchChild){
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity.setId(vehicleSubCategory.getId());
		vehicleSubCategoryEntity.setAirConditioner(vehicleSubCategory.getAirConditioner());
		vehicleSubCategoryEntity.setName(vehicleSubCategory.getName());
		vehicleSubCategoryEntity.setFuelType(vehicleSubCategory.getFuelType());
		vehicleSubCategoryEntity.setAverageMileage(vehicleSubCategory.getAverageMileage());
		return vehicleSubCategoryEntity;
	}

	@Override
	public VehicleSubCategoryEntity getEntityChild(VehicleSubCategory vehicleSubCategory, VehicleSubCategoryEntity vehicleSubCategoryEntity){	
		return vehicleSubCategoryEntity;
	}

	@Override
	public VehicleSubCategory getDomainModel(VehicleSubCategoryEntity vehicleSubCategoryEntity, boolean fetchChild){
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setId(vehicleSubCategoryEntity.getId());
		vehicleSubCategory.setAirConditioner(vehicleSubCategoryEntity.getAirConditioner());
		vehicleSubCategory.setName(vehicleSubCategoryEntity.getName());
		vehicleSubCategory.setFuelType(vehicleSubCategoryEntity.getFuelType());
		vehicleSubCategory.setAverageMileage(vehicleSubCategoryEntity.getAverageMileage());
		return vehicleSubCategory;
	}

	@Override
	public VehicleSubCategory getDomainModelChild(VehicleSubCategory vehicleSubCategory, VehicleSubCategoryEntity vehicleSubCategoryEntity){
		return vehicleSubCategory;
	}

	@Override
	public Collection<VehicleSubCategory> getDomainModels(Collection<VehicleSubCategory> vehicleSubCategories, 
			Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities, boolean fetchChild){
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
			vehicleSubCategory = getDomainModel(vehicleSubCategoryEntity, fetchChild);
			vehicleSubCategories.add(vehicleSubCategory);
		}
		return vehicleSubCategories;
	}

	@Override
	public Collection<VehicleSubCategoryEntity> getEntities(Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities, 
			Collection<VehicleSubCategory> vehicleSubCategories, boolean fetchChild){
		for (VehicleSubCategory vehicleSubCategory : vehicleSubCategories) {
			vehicleSubCategoryEntities.add(getEntity(vehicleSubCategory,fetchChild));
		}
		return vehicleSubCategoryEntities;
	}

}
