package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.UserDAO;

public class UserDomainService implements DomainService<User> {
	
	private static final Logger logger = LogManager.getLogger(UserDomainService.class.getName());
	private final UserDAO userDAO;
	
	public UserDomainService() {
		userDAO = new UserDAO();
	}

	public boolean isExist(String userEmail){
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}
	
	public Collection<Role> getRoles(int id){
		User user = getChild(id);
		logger.debug("Role size: "+user.getRoles().size());
		return user.getRoles();
	}
	
	@Override
	public int create(User user){
		logger.entry();
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		int id = userDAO.create(userDO.getUserEntity());
		logger.exit();
		return id;
	}

	@Override
	public User get(int id){
		UserDO userDO = new UserDO();
		UserEntity userEntity = new UserEntity();
		userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		userDO.setUserEntity(userEntity);
		return userDO.getUser();
	}
	
	@Override
	public User getChild(int id){
		
	 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		UserDO userDO = new UserDO();
		UserEntity userEntity = new UserEntity();
		userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		userDO.setUserEntity(userEntity);		
		userDO.fetchChild();
		return userDO.getUser();
	}
	
	@Override
	public List<User> getAll(){
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
	
	@Override
	public void update(User user){
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		userDAO.update(userDO.getUserEntity());
	}
	
	@Override
	public void delete(User user){
		UserDO userDO = new UserDO();
		userDO.setUser(user);
		userDAO.delete(userDO.getUserEntity());
	}
}
