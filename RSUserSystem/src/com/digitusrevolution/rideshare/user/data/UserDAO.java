package com.digitusrevolution.rideshare.user.data;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public class UserDAO extends GenericDAOImpl<UserEntity>{

	public UserDAO() {
		super(UserEntity.class);
	}

	public UserEntity getUserByEmail(String email){
		
		return null;		
	}
	
}
