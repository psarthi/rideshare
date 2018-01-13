package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.FullGroup;
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
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		FullGroup createdGroup = groupBusinessService.getGroup(id);
		return Response.ok().entity(createdGroup).build();
	}

	@GET
	@Path("/{id}")
	public Response getGroup(@PathParam("id") int groupId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		FullGroup group = groupBusinessService.getGroup(groupId);
		return Response.ok().entity(group).build();
	}

}
