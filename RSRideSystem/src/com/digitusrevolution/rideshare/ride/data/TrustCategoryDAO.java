package com.digitusrevolution.rideshare.ride.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;

public class TrustCategoryDAO extends GenericDAOImpl<TrustCategoryEntity, String> {
	
	public TrustCategoryDAO() {
		super(TrustCategoryEntity.class);
	}

}
