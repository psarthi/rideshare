package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;

public class CityDAO extends GenericDAOImpl<CityEntity,Integer>{
	
	public CityDAO() {
		super(CityEntity.class);
	}

}
