package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;

public class VehicleCategoryMapper implements Mapper<VehicleCategory, VehicleCategoryEntity>{

	@Override
	public VehicleCategoryEntity getEntityWithOnlyPK(VehicleCategory vehicleCategory) {
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity.setId(vehicleCategory.getId());
		return vehicleCategoryEntity;
	}

	@Override
	public VehicleCategoryEntity getEntity(VehicleCategory vehicleCategory){
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity = getEntityWithOnlyPK(vehicleCategory);
		vehicleCategoryEntity.setName(vehicleCategory.getName());
		
		vehicleCategoryEntity = getEntityChild(vehicleCategory, vehicleCategoryEntity);
		
		return vehicleCategoryEntity;
	}
	
	@Override
	public VehicleCategoryEntity getEntityChild(VehicleCategory vehicleCategory, VehicleCategoryEntity vehicleCategoryEntity){		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		Collection<VehicleSubCategory> vehicleSubCategories = vehicleCategory.getSubCategories();
		vehicleCategoryEntity.setSubCategories(vehicleSubCategoryMapper.getEntities(vehicleSubCategories));		
		
		return vehicleCategoryEntity;
	}

	@Override
	public VehicleCategory getDomainModelWithOnlyPK(VehicleCategoryEntity vehicleCategoryEntity) {
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setId(vehicleCategoryEntity.getId());
		return vehicleCategory;
	}

	@Override
	public VehicleCategory getDomainModel(VehicleCategoryEntity vehicleCategoryEntity){
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory = getDomainModelWithOnlyPK(vehicleCategoryEntity);
		vehicleCategory.setName(vehicleCategoryEntity.getName());
		
		return vehicleCategory;
	}
	
	@Override
	public VehicleCategory getDomainModelChild(VehicleCategory vehicleCategory, VehicleCategoryEntity vehicleCategoryEntity){
		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		Collection<VehicleSubCategoryEntity> vehicleSubCategoryEntities = vehicleCategoryEntity.getSubCategories();
		vehicleCategory.setSubCategories(vehicleSubCategoryMapper.getDomainModels(vehicleSubCategoryEntities));		
		
		return vehicleCategory;
	}

	@Override
	public Collection<VehicleCategory> getDomainModels(Collection<VehicleCategoryEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategoryEntity> getEntities(Collection<VehicleCategory> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategory> getDomainModelsWithOnlyPK(Collection<VehicleCategoryEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategoryEntity> getEntitiesWithOnlyPK(Collection<VehicleCategory> model) {
		// TODO Auto-generated method stub
		return null;
	}
}