package com.digitusrevolution.rideshare.billing.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.billing.data.core.BillEntity;

public class BillDAO extends GenericDAOImpl<BillEntity, Integer>{

	public BillDAO() {
		super(BillEntity.class);
	}

}
