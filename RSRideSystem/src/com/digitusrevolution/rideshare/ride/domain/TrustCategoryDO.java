package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryDO implements DomainObject{
	
	TrustCategory trustCategory;
	TrustCategoryEntity trustCategoryEntity;
	
	public TrustCategoryDO() {
		trustCategory = new TrustCategory();
		trustCategoryEntity = new TrustCategoryEntity();
	}
	
	public TrustCategory getTrustCategory() {
		return trustCategory;
	}



	public void setTrustCategory(TrustCategory trustCategory) {
		this.trustCategory = trustCategory;
		mapDomainModelToDataModel();
	}



	public TrustCategoryEntity getTrustCategoryEntity() {
		return trustCategoryEntity;
	}



	public void setTrustCategoryEntity(TrustCategoryEntity trustCategoryEntity) {
		this.trustCategoryEntity = trustCategoryEntity;
		mapDataModelToDomainModel();
	}



	@Override
	public void mapDomainModelToDataModel() {
		trustCategoryEntity.setName(trustCategory.getName());
		
	}

	@Override
	public void mapDataModelToDomainModel() {
		trustCategory.setName(trustCategoryEntity.getName());
		
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}

}
