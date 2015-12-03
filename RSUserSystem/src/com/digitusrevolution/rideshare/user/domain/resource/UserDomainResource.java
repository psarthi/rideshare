package com.digitusrevolution.rideshare.user.domain.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.service.UserDomainService;

@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserDomainResource{


	@PUT
	public Response create(User user){	
		UserDomainService userDomainService = new UserDomainService();
		int id = userDomainService.create(user);
		return Response.ok(Integer.toString(id)).build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild){
		
		UserDomainService userDomainService = new UserDomainService();
		User user = userDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(user).build();
	}
	

	@GET
	public Response getAll(){
		
		UserDomainService userDomainService = new UserDomainService();
		List<User> users = userDomainService.getAll();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();
	}

	@POST
	public Response update(User user){
		UserDomainService userDomainService = new UserDomainService();
		userDomainService.update(user);
		return Response.ok().build();
	}
	
	@DELETE
	public Response delete(User user){
		UserDomainService userDomainService = new UserDomainService();
		userDomainService.delete(user);
		return Response.ok().build();
	}

}
