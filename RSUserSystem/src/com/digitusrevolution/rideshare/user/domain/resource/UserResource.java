package com.digitusrevolution.rideshare.user.domain.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.UserService;


@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private UserService userService = new UserService();
	
	@GET
	@Path("/{userId}")
	public Response getUser(@PathParam("userId") int userId){
		User user = userService.getUser(userId);
		return Response.ok(user).build();
	}
	
	@GET
	public Response getAllUser(){
		List<User> users = userService.getAllUser();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();	
	}

	@POST
	public Response createUser(User user){
		User createdUser = userService.createUser(user);
		return Response.ok(createdUser).build();
	}

}
