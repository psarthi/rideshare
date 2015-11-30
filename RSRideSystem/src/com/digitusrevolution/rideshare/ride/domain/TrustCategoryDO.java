package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustCategoryMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryDO implements DomainObject{
	
	private TrustCategory trustCategory;
	private TrustCategoryEntity trustCategoryEntity;
	private TrustCategoryMapper trustCategoryMapper;
	
	public TrustCategoryDO() {
		trustCategory = new TrustCategory();
		trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryMapper = new TrustCategoryMapper();
	}
	
	public TrustCategory getTrustCategory() {
		return trustCategory;
	}

	public void setTrustCategory(TrustCategory trustCategory) {
		this.trustCategory = trustCategory;
		trustCategoryEntity = trustCategoryMapper.getEntity(trustCategory);
	}

	public TrustCategoryEntity getTrustCategoryEntity() {
		return trustCategoryEntity;
	}

	public void setTrustCategoryEntity(TrustCategoryEntity trustCategoryEntity) {
		this.trustCategoryEntity = trustCategoryEntity;
		trustCategory = trustCategoryMapper.getDomainModel(trustCategoryEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

}
