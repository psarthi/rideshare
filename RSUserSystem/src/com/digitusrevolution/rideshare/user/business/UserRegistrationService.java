package com.digitusrevolution.rideshare.user.business;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.UserService;
import com.digitusrevolution.rideshare.user.exception.EmailExist;

public class UserRegistrationService {
	
	public int registerUser(User user){
		
		boolean userExist;
		UserService userService = new UserService();
		userExist = userService.checkUserExist(user.getEmail());
		if (userExist){
			throw new EmailExist("Email id already exist :"+user.getEmail());					
		} else {
			return userService.createUser(user);
		}
	}

}
