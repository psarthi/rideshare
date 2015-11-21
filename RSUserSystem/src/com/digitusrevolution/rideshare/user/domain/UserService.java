package com.digitusrevolution.rideshare.user.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.UserDAO;

public class UserService {
	
	private UserDAO userDAO;
	private static final Logger logger = LogManager.getLogger(UserService.class.getName());
	
	public UserService(){
		userDAO = new UserDAO();
	}

	public void createUser(User user){
		logger.entry();
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		userDAO.create(userDO.getUserEntity());
		logger.exit();
	}

	public boolean checkUserExist(String userEmail){
		userDAO.getUserByEmail(userEmail);
		return false;
	}
	
	public User getUser(int userId){
		UserEntity userEntity = new UserEntity();
		UserDO userDO = new UserDO();
		userEntity = userDAO.get(userId);
		userDO.setUserEntity(userEntity);
		return userDO.getUser();
	}
	
}
