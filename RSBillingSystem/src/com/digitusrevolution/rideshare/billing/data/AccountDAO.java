package com.digitusrevolution.rideshare.billing.data;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;

public class AccountDAO extends GenericDAOImpl<AccountEntity, Integer>{

	private static final Class<AccountEntity> entityClass = AccountEntity.class;

	public AccountDAO() {
		super(entityClass);
	}


}
