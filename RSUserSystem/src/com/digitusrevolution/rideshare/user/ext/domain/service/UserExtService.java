package com.digitusrevolution.rideshare.user.ext.domain.service;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.UserService;

public class UserExtService {

	private UserService userService = new UserService();

	public User getUser(int userId){
		
		return userService.getUser(userId);
	}
	
	public void createUser(User user){
		
		userService.createUser(user);
	}
}
