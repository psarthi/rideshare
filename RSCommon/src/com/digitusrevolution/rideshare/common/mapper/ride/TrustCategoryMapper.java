package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryMapper implements Mapper<TrustCategory, TrustCategoryEntity>{

	@Override
	public TrustCategory getDomainModel(TrustCategoryEntity trustCategoryEntity, boolean fetchChild){
		TrustCategory trustCategory = new TrustCategory();
		trustCategory.setName(trustCategoryEntity.getName());
		return trustCategory;		
	}

	@Override
	public TrustCategoryEntity getEntity(TrustCategory trustCategory, boolean fetchChild){
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity.setName(trustCategory.getName());
		return trustCategoryEntity;
	}

	@Override
	public Collection<TrustCategoryEntity> getEntities(Collection<TrustCategoryEntity> trustCategoryEntities, 
			Collection<TrustCategory> trustCategories, boolean fetchChild){
		for (TrustCategory trustCategory : trustCategories) {
			trustCategoryEntities.add(getEntity(trustCategory, fetchChild));
		}
		return trustCategoryEntities;
	}

	@Override
	public Collection<TrustCategory> getDomainModels(
			Collection<TrustCategory> trustCategories, Collection<TrustCategoryEntity> trustCategoryEntities, boolean fetchChild){
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			TrustCategory trustCategory = new TrustCategory();
			trustCategory = getDomainModel(trustCategoryEntity, fetchChild);
			trustCategories.add(trustCategory);
		}		
		return trustCategories;
	}

	@Override
	public TrustCategoryEntity getEntityChild(TrustCategory trustCategory, TrustCategoryEntity trustCategoryEntity) {
		return trustCategoryEntity;
	}

	@Override
	public TrustCategory getDomainModelChild(TrustCategory trustCategory, TrustCategoryEntity trustCategoryEntity) {
		return trustCategory;
	}

}
