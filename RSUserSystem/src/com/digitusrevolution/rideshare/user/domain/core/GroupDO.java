package com.digitusrevolution.rideshare.user.domain.core;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.user.data.GroupDAO;
import com.digitusrevolution.rideshare.user.domain.MembershipRequestDO;

public class GroupDO implements DomainObjectPKInteger<Group>{

	private Group group;
	private GroupEntity groupEntity;
	private final GroupDAO groupDAO;
	private GroupMapper groupMapper;
	private static final Logger logger = LogManager.getLogger(GroupDO.class.getName());


	public GroupDO() {
		group = new Group();
		groupEntity = new GroupEntity();
		groupDAO = new GroupDAO();
		groupMapper = new GroupMapper();
	}

	public void setGroup(Group group) {
		this.group = group;
		groupEntity = groupMapper.getEntity(group, true);
	}

	public void setGroupEntity(GroupEntity groupEntity) {
		this.groupEntity = groupEntity;
		group = groupMapper.getDomainModel(groupEntity, false);
	}

	@Override
	public List<Group> getAll() {
		List<Group> groups = new ArrayList<>();
		List<GroupEntity> groupEntities = groupDAO.getAll();
		for (GroupEntity groupEntity : groupEntities) {
			setGroupEntity(groupEntity);
			groups.add(group);
		}
		return groups;
	}

