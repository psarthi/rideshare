package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRegistrationResource {

	@POST
	public Response registerUser(User user){

		UserRegistrationService userRegistrationService = new UserRegistrationService();
		int id = userRegistrationService.registerUser(user);
		return Response.ok().entity(Integer.toString(id)).build();

	}
}
