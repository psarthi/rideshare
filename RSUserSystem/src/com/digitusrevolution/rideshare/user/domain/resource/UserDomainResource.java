package com.digitusrevolution.rideshare.user.domain.resource;

import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.inf.DomainResourceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.user.domain.service.UserDomainService;

@Path("/domain/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserDomainResource implements DomainResourceLong<User>{


	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") long id, @QueryParam("fetchChild") String fetchChild){
		
		UserDomainService userDomainService = new UserDomainService();
		User user = userDomainService.get(id, Boolean.valueOf(fetchChild));
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

	/**
	 * 
	 * @param id Id of the user
	 * @return collection of roles
	 */
	@GET
	@Path("/{id}/roles")
	public Response getRoles(@PathParam("id") long id){
		UserDomainService userDomainService = new UserDomainService();
		Collection<Role> roles = userDomainService.getRoles(id);
		GenericEntity<Collection<Role>> entity = new GenericEntity<Collection<Role>>(roles) {};
		return Response.ok(entity).build();
	}
	
	/**
	 * This will get VehcileDomainResource Object which can be accessed via UserDomainResource 
	 * and your path for all VehcileDomainResources should have prefix of this function path
	 * 
	 * @return VehicleDomainResource object
	 */
	@Path("/{id}/vehicles")
	public VehicleDomainResource getVehicleDomainResource(){
		return new VehicleDomainResource();
	}
	
	@GET
	@Path("/{id}/groups")
	public Response getGroups(@PathParam("id") long userId){
		UserDomainService userDomainService = new UserDomainService();
		List<GroupDetail> groups = userDomainService.getGroups(userId);
		GenericEntity<List<GroupDetail>> entity = new GenericEntity<List<GroupDetail>>(groups) {};
		return Response.ok(entity).build();
	}
}
