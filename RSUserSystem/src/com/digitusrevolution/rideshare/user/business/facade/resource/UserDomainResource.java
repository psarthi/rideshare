package com.digitusrevolution.rideshare.user.business.facade.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.business.facade.UserDomainFacade;


@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserDomainResource {
	
	private UserDomainFacade userBusinessService = new UserDomainFacade();
	
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild){
		if (Boolean.valueOf(fetchChild)){
			User user = userBusinessService.getChild(id);
			return Response.ok(user).build();
		} else {
			User user = userBusinessService.get(id);
			return Response.ok(user).build();			
		}
	}
	
	@GET
	public Response getAll(){
		List<User> users = userBusinessService.getAll();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();	
	}

	@POST
	public Response create(User user){
		int id = userBusinessService.create(user);
		return Response.ok(Integer.toString(id)).build();
	}

}
