package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.business.UserRegistrationService;

@Path("/business/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserRegistrationResource {
	
	private UserRegistrationService userRegistrationService = new UserRegistrationService();  
	
	@POST
	public Response registerUser(User user){
		user = userRegistrationService.registerUser(user);
		return Response.ok().entity(user).build();
	}
	
	@Path("/{userId}/vehicles")
	public VehicleRegistrationResource getVehicleRegistrationResource(){
		return new VehicleRegistrationResource();
	}
}
