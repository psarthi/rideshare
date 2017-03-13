package com.digitusrevolution.rideshare.user.domain.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.dto.UserDTO;

@Path("/domain/usersystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserSystemDomainResource {

	@GET
	@Path("/model")
	public Response getModel(){
		UserDTO model = new UserDTO();
		return Response.ok().entity(model).build();
	}
}
