package com.digitusrevolution.rideshare.ride.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;

public class RideDAO extends GenericDAOImpl<RideEntity, Integer>{

	public RideDAO() {
		super(RideEntity.class);
	}
	

}
