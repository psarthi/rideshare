package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;

public class VehicleDAO extends GenericDAOImpl<VehicleEntity, Long>{

	public VehicleDAO() {
		super(VehicleEntity.class);
	}

}
