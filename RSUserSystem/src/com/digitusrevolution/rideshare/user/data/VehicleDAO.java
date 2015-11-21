package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.core.VehicleEntity;

public class VehicleDAO extends GenericDAOImpl<VehicleEntity>{

	public VehicleDAO() {
		super(VehicleEntity.class);
	}

}
