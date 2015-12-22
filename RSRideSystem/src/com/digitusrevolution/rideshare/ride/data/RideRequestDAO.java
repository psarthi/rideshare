package com.digitusrevolution.rideshare.ride.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;

public class RideRequestDAO extends GenericDAOImpl<RideRequestEntity, Integer>{

	public RideRequestDAO() {
		super(RideRequestEntity.class);
	}
	


}
