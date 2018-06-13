package com.digitusrevolution.rideshare.serviceprovider.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.PartnerEntity;

public class PartnerDAO extends GenericDAOImpl<PartnerEntity, Integer>{

	public PartnerDAO() {
		super(PartnerEntity.class);
	}
}
