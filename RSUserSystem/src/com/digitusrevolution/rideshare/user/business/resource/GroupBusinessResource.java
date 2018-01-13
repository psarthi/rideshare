package com.digitusrevolution.rideshare.user.business.resource;

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

import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.user.business.GroupBusinessService;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupBusinessResource {
	
	/**
	 * 
	 * This function should be accessed via UserBusinessResource and not directly as this require userId along with Vehicle
	 * 
	 * @param userId Id of the user
	 * @param group Group to be created
	 * @return status OK
	 */
	@POST
	@Path("/create")
	public Response createGroup(BasicGroup group){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		int id = groupBusinessService.createGroup(group);
		//Since we are trying to get all data before even committing, all child objects may not come 
		//so its cleaner to have get All updated data post commit in different transaction
		GroupDetail createdGroup = groupBusinessService.getGroupDetails(id);
		return Response.ok().entity(createdGroup).build();
	}

	@GET
	@Path("/{id}")
	public Response getGroupDetails(@PathParam("id") int groupId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		GroupDetail group = groupBusinessService.getGroupDetails(groupId);
		return Response.ok().entity(group).build();
	}

	@GET
	@Path("/{id}/members")
	public Response getMembers(@PathParam("id") int groupId, @QueryParam("page") int page) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<BasicUser> users = groupBusinessService.getMembers(groupId, page);
		GenericEntity<List<BasicUser>> entity = new GenericEntity<List<BasicUser>>(users) {};
		return Response.ok(entity).build();
	}

}
