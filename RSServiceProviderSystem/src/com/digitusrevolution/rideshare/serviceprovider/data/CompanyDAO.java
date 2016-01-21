package com.digitusrevolution.rideshare.serviceprovider.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;

public class CompanyDAO extends GenericDAOImpl<CompanyEntity, Integer>{

	public CompanyDAO() {
		super(CompanyEntity.class);
	}
}
