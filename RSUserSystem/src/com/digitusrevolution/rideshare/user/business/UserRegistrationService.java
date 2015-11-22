package com.digitusrevolution.rideshare.user.business;

import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.UserService;

public class UserRegistrationService {
	
	public Response registerUser(User user){
		
		boolean userExist;
		UserService userService = new UserService();
		userExist = userService.checkUserExist(user.getEmail());
		if (userExist){
			return Response.serverError().entity("Email id already exist").build();						
		} else {
			userService.createUser(user);
			return Response.ok().build();
		}		
	}

}
