package com.digitusrevolution.rideshare.user.domain.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;

public class UserDO implements DomainObject{

	private User user;
	private UserEntity userEntity;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());
	private UserMapper userMapper;

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
		userMapper = new UserMapper();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		userEntity = userMapper.getEntity(user);
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		user = userMapper.getDomainModel(userEntity);
	}

	@Override
	public void fetchChild(){
		user = userMapper.getDomainModelChild(user, userEntity);
	}

	public void addVehicle(Vehicle vehicle){
		logger.entry();
		UserDomainService userDomainService = new UserDomainService();
		user.getVehicles().add(vehicle);
		userDomainService.update(user);
		logger.exit();
	}
}
