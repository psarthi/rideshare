package com.digitusrevolution.rideshare.user.domain.core;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
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
	public int createGroup(Group group, int userId){
		ZonedDateTime currentTimeInUTC = DateTimeUtil.getCurrentTimeInUTC();
		group.setCreatedDateTime(currentTimeInUTC);
		UserDO userDO = new UserDO();
		User user = userDO.get(userId);
		group.setOwner(user);
		int id = create(group);
		return id;
	}

	public void addAdmin(){

	}

	public void transferOwnership(){

	}

	public void inviteUsers(){

	}

	public void searchGroup(){

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
	 * 
	 */
	public void sendMembershipRequest(int userId, int groupId, MembershipRequest membershipRequest){
		membershipRequest.setCreatedDateTime(DateTimeUtil.getCurrentTimeInUTC());
		membershipRequest.setStatus(ApprovalStatus.Pending);
		UserDO userDO = new UserDO();
		User user = userDO.get(userId);
		membershipRequest.setUser(user);
		group = get(groupId);
		MembershipRequest submittedMembershipRequest = getMembershipRequest(userId, groupId);
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
		}
	}

	public MembershipRequest getMembershipRequest(int userId, int groupId){
		MembershipRequestEntity membershipRequestEntity = groupDAO.getMembershipRequest(userId, groupId);
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
		MembershipRequest membershipRequest = getMembershipRequest(userId, groupId);
		group = getAllData(groupId);
		if (membershipRequest!=null){
			if (!membershipRequest.getStatus().equals(ApprovalStatus.Approved)){
				//Reason for removing membership request as we want to update the status and for that either we can iterate 
				//all request and then update the specific one, or easy option and faster option would be remove and update
				group.getMembershipRequests().remove(membershipRequest);
				membershipRequest.setStatus(ApprovalStatus.Approved);
				group.getMembershipRequests().add(membershipRequest);
				group.getMembers().add(membershipRequest.getUser());
				update(group);
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
		MembershipRequest membershipRequest = getMembershipRequest(userId, groupId);
		if (membershipRequest!=null){
			if (membershipRequest.getStatus().equals(ApprovalStatus.Pending)){
				//Reason for concatinating is to keep track of old reason for rejection with date time
				remark.concat("\r\n UTC Time:"+DateTimeUtil.getCurrentTimeInUTC().toString()+"--------\r\n");
				membershipRequest.getAdminRemark().concat(remark);
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
		group.getMembers().remove(member);
		update(group);
	}

	public void giveFeedback(){

	}

	public void leaveGroup(){

	}
}










































