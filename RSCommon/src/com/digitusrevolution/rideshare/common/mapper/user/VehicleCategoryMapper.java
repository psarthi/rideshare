package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;

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
		vehicleCategoryEntity.setSubCategories(vehicleSubCategoryMapper.getEntities(vehicleCategoryEntity.getSubCategories(), 
				vehicleCategory.getSubCategories()));		

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
		vehicleCategory.setSubCategories(vehicleSubCategoryMapper.getDomainModels(vehicleCategory.getSubCategories(), 
				vehicleCategoryEntity.getSubCategories()));		

		return vehicleCategory;
	}

	@Override
	public Collection<VehicleCategory> getDomainModelsWithOnlyPK(Collection<VehicleCategory> models,
			Collection<VehicleCategoryEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategory> getDomainModels(Collection<VehicleCategory> models,
			Collection<VehicleCategoryEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategoryEntity> getEntitiesWithOnlyPK(Collection<VehicleCategoryEntity> entities,
			Collection<VehicleCategory> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<VehicleCategoryEntity> getEntities(Collection<VehicleCategoryEntity> entities,
			Collection<VehicleCategory> model) {
		// TODO Auto-generated method stub
		return null;
	}
}