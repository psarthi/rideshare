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

import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroup;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupInviteUserSearchResult;
import com.digitusrevolution.rideshare.model.user.dto.GroupListType;
import com.digitusrevolution.rideshare.model.user.dto.GroupMember;
import com.digitusrevolution.rideshare.user.business.GroupBusinessService;
import com.digitusrevolution.rideshare.user.business.UserBusinessService;

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
	public Response createGroup(BasicGroup group){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		int id = groupBusinessService.createGroup(group);
		//Since we are trying to get all data before even committing, all child objects may not come 
		//so its cleaner to have get All updated data post commit in different transaction
		GroupDetail createdGroup = groupBusinessService.getGroupDetails(id, group.getOwner().getId());
		return Response.ok().entity(createdGroup).build();
	}
	
	@GET
	public Response getGroups(@PathParam("userId") int userId, @QueryParam("listType") GroupListType listType, @QueryParam("page") int page) {
		UserBusinessService userBusinessService = new UserBusinessService();
		List<GroupDetail> groups = userBusinessService.getGroups(userId, listType, page);
		GenericEntity<List<GroupDetail>> entity = new GenericEntity<List<GroupDetail>>(groups) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("/{groupId}/members")
	public Response getMembers(@PathParam("groupId") int groupId, @QueryParam("page") int page) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupMember> members = groupBusinessService.getMembers(groupId, page);
		GenericEntity<List<GroupMember>> entity = new GenericEntity<List<GroupMember>>(members) {};
		return Response.ok(entity).build();
	}

	@GET
	@Path("/{groupId}")
	public Response getGroupDetails(@PathParam("userId") int userId, @PathParam("groupId") int groupId){
		//This is an exception where we are calling service from different resource
		//i.e. user business resource calling group business service and the reason 
		//is we need to capture user id as well as group id
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		GroupDetail group = groupBusinessService.getGroupDetails(groupId, userId);
		return Response.ok().entity(group).build();
	}
	
	@GET
	@Path("/{groupId}/searchuser")
	public Response searchUserByNameForGroupInvite(@PathParam("groupId") int groupId, @QueryParam("name") String name,
			@QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupInviteUserSearchResult> users = groupBusinessService.searchUserByNameForGroupInvite(groupId, name, page);
		GenericEntity<List<GroupInviteUserSearchResult>> entity = new GenericEntity<List<GroupInviteUserSearchResult>>(users) {};
		return Response.ok(entity).build();
	}
	
	@POST
	@Path("/{groupId}/invite")
	public Response inviteUsers(@PathParam("groupId") int groupId, List<Integer> userIds){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.inviteUsers(groupId, userIds);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(ResponseMessage.Code.OK);
		return Response.ok(responseMessage).build();
	}
	
	@GET
	@Path("/search")
	public Response searchGroupByName(@PathParam("userId") int userId, @QueryParam("name") String name, 
			@QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupDetail> groupDetails = groupBusinessService.searchGroupByName(userId, name, page);
		GenericEntity<List<GroupDetail>> entity = new GenericEntity<List<GroupDetail>>(groupDetails) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/{groupId}/membershiprequests")
	public Response getUserMembershipRequests(@PathParam("groupId") int groupId, @QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<BasicMembershipRequest> membershipRequests = groupBusinessService.getGroupMembershipRequests(groupId, page);
		GenericEntity<List<BasicMembershipRequest>> entity = new GenericEntity<List<BasicMembershipRequest>>(membershipRequests) {};
		return Response.ok(entity).build();
	}

}
