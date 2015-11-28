package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;

public class RoleDAO extends GenericDAOImpl<RoleEntity,String>{

	public RoleDAO() {
		super(RoleEntity.class);
	}
}