	@Override
	public void update(Group group) {
		if (group.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+group.getId());
		}
		setGroup(group);
		groupDAO.update(groupEntity);
	}

	@Override
	public void fetchChild() {
		group = groupMapper.getDomainModelChild(group, groupEntity);	
	}

	@Override
	public int create(Group group) {
		setGroup(group);
		int id = groupDAO.create(groupEntity);
		return id;
	}

	@Override
	public Group get(int id) {
		groupEntity = groupDAO.get(id);
		if (groupEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setGroupEntity(groupEntity);
		return group;
	}

	@Override
	public Group getAllData(int id) {
		get(id);
		fetchChild();
		return group;
	}

	@Override
	public void delete(int id) {
		group = get(id);
		setGroup(group);
		groupDAO.delete(groupEntity);
	}

	/*
	 * Purpose - Create group by setting additional properties such as createdDate, Owner etc.
	 */
	public int createGroup(Group group){
		ZonedDateTime currentTimeInUTC = DateTimeUtil.getCurrentTimeInUTC();
		group.setCreatedDateTime(currentTimeInUTC);
		UserDO userDO = new UserDO();
		User user = userDO.get(group.getOwner().getId());
		group.setOwner(user);
		//Its important to add owner as group member as well
		group.getMembers().add(user);
		//Its important to add owner as group admin as well
		group.getAdmins().add(user);
		int id = create(group);
		return id;
	}

	/*
	 * Purpose - Submit membership request for a group
	 * 
	 * High level logic -
	 * 
	 * - Check if membership request is already submitted
	 * - If yes, check the status of that and if its pending, then don't allow resubmission
	 * - If its rejected, update the previous one with status as pending
	 * 	 Note - For now, we will not maintain the transaction log of submission, resubmission dates etc.
	 * - If membership request doesn't exist, then create new one
	 * - Remove group invitation from user
	 * 
	 */
	public void sendMembershipRequest(int groupId, MembershipRequest membershipRequest){
		membershipRequest.setCreatedDateTime(DateTimeUtil.getCurrentTimeInUTC());
		membershipRequest.setStatus(ApprovalStatus.Pending);
		UserDO userDO = new UserDO();
		User user = userDO.getAllData(membershipRequest.getUser().getId());
		membershipRequest.setUser(user);
		group = getAllData(groupId);
		MembershipRequest submittedMembershipRequest = getMembershipRequest(groupId, membershipRequest.getUser().getId());
		//Check if request has been submitted earlier as well
		if (submittedMembershipRequest!=null){
			if (!submittedMembershipRequest.getStatus().equals(ApprovalStatus.Rejected)){
				throw new NotAcceptableException("Membership request can't be resubmitted as your earlier request is not in valid state. "
						+ "Current status is:"+submittedMembershipRequest.getStatus());
			} else {
				//Setting the old id into the membership request, so that it doesn't create the new one 
				membershipRequest.setId(submittedMembershipRequest.getId());
				//We are not updating group as it would be bit complicated as we need to update all values of new request with old request
				//So directly using membershipDO
				MembershipRequestDO membershipRequestDO = new MembershipRequestDO();
				membershipRequestDO.update(membershipRequest);
			}
		}
		//This is the case, when its a new request
		else {
			//Add new membership request to the list and update the group
			//This will take care of association as well as creation of membership request
			group.getMembershipRequests().add(membershipRequest);
			update(group);
			//Remove group invitation from the user
			user.getGroupInvites().remove(group);
			userDO.update(user);
		}
	}

	/*
	 * Purpose - Get complete membership request info and not just basic info
	 * 
	 */
	public MembershipRequest getMembershipRequest(int groupId, int userId){
		MembershipRequestEntity membershipRequestEntity = groupDAO.getMembershipRequest(groupId, userId);
		if (membershipRequestEntity!=null){
			MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
			//Its important to send complete membership request as it may be used as per business need so fetch option should be true
			MembershipRequest membershipRequest = membershipRequestMapper.getDomainModel(membershipRequestEntity, true);
			return membershipRequest;			
		}
		//don't throw exception, let business logic handle this
		return null;
	}

	/*
	 * Purpose - Approve membership request
	 * 
	 * High level logic -
	 * 
	 * - Get status of membership request
	 * - If its not approved, then only it can be approved else throw exception
	 * - Add member to group
	 * - Update the membership status as Approved
	 * 
	 */
	public void approveMembershipRequest(int groupId, int userId){
		MembershipRequest membershipRequest = getMembershipRequest(groupId, userId);
		group = getAllData(groupId);
		if (membershipRequest!=null){
			if (!membershipRequest.getStatus().equals(ApprovalStatus.Approved)){
				group.getMembers().add(membershipRequest.getUser());
				update(group);
				//Don't try to remove membership request from group and add back to update, as it will throw exception
				//saying unique key violation, so easy option is to use membership do to update the same
				//Ensure that this should be done post group update else, status would not change as group has Pending status
				//for the same membership request
				membershipRequest.setStatus(ApprovalStatus.Approved);
				MembershipRequestDO membershipRequestDO = new MembershipRequestDO();
				membershipRequestDO.update(membershipRequest);
			} else {
				throw new NotAcceptableException("Membership request can't be approved as its not in valid state."
						+ "Its current status:"+membershipRequest.getStatus());
			}
		} else {
			throw new NotAcceptableException("No membership request has been submitted by user id:"+userId +" for group id:"+groupId);
		}
	}

	/*
	 * Purpose - Reject membership request
	 * 
	 * High level logic -
	 * 
	 * - Check the status of membership request and see if its Pending
	 * - Only pending status can be rejected
	 * - If its pending, then add remark for rejection and update the status as Rejected
	 * - Maintain log of old remarks by appending the new remark to the old one with date time
	 * 
	 */
	public void rejectMembershipRequest(int groupId, int userId, String remark){
		MembershipRequest membershipRequest = getMembershipRequest(groupId, userId);
		if (membershipRequest!=null){
			if (membershipRequest.getStatus().equals(ApprovalStatus.Pending)){
				//Reason for concatinating is to keep track of old reason for rejection with date time
				String deliminiator = "\r\nUTC Time:"+DateTimeUtil.getCurrentTimeInUTC().toString()+"\r\n--------\r\n";
				String updatedRemark;
				if (membershipRequest.getAdminRemark()==null){
					updatedRemark = deliminiator.concat(remark);
				} else {
					updatedRemark = membershipRequest.getAdminRemark().concat(deliminiator).concat(remark);					
				}
				membershipRequest.setAdminRemark(updatedRemark);
				membershipRequest.setStatus(ApprovalStatus.Rejected);
				MembershipRequestDO membershipRequestDO = new MembershipRequestDO();
				membershipRequestDO.update(membershipRequest);
			} else {
				throw new NotAcceptableException("Membership request can't be rejected as its not in valid state."
						+ "Its current status:"+membershipRequest.getStatus());
			}
		}
		else {
			throw new NotAcceptableException("No membership request has been submitted by user id:"+userId +" for group id:"+groupId);
		}
	}
	
	/*
	 * Purpose - Remove member from the group
	 * 
	 * High level logic -
	 * 
	 * - Remove member from group
	 * 
	 */
	public void removeMember(int groupId, int memberUserId){
		group = getAllData(groupId);
		UserDO userDO = new UserDO();
		User member = userDO.get(memberUserId);
		boolean removeStatus = group.getMembers().remove(member);
		if (removeStatus){
			update(group);	
		} else {
			throw new NotAcceptableException("Group with id:"+groupId+" doesn't not have any member with id:"+memberUserId);
		}
	}
	
	public void leaveGroup(int groupId, int userId){
		//Reason for having seperate function is just for naming convenience
		//and may be in future additional task may be added
		removeMember(groupId, userId);
	}
	
	/*
	 * Purpose - Add group admin who should be one of the member of the group
	 * 
	 * High level logic -
	 * 
	 * - Check if user is group member
	 * - If yes, then check if user is already an admin (No need to check for owner, as owner is also added to group admins)
	 * - if user is not admin and member of group, then add as admin
	 * 
	 */
	public void addAdmin(int groupId, int memberUserId){ 	
		User member = getMember(groupId, memberUserId);
		if (member!=null){
			group = getAllData(groupId);
			if (!group.isMemberAdmin(memberUserId)){
				group.getAdmins().add(member);
				update(group);
			} else {
				throw new NotAcceptableException("User is already an admin. User id, Group id:"+memberUserId+","+groupId);
			}
		} else {
			throw new NotAcceptableException("User is not a member of this group, So can't be added as admin. "
					+ "User id, Group id:"+memberUserId+","+groupId);
		}
	}
	
	/*
	 * Purpose - Return member with basic information
	 * 
	 */
	public User getMember(int groupId, int memberUserId){
		UserEntity memberEntity = groupDAO.getMember(groupId, memberUserId);
		if (memberEntity!=null){
			UserMapper userMapper = new UserMapper();
			//No need to get complete user, so set the fetch value as false
			User member = userMapper.getDomainModel(memberEntity, false);
			return member;
		}
		//don't throw exception, let business logic handle this
		return null;
	}

	/*
	 * Purpose - Send group membership invitation to multiple users
	 * 
	 * High level logic -
	 * 
	 * - Check for all users, if user is already a member
	 * - Create a valid user list
	 * - Add group to all valid user group invites collection 
	 * 
	 */
	public void inviteUsers(int groupId, List<Integer> userIds){
		List<Integer> validUserIds = new ArrayList<>();
		for (Integer userId : userIds) {
			User member = getMember(groupId, userId);
			if (member==null){
				validUserIds.add(userId);
			} else {
				logger.debug("User is already group member. Group id, User Id:"+groupId+","+userId);
			}
		}
		group = get(groupId);
		for (Integer userId : validUserIds) {
			UserDO userDO = new UserDO();
			User user = userDO.getAllData(userId);
			user.getGroupInvites().add(group);
			userDO.update(user);						
		}
	}
	
	/*
	 * Purpose - Give feedback for group and one user can have only one feedback which can be overwritten
	 * 
	 * High level logic -
	 * 
	 * - Check if user is a member of the group
	 * - If yes, check if feedback exist for that user
	 * - If yes, overwrite else add a new feedback
	 * 
	 */
	public void giveFeedback(int groupId, int memberUserId, GroupFeedback feedback){
		User member = getMember(groupId, memberUserId);
		if (member!=null){
			group=getAllData(groupId);
			Collection<GroupFeedback> feedbacks = group.getFeedbacks();
			boolean newFeedback = true;
			//Reason for iterating through all feedbacks here instead of another function, so that we can update that feedback
			//If we get feedback from another function, we loose the pointer and unable to update directly and we may have to 
			//find tricky way to update that feedback
			for (GroupFeedback groupFeedback : feedbacks) {
				if (groupFeedback.getGivenByUser().getId()==memberUserId){
					//Update the feedback
					groupFeedback.setVote(feedback.getVote());
					newFeedback = false;
					break;
				}
			}
			if (newFeedback){
				//Add feedback
				feedback.setGivenByUser(member);
				group.getFeedbacks().add(feedback);
			}
			update(group);
		}else {
			throw new WebApplicationException("User can't vote for this group as you are not a member. Group id, User id:"+groupId+","+memberUserId);
		}
	}
	
	public int getMemberCount(int groupId) {
		return groupDAO.getMemberCount(groupId);
	}
	
	public List<User> getMembers(int groupId, int page){
		
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<UserEntity> userEntities = groupDAO.getMembers(groupId, startIndex);
		UserMapper userMapper = new UserMapper();
		LinkedList<User> users = new LinkedList<>();
		userMapper.getDomainModels(users, userEntities, false);
		//this will sort the list further
		Collections.sort(users);
		return users;
	}


}










































