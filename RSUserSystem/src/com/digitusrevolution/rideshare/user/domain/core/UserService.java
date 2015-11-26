package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.UserDAO;

public class UserService {
	
	private static final Logger logger = LogManager.getLogger(UserService.class.getName());
	
	public UserService(){
	}

	public User createUser(User user){
		logger.entry();
		UserDO userDO = new UserDO();
		UserDAO userDAO = new UserDAO();
		userDO.setUser(user);
		int id = userDAO.create(userDO.getUserEntity());
		user.setId(id);
		logger.exit();
		return user;
	}

	public boolean checkUserExist(String userEmail){
		UserDAO userDAO = new UserDAO();
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}
	
	public User getUser(int userId){
		UserDAO userDAO = new UserDAO();
		UserEntity userEntity = new UserEntity();
		UserDO userDO = new UserDO();
		userEntity = userDAO.get(userId);
		if (userEntity == null){
			throw new NotFoundException("No User found with id: "+userId);
		}
		userDO.setUserEntity(userEntity);
		return userDO.getUser();
	}
	
	public User getUserFullDetail(int userId){
		
	 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		UserDAO userDAO = new UserDAO();
		UserEntity userEntity = new UserEntity();
		UserDO userDO = new UserDO();
		userEntity = userDAO.get(userId);
		if (userEntity == null){
			throw new NotFoundException("No User found with id: "+userId);
		}
		userDO.setUserEntity(userEntity);		
		userDO.mapChildDataModelToDomainModel();
		return userDO.getUser();
	}
	
	public List<User> getAllUser(){
		UserDAO userDAO = new UserDAO();
		List<UserEntity> userEntities = new ArrayList<>();
		List<User> users = new ArrayList<>();
		userEntities = userDAO.getAll();
		for (UserEntity userEntity : userEntities) {
			UserDO userDO = new UserDO();
			userDO.setUserEntity(userEntity);
			users.add(userDO.getUser());
		}
		return users;
	}
	
	public void updateUser(User user){
		UserDAO userDAO = new UserDAO();
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		userDO.mapChildDomainModelToDataModel();
		userDAO.update(userDO.getUserEntity());
	}
}
