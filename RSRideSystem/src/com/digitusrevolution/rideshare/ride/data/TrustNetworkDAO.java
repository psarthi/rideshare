package com.digitusrevolution.rideshare.ride.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;

public class TrustNetworkDAO extends GenericDAOImpl<TrustNetworkEntity, Integer>{
	
	public TrustNetworkDAO() {
		super(TrustNetworkEntity.class);
	}

}
