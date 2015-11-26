package com.digitusrevolution.rideshare.user.business.resource;

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
import com.digitusrevolution.rideshare.user.business.UserBusinessService;


@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private UserBusinessService userBusinessService = new UserBusinessService();
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id){
		User user = userBusinessService.get(id);
		return Response.ok(user).build();
	}
	
	@GET
	@Path("/child/{id}")
	public Response getChild(@PathParam("id") int id){
		User user = userBusinessService.getChild(id);
		return Response.ok(user).build();
	}

	
	@GET
	public Response getAll(){
		List<User> users = userBusinessService.getAll();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();	
	}

	@POST
	public Response create(User user){
		User createdUser = userBusinessService.create(user);
		return Response.ok(createdUser).build();
	}

}
