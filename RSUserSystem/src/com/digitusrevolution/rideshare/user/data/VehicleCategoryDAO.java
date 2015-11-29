package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;

public class VehicleCategoryDAO extends GenericDAOImpl<VehicleCategoryEntity, Integer>{

	public VehicleCategoryDAO() {
		super(VehicleCategoryEntity.class);
	}

}
