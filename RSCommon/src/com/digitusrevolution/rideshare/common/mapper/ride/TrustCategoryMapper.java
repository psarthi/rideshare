package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryMapper implements Mapper<TrustCategory, TrustCategoryEntity>{

	@Override
	public TrustCategory getDomainModelWithOnlyPK(TrustCategoryEntity trustCategoryEntity) {
		TrustCategory trustCategory = new TrustCategory();
		trustCategory.setName(trustCategoryEntity.getName());
		return trustCategory;
	}

	@Override
	public TrustCategory getDomainModel(TrustCategoryEntity trustCategoryEntity){
		TrustCategory trustCategory = new TrustCategory();
		trustCategory = getDomainModelWithOnlyPK(trustCategoryEntity);
		return trustCategory;		
	}

	@Override
	public TrustCategoryEntity getEntityWithOnlyPK(TrustCategory trustCategory) {
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity.setName(trustCategory.getName());
		return trustCategoryEntity;
	}

	@Override
	public TrustCategoryEntity getEntity(TrustCategory trustCategory){
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity = getEntityWithOnlyPK(trustCategory);
		return trustCategoryEntity;
	}

	@Override
	public Collection<TrustCategoryEntity> getEntities(Collection<TrustCategory> trustCategories){
		Collection<TrustCategoryEntity> trustCategoryEntities = new ArrayList<>();
		for (TrustCategory trustCategory : trustCategories) {
			trustCategoryEntities.add(getEntity(trustCategory));
		}
		return trustCategoryEntities;
	}

	@Override
	public Collection<TrustCategoryEntity> getEntitiesWithOnlyPK(Collection<TrustCategory> trustCategories) {
		Collection<TrustCategoryEntity> trustCategoryEntities = new ArrayList<>();
		for (TrustCategory trustCategory : trustCategories) {
			trustCategoryEntities.add(getEntityWithOnlyPK(trustCategory));
		}
		return trustCategoryEntities;
	}

	@Override
	public Collection<TrustCategory> getDomainModels(Collection<TrustCategoryEntity> trustCategoryEntities){
		Collection<TrustCategory> trustCategories = new ArrayList<>();
		TrustCategory trustCategory = new TrustCategory();
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			trustCategory = getDomainModel(trustCategoryEntity);
			trustCategories.add(trustCategory);
		}		
		return trustCategories;
	}
	
	@Override
	public Collection<TrustCategory> getDomainModelsWithOnlyPK(Collection<TrustCategoryEntity> trustCategoryEntities) {
		Collection<TrustCategory> trustCategories = new ArrayList<>();
		TrustCategory trustCategory = new TrustCategory();
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			trustCategory = getDomainModelWithOnlyPK(trustCategoryEntity);
			trustCategories.add(trustCategory);
		}		
		return trustCategories;
	}

	@Override
	public TrustCategoryEntity getEntityChild(TrustCategory model, TrustCategoryEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TrustCategory getDomainModelChild(TrustCategory model, TrustCategoryEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
