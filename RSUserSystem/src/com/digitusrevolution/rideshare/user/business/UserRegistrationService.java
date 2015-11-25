package com.digitusrevolution.rideshare.user.business;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.UserService;
import com.digitusrevolution.rideshare.user.exception.EmailExistException;

public class UserRegistrationService {
	
	public User registerUser(User user){
		
		boolean userExist;
		UserService userService = new UserService();
		userExist = userService.checkUserExist(user.getEmail());
		if (userExist){
			throw new EmailExistException("Email id already exist :"+user.getEmail());					
		} else {
			return userService.createUser(user);
		}
	}

}
