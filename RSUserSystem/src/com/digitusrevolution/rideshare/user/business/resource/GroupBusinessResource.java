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

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.model.common.ResponseMessage;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.dto.BasicGroupInfo;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupFeedbackInfo;
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
	@Secured
	@POST
	public Response createGroup(BasicGroupInfo group){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		long id = groupBusinessService.createGroup(group);
		//Since we are trying to get all data before even committing, all child objects may not come 
		//so its cleaner to have get All updated data post commit in different transaction
		GroupDetail createdGroup = groupBusinessService.getGroupDetail(id, group.getOwner().getId());
		return Response.ok().entity(createdGroup).build();
	}
	
	@Secured
	@POST
	@Path("/update")
	public Response updateGroup(BasicGroupInfo group) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.updateGroup(group);
		//Since we are trying to get all data before even committing, all child objects may not come 
		//so its cleaner to have get All updated data post commit in different transaction
		GroupDetail createdGroup = groupBusinessService.getGroupDetail(group.getId(), group.getOwner().getId());
		return Response.ok().entity(createdGroup).build();
	}
	
	@Secured
	@GET
	public Response getGroups(@PathParam("userId") long userId, @QueryParam("listType") GroupListType listType, @QueryParam("page") int page) {
		UserBusinessService userBusinessService = new UserBusinessService();
		List<GroupDetail> groups = userBusinessService.getGroups(userId, listType, page);
		GenericEntity<List<GroupDetail>> entity = new GenericEntity<List<GroupDetail>>(groups) {};
		return Response.ok(entity).build();
	}

	@Secured
	@GET
	@Path("/{groupId}/members")
	public Response getMembers(@PathParam("groupId") long groupId, @QueryParam("page") int page) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupMember> members = groupBusinessService.getMembers(groupId, page);
		GenericEntity<List<GroupMember>> entity = new GenericEntity<List<GroupMember>>(members) {};
		return Response.ok(entity).build();
	}

	@Secured
	@GET
	@Path("/{groupId}")
	public Response getGroupDetail(@PathParam("userId") long userId, @PathParam("groupId") long groupId){
		//This is an exception where we are calling service from different resource
		//i.e. user business resource calling group business service and the reason 
		//is we need to capture user id as well as group id
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		GroupDetail group = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(group).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/searchuser")
	public Response searchUserByNameForGroupInvite(@PathParam("groupId") long groupId, @QueryParam("name") String name,
			@QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupInviteUserSearchResult> users = groupBusinessService.searchUserByNameForGroupInvite(groupId, name, page);
		GenericEntity<List<GroupInviteUserSearchResult>> entity = new GenericEntity<List<GroupInviteUserSearchResult>>(users) {};
		return Response.ok(entity).build();
	}
	
	@Secured
	@POST
	@Path("/{groupId}/invite")
	public Response inviteUsers(@PathParam("groupId") long groupId, List<Long> userIds){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.inviteUsers(groupId, userIds);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(ResponseMessage.Code.OK);
		return Response.ok(responseMessage).build();
	}
	
	@Secured
	@GET
	@Path("/search")
	public Response searchGroupByName(@PathParam("userId") long userId, @QueryParam("name") String name, 
			@QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<GroupDetail> groupDetails = groupBusinessService.searchGroupByName(userId, name, page);
		GenericEntity<List<GroupDetail>> entity = new GenericEntity<List<GroupDetail>>(groupDetails) {};
		return Response.ok(entity).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/membershiprequests")
	public Response getUserMembershipRequests(@PathParam("groupId") long groupId, @QueryParam("page") int page){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		List<BasicMembershipRequest> membershipRequests = groupBusinessService.getGroupMembershipRequests(groupId, page);
		GenericEntity<List<BasicMembershipRequest>> entity = new GenericEntity<List<BasicMembershipRequest>>(membershipRequests) {};
		return Response.ok(entity).build();
	}
	
	@Secured
	@GET
	//Reason for changing it to request to avoid confusion / typos between membershiprequest(s)
	@Path("/{groupId}/request")
	public Response getMembershipRequest(@PathParam("groupId") long groupId, @PathParam("userId") long userId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		BasicMembershipRequest membershipRequest = groupBusinessService.getMembershipRequest(groupId, userId);
		return Response.ok().entity(membershipRequest).build();
	}
	
	@Secured
	@POST
	@Path("/{groupId}/request")
	public Response sendMembershipRequest(@PathParam("groupId") long groupId, @PathParam("userId") long userId, BasicMembershipRequest membershipRequest){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.sendMembershipRequest(groupId, membershipRequest);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(groupDetail).build();
	}

	@Secured
	@POST
	@Path("/{groupId}/approverequest/{requesterUserId}")
	public Response approveMembershipRequest(@PathParam("groupId") long groupId, @PathParam("userId") long userId,
			@PathParam("requesterUserId") long requesterUserId, String remark){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.approveMembershipRequest(groupId, requesterUserId, remark);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(groupDetail).build();
	}
	
	@Secured
	@POST
	@Path("/{groupId}/rejectrequest/{requesterUserId}")
	public Response rejectMembershipRequest(@PathParam("groupId") long groupId, @PathParam("userId") long userId,
			@PathParam("requesterUserId") long requesterUserId, String remark){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.rejectMembershipRequest(groupId, requesterUserId, remark);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(groupDetail).build();
	}
	
	@Secured
	@POST
	@Path("/{groupId}/feedback")
	public Response giveFeedback(@PathParam("groupId") long groupId, @PathParam("userId") long memberUserId, GroupFeedbackInfo feedback){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.giveFeedback(groupId, memberUserId, feedback);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, memberUserId);
		return Response.ok().entity(groupDetail).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/leave")
	public Response leaveGroup(@PathParam("groupId") long groupId, @PathParam("userId") long userId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.leaveGroup(groupId, userId);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(groupDetail).build();
	}
	
	@Secured
	@POST
	@Path("/{groupId}/updatemembershipform")
	public Response updateMembershipForm(@PathParam("groupId") long groupId, @PathParam("userId") long userId, 
			MembershipForm form) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.updateMembershipForm(groupId, form);
		//Getting updated groupdetail in seperate transaction so that we get updated data
		GroupDetail groupDetail = groupBusinessService.getGroupDetail(groupId, userId);
		return Response.ok().entity(groupDetail).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/addadmin/{memberUserId}")
	public Response addAdmin(@PathParam("groupId") long groupId, @PathParam("userId") long signedInUserId, 
			@PathParam("memberUserId") long memberUserId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.addAdmin(signedInUserId, groupId, memberUserId);
		GroupMember member = groupBusinessService.getMember(groupId, memberUserId);
		return Response.ok().entity(member).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/member")
	public Response getMember(@PathParam("groupId") long groupId, @PathParam("userId") long memberUserId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		GroupMember member = groupBusinessService.getMember(groupId, memberUserId);
		return Response.ok().entity(member).build();
	}
	
	@Secured
	@GET
	@Path("/{groupId}/removemember/{memberUserId}")
	public Response removeMember(@PathParam("groupId") long groupId, @PathParam("userId") long signedInUserId,
			@PathParam("memberUserId") long memberUserId){
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		groupBusinessService.removeMember(signedInUserId, groupId, memberUserId);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setStatus(ResponseMessage.Code.OK);
		return Response.ok(responseMessage).build();
	}
	
	@Secured
	@GET
	@Path("/checkgroupexist/{name}")
	public Response isGroupNameExist(@PathParam("name") String name) {
		GroupBusinessService groupBusinessService = new GroupBusinessService();
		boolean status = groupBusinessService.isGroupNameExist(name);
		ResponseMessage responseMessage = new ResponseMessage();
		responseMessage.setResult(Boolean.toString(status));
		return Response.ok(responseMessage).build();
	}

}











