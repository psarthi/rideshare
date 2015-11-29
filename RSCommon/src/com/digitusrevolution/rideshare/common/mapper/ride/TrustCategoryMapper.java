package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryMapper {

	public TrustCategory getTrustCategory(TrustCategoryEntity trustCategoryEntity){
		TrustCategory trustCategory = new TrustCategory();
		trustCategory.setName(trustCategoryEntity.getName());
		return trustCategory;		
	}

	public TrustCategoryEntity getTrustCategoryEntity(TrustCategory trustCategory){
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity.setName(trustCategory.getName());
		return trustCategoryEntity;
	}

	public Collection<TrustCategoryEntity> getTrustCategoryEntities(Collection<TrustCategory> trustCategories){

		Collection<TrustCategoryEntity> trustCategoryEntities = new ArrayList<>();

		for (TrustCategory trustCategory : trustCategories) {
			trustCategoryEntities.add(getTrustCategoryEntity(trustCategory));
		}
		return trustCategoryEntities;
	}

	public Collection<TrustCategory> getTrustCategories(Collection<TrustCategoryEntity> trustCategoryEntities){

		Collection<TrustCategory> trustCategories = new ArrayList<>();

		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			trustCategories.add(getTrustCategory(trustCategoryEntity));
		}
		
		return trustCategories;
	}

}
