package com.digitusrevolution.rideshare.user.domain.resource;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.inf.DomainResource;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.domain.service.UserDomainService;

@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserDomainResource implements DomainResource<User>{


	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id){
		
		UserDomainService userDomainService = new UserDomainService();
		User user = userDomainService.get(id);
		return Response.ok(user).build();
	}
	

	@Override
	@GET
	public Response getAll(){
		
		UserDomainService userDomainService = new UserDomainService();
		List<User> users = userDomainService.getAll();
		GenericEntity<List<User>> entity = new GenericEntity<List<User>>(users) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("/{id}/roles")
	public Response getRoles(@PathParam("id") int id){
		UserDomainService userDomainService = new UserDomainService();
		Collection<Role> roles = userDomainService.getRoles(id);
		GenericEntity<Collection<Role>> entity = new GenericEntity<Collection<Role>>(roles) {};
		return Response.ok(entity).build();
	}
	
	@Path("/{id}/vehicles")
	public VehicleDomainResource getVehicleDomainResource(){
		return new VehicleDomainResource();
	}
}
