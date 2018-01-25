package com.digitusrevolution.rideshare.user.domain.resource;

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

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.inf.DomainResourceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.user.domain.service.GroupDomainService;

@Path("/domain/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupDomainResource implements DomainResourceLong<Group>{

	@Override
	@Secured
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") long id, @QueryParam("fetchChild") String fetchChild){
		GroupDomainService groupDomainService = new GroupDomainService();
		Group group = groupDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(group).build();
	}
	

	@Override
	@Secured
	@GET
	public Response getAll(){	
		GroupDomainService groupDomainService = new GroupDomainService();
		List<Group> groups = groupDomainService.getAll();
		GenericEntity<List<Group>> entity = new GenericEntity<List<Group>>(groups) {};
		return Response.ok(entity).build();
	}

}
