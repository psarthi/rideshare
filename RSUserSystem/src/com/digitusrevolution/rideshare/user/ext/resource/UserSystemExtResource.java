package com.digitusrevolution.rideshare.user.ext.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.ext.service.UserSystemExtService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserSystemExtResource {
	
	private UserSystemExtService userSystemExtService = new UserSystemExtService();
	
	@GET
	@Path("/{userId}")
	public User getUser(@PathParam("userId") int userId){
		System.out.println("Web Service - getUser");
		return userSystemExtService.getUser(userId);
	}
	
	@POST
	public void createUser(User user){
		userSystemExtService.createUser(user);
	}

}
