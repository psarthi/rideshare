package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

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

	public User createUser(User user){
		logger.entry();
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		int id = userDAO.create(userDO.getUserEntity());
		user.setId(id);
		logger.exit();
		return user;
	}

	public boolean checkUserExist(String userEmail){
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}
	
	public User getUser(int userId){
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

		UserEntity userEntity = new UserEntity();
		UserDO userDO = new UserDO();
		userEntity = userDAO.get(userId);
		if (userEntity == null){
			throw new NotFoundException("No User found with id: "+userId);
		}
		userDO.setUserEntity(userEntity);
		userDO.mapVehicleDataModelToDomainModel();
		return userDO.getUser();

	}
	
	public List<User> getAllUser(){
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
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		userDO.mapVehicleDomainModelToDataModel();
		userDAO.update(userDO.getUserEntity());
	}

	
}
