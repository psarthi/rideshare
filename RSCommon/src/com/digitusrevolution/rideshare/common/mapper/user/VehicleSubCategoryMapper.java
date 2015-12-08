package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;
import java.util.LinkedList;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleSubCategoryMapper implements Mapper<VehicleSubCategory, VehicleSubCategoryEntity>{

	@Override
	public VehicleSubCategoryEntity getEntityWithOnlyPK(VehicleSubCategory vehicleSubCategory) {
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity.setId(vehicleSubCategory.getId());
		return vehicleSubCategoryEntity;
	}

	@Override
	public VehicleSubCategoryEntity getEntity(VehicleSubCategory vehicleSubCategory){
		VehicleSubCategoryEntity vehicleSubCategoryEntity = new VehicleSubCategoryEntity();
		vehicleSubCategoryEntity = getEntityWithOnlyPK(vehicleSubCategory);
		vehicleSubCategoryEntity.setAirConditioner(vehicleSubCategory.getAirConditioner());
		vehicleSubCategoryEntity.setName(vehicleSubCategory.getName());
		return vehicleSubCategoryEntity;
	}

	@Override
	public VehicleSubCategoryEntity getEntityChild(VehicleSubCategory vehicleSubCategory, VehicleSubCategoryEntity vehicleSubCategoryEntity){	
		return null;
	}

	@Override
	public VehicleSubCategory getDomainModelWithOnlyPK(VehicleSubCategoryEntity vehicleSubCategoryEntity) {
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory.setId(vehicleSubCategoryEntity.getId());
		return vehicleSubCategory;
	}

	@Override
	public VehicleSubCategory getDomainModel(VehicleSubCategoryEntity vehicleSubCategoryEntity){
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		vehicleSubCategory = getDomainModelWithOnlyPK(vehicleSubCategoryEntity);
		vehicleSubCategory.setAirConditioner(vehicleSubCategoryEntity.getAirConditioner());
		vehicleSubCategory.setName(vehicleSubCategoryEntity.getName());
		return vehicleSubCategory;
	}

	@Override
	public VehicleSubCategory getDomainModelChild(VehicleSubCategory vehicleSubCategory, VehicleSubCategoryEntity vehicleSubCategoryEntity){
		return null;
	}

	@Override
	public Collection<VehicleSubCategory> getDomainModels(Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities){
		Collection<VehicleSubCategory> vehicleSubCategories = new LinkedList<>();
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			vehicleSubCategory = getDomainModel(vehicleSubCategoryEntity);
			vehicleSubCategories.add(vehicleSubCategory);
		}
		return vehicleSubCategories;
	}

	@Override
	public Collection<VehicleSubCategory> getDomainModelsWithOnlyPK(Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities) {
		Collection<VehicleSubCategory> vehicleSubCategories = new LinkedList<>();
		VehicleSubCategory vehicleSubCategory = new VehicleSubCategory();
		for (VehicleSubCategoryEntity vehicleSubCategoryEntity : vehicleSubCategoryEntities) {
			vehicleSubCategory = getDomainModelWithOnlyPK(vehicleSubCategoryEntity);
			vehicleSubCategories.add(vehicleSubCategory);
		}
		return vehicleSubCategories;
	}

	@Override
	public Collection<VehicleSubCategoryEntity> getEntities(Collection<VehicleSubCategory> vehicleSubCategories){
		Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities = new LinkedList<>();
		for (VehicleSubCategory vehicleSubCategory : vehicleSubCategories) {
			vehicleSubCategoryEntities.add(getEntity(vehicleSubCategory));
		}
		return vehicleSubCategoryEntities;
	}

	@Override
	public Collection<VehicleSubCategoryEntity> getEntitiesWithOnlyPK(Collection<VehicleSubCategory> vehicleSubCategories) {
		Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities = new LinkedList<>();
		for (VehicleSubCategory vehicleSubCategory : vehicleSubCategories) {
			vehicleSubCategoryEntities.add(getEntityWithOnlyPK(vehicleSubCategory));
		}
		return vehicleSubCategoryEntities;
	}

}
