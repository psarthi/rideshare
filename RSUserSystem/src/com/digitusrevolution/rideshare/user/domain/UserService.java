package com.digitusrevolution.rideshare.user.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.impl.UserDAOImpl;
import com.digitusrevolution.rideshare.user.data.inf.UserDAO;

public class UserService {
	
	private UserDAO userDAO;
	private UserDO userDO;
	private static final Logger logger = LogManager.getLogger(UserService.class.getName());
	
	public UserService(){
		userDAO = new UserDAOImpl();
		userDO = new UserDO();
	}
	
	public UserDO getUserDO() {
		return userDO;
	}

	public void setUserDO(UserDO userDO) {
		this.userDO = userDO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}
	
	public void createUser(User user){
		logger.entry();
		userDO.setUser(user);
		userDAO.createUser(userDO.getUserEntity());
		logger.debug("Creted User - " + userDO.getUserEntity().getId()+","+userDO.getUserEntity().getEmail());
		logger.exit();
	}

	public boolean checkUserExist(String userEmail){
		userDAO.getUserByEmail(userEmail);
		return false;
	}
	
	public User getUser(int userId){
		User user = new User();
		user.setId(userId);
		user.setFirstName("Partha");
		user.setLastName("Sarthi");
		user.setEmail("partha.sarthi@gmail.com");
		return user;		
	}
	
}
