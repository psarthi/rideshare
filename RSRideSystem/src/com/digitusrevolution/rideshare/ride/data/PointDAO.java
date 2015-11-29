package com.digitusrevolution.rideshare.ride.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;

public class PointDAO extends GenericDAOImpl<PointEntity, Integer> {

	public PointDAO() {
		super(PointEntity.class);
	}

}
