package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;

public class VehicleCategoryMapper implements Mapper<VehicleCategory, VehicleCategoryEntity>{

	@Override
	public VehicleCategoryEntity getEntity(VehicleCategory vehicleCategory, boolean fetchChild){
		VehicleCategoryEntity vehicleCategoryEntity = new VehicleCategoryEntity();
		vehicleCategoryEntity.setId(vehicleCategory.getId());
		vehicleCategoryEntity.setName(vehicleCategory.getName());

		if (fetchChild){
			vehicleCategoryEntity = getEntityChild(vehicleCategory, vehicleCategoryEntity);			
		}
		
		return vehicleCategoryEntity;
	}

	@Override
	public VehicleCategoryEntity getEntityChild(VehicleCategory vehicleCategory, VehicleCategoryEntity vehicleCategoryEntity){		
		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		vehicleCategoryEntity.setSubCategories(vehicleSubCategoryMapper.getEntities(vehicleCategoryEntity.getSubCategories(), 
				vehicleCategory.getSubCategories(), true));		

		return vehicleCategoryEntity;
	}


	@Override
	public VehicleCategory getDomainModel(VehicleCategoryEntity vehicleCategoryEntity, boolean fetchChild){
		VehicleCategory vehicleCategory = new VehicleCategory();
		vehicleCategory.setId(vehicleCategoryEntity.getId());
		vehicleCategory.setName(vehicleCategoryEntity.getName());
		
		if (fetchChild){
			vehicleCategory = getDomainModelChild(vehicleCategory, vehicleCategoryEntity);
		}

		return vehicleCategory;
	}

	@Override
	public VehicleCategory getDomainModelChild(VehicleCategory vehicleCategory, VehicleCategoryEntity vehicleCategoryEntity){

		VehicleSubCategoryMapper vehicleSubCategoryMapper = new VehicleSubCategoryMapper();
		vehicleCategory.setSubCategories(vehicleSubCategoryMapper.getDomainModels(vehicleCategory.getSubCategories(), 
				vehicleCategoryEntity.getSubCategories(), true));		

		return vehicleCategory;
	}

	@Override
	public Collection<VehicleCategory> getDomainModels(Collection<VehicleCategory> models,
			Collection<VehicleCategoryEntity> entities, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategoryEntity> getEntities(Collection<VehicleCategoryEntity> entities,
			Collection<VehicleCategory> model, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}
}