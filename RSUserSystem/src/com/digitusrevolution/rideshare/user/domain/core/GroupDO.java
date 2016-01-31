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
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
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
		MembershipRequest submittedMembershipRequest = group.getMembershipRequest(userId);
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
	
	public void approveMembershipRequest(){
		
	}
	
	public void rejectMembershipRequest(){
		
	}
	
	public void giveFeedback(){
		
	}
	
	public void leaveGroup(){
		
	}
}










































