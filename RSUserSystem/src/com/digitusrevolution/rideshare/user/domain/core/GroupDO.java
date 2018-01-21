package com.digitusrevolution.rideshare.user.domain.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;
import com.digitusrevolution.rideshare.model.user.domain.MembershipForm;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;
import com.digitusrevolution.rideshare.model.user.domain.Photo;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;
import com.digitusrevolution.rideshare.model.user.domain.Vote;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicMembershipRequest;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.model.user.dto.GroupInviteUserSearchResult;
import com.digitusrevolution.rideshare.model.user.dto.GroupMember;
import com.digitusrevolution.rideshare.model.user.dto.MembershipStatus;
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
	public int createGroup(Group group, byte[] rawImage){
		ZonedDateTime currentTimeInUTC = DateTimeUtil.getCurrentTimeInUTC();
		group.setCreatedDateTime(currentTimeInUTC);
		UserDO userDO = new UserDO();
		User user = userDO.get(group.getOwner().getId());
		group.setOwner(user);
		//Its important to add owner as group member as well
		group.getMembers().add(user);
		//Its important to add owner as group admin as well
		group.getAdmins().add(user);
		//Storing group Photo
		if (rawImage!=null) {
			String filePath = saveRawImage(rawImage, group);
			if (filePath!=null) {
				String photoUrl = PropertyReader.getInstance().getProperty("PHOTO_WEB_URL");
				String groupPhotoUrl = photoUrl + filePath;
				Photo photo = new Photo();
				photo.setImageLocation(groupPhotoUrl);
				group.setPhoto(photo);							
			}
		}
		int id = create(group);
		return id;
	}
	
	public void updateGroup(Group updatedGroup, byte[] rawImage) {
		group = getAllData(updatedGroup.getId());
		group.setName(updatedGroup.getName());
		group.setInformation(updatedGroup.getInformation());
		if (rawImage!=null) {
			String filePath = saveRawImage(rawImage, group);
			if (filePath!=null) {
				String photoUrl = PropertyReader.getInstance().getProperty("PHOTO_WEB_URL");
				String groupPhotoUrl = photoUrl + filePath;
				//This will update the photo
				if (group.getPhoto()!=null) {
					group.getPhoto().setImageLocation(groupPhotoUrl);	
				}
				//This will add new photo
				else {
					Photo photo = new Photo();
					photo.setImageLocation(groupPhotoUrl);
					group.setPhoto(photo);							
				}
			}
		}
		update(group);
	}
	
	private String saveRawImage(byte[] rawImage, Group group) {
		String photoRootDir = PropertyReader.getInstance().getProperty("PHOTO_ROOT_PATH");
		String photoGroupDir = PropertyReader.getInstance().getProperty("GROUP_PHOTO_FOLDER_NAME");
		String photoFullPath =  photoRootDir + photoGroupDir;
		// Use relative path for Unix systems
		File dir = new File(photoFullPath);
		//Somehow this is not working, so best is to have the group directory created manually
		//dir.getParentFile().mkdirs(); 
		//We can't user groupId as its not generated as of now, so use random UUID
		String fileName = UUID.randomUUID().toString() + ".jpg";
		File destination = new File(dir,fileName);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(rawImage);
            fo.close();
            String filePathExcludingRootDir = photoGroupDir + File.separator + fileName;  
            logger.debug("Saved File path:"+filePathExcludingRootDir);
            return filePathExcludingRootDir;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
		group = getAllData(groupId);
		membershipRequest.setUser(user);
		membershipRequest.setGroup(group);
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

	public BasicMembershipRequest getBasicMembershipRequest(int groupId, int userId){
		MembershipRequest membershipRequest = getMembershipRequest(groupId, userId);
		if (membershipRequest!=null){
			UserDO userDO = new UserDO();
			return userDO.getBasicMembershipRequestFromRequest(membershipRequest);			
		}
		//don't throw exception, let business logic handle this
		return null;
	} 

	public boolean isMembershipRequestSubmitted(int groupId, int userId) {
		MembershipRequestEntity membershipRequestEntity = groupDAO.getMembershipRequest(groupId, userId);
		if (membershipRequestEntity!=null){
			return true;
		} else {
			return false;
		}
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
	public void approveMembershipRequest(int groupId, int requesterUserId, String remark){
		MembershipRequest membershipRequest = getMembershipRequest(groupId, requesterUserId);
		group = getAllData(groupId);
		if (membershipRequest!=null){
			if (!membershipRequest.getStatus().equals(ApprovalStatus.Approved)){
				membershipRequest.setAdminRemark(remark);
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
			throw new NotAcceptableException("No membership request has been submitted by user id:"+requesterUserId +" for group id:"+groupId);
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
	public void rejectMembershipRequest(int groupId, int requesterUserId, String remark){
		MembershipRequest membershipRequest = getMembershipRequest(groupId, requesterUserId);
		if (membershipRequest!=null){
			if (membershipRequest.getStatus().equals(ApprovalStatus.Pending)){
				membershipRequest.setAdminRemark(remark);
				membershipRequest.setStatus(ApprovalStatus.Rejected);
				MembershipRequestDO membershipRequestDO = new MembershipRequestDO();
				membershipRequestDO.update(membershipRequest);
			} else {
				throw new NotAcceptableException("Membership request can't be rejected as its not in valid state."
						+ "Its current status:"+membershipRequest.getStatus());
			}
		}
		else {
			throw new NotAcceptableException("No membership request has been submitted by user id:"+requesterUserId +" for group id:"+groupId);
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
	public void removeMember(int signedInUserId, int groupId, int memberUserId){
		if (isAdmin(groupId, signedInUserId)) {
			group = getAllData(groupId);
			UserDO userDO = new UserDO();
			User member = userDO.get(memberUserId);
			if (group.getOwner().getId()==memberUserId) {
				throw new NotAcceptableException("Owner can't be removed from the group");
			} else {
				boolean removeStatus = group.getMembers().remove(member);		
				if (removeStatus){
					//This will remove admin as well if user is an admin
					if (isAdmin(groupId, memberUserId)) {
						group.getAdmins().remove(member);
					}
					//This will remove membership request if submitted (in case you have created the group so request is there)
					MembershipRequestEntity membershipRequestEntity = groupDAO.getMembershipRequest(groupId, memberUserId);
					if (membershipRequestEntity!=null) {
						MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
						MembershipRequest membershipRequest = membershipRequestMapper.getDomainModel(membershipRequestEntity, false);
						group.getMembershipRequests().remove(membershipRequest);				
					}
					update(group);	
				} else {
					throw new NotAcceptableException("Group with id:"+groupId+" doesn't not have any member with id:"+memberUserId);
				}			
			}			
		} else {
			throw new NotAcceptableException("You are not authorized to approve the request as you are not an admin");
		}
	}

	public void leaveGroup(int groupId, int userId){
		//Reason for having seperate function is just for naming convenience
		//and may be in future additional task may be added
		group = get(groupId);
		if (group.getOwner().getId()==userId) {
			throw new NotAcceptableException("You can't leave the group as you are the owner of this group with id:"+groupId);
		} else {
			//Note - Here signedInUser and memberUserId is same, so sending userId in both parameter
			removeMember(userId, groupId, userId);	
		}
	}

	public void updateMembershipForm(int groupId, MembershipForm form) {
		group = getAllData(groupId);
		group.setMembershipForm(form);
		update(group);
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
	public void addAdmin(int signedInUserId, int groupId, int memberUserId){
		if (isAdmin(groupId, signedInUserId)) {
			User member = getMember(groupId, memberUserId);
			if (member!=null){
				//TODO This is a very expensive operation as it will get all members etc. we need to find better way to avoid getAllData for group/user etc.
				group = getAllData(groupId);
				if (!isAdmin(groupId, memberUserId)){
					group.getAdmins().add(member);
					update(group);
				} else {
					throw new NotAcceptableException("User is already an admin. User id, Group id:"+memberUserId+","+groupId);
				}
			} else {
				throw new NotAcceptableException("User is not a member of this group, So can't be added as admin. "
						+ "User id, Group id:"+memberUserId+","+groupId);
			}			
		} else {
			throw new NotAcceptableException("You are not authorized to approve the request as you are not an admin");
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
	 * Purpose - Return true/false if user is a member or not
	 * 
	 * This is just a convinience method similar to getMember which avoids any further sql queries while doing mapping
	 */
	public boolean isMember(int groupId, int userId) {
		UserEntity memberEntity = groupDAO.getMember(groupId, userId);
		if (memberEntity!=null){
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Purpose - Return true/false if user is an admin of the group
	 * This function should be used when you just need to know the status of admin as its a very less resource intesive function 
	 * as compared to getAllData of group and then find out in the list of admin if it exist or not
	 */
	public boolean isAdmin(int groupId, int memberUserId) {
		return groupDAO.isAdmin(groupId, memberUserId);
	}

	public Vote getVote(int groupId, int memberUserId) {
		group = getAllData(groupId);
		for (GroupFeedback feedback: group.getFeedbacks()) {
			if (feedback.getGivenByUser().getId()==memberUserId) {
				return feedback.getVote();
			}
		}
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
		int genuineVotes = 0;
		int fakeVotes = 0;
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
			//Reiterating through the feedbacks so that we can get the total geunine/fake count and the same can be updated as well
			for (GroupFeedback groupFeedback: group.getFeedbacks()) {
				if (groupFeedback.getVote().equals(Vote.Genuine)) {
					genuineVotes++;
				} else {
					fakeVotes++;
				}
			}
			group.setGenuineVotes(genuineVotes);
			group.setFakeVotes(fakeVotes);
			update(group);
		}else {
			throw new WebApplicationException("User can't vote for this group as you are not a member. Group id, User id:"+groupId+","+memberUserId);
		}
	}

	public int getMemberCount(int groupId) {
		return groupDAO.getMemberCount(groupId);
	}

	public List<GroupMember> getMembers(int groupId, int page){

		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		List<UserEntity> userEntities = groupDAO.getMembers(groupId, startIndex);
		UserMapper userMapper = new UserMapper();
		LinkedList<User> members = new LinkedList<>();
		members = (LinkedList<User>) userMapper.getDomainModels(members, userEntities, false);
		//this will sort the list further
		Collections.sort(members);

		LinkedList<GroupMember> groupMembers = new LinkedList<>();
		for (User user: members) {
			GroupMember groupMember = new GroupMember();
			groupMember = getGroupMemberFromUser(groupId, user);
			groupMembers.add(groupMember);
		}

		return groupMembers;
	}

	public GroupMember getGroupMemberFromUser(int groupId, User user) {
		GroupMember groupMember;
		groupMember = JsonObjectMapper.getMapper().convertValue(user, GroupMember.class);
		groupMember.setAdmin(isAdmin(groupId, user.getId()));
		return groupMember;
	}

	/*
	 * This will get group details with membership status and member count
	 * 
	 */
	public GroupDetail getGroupDetail(int groupId, int userId) {
		Group group = get(groupId);
		return getGroupDetailFromGroup(group, userId);
	}

	/*
	 * This function is for convinience so that we can easily get GroupDetail with updated details from group
	 * 
	 */
	public GroupDetail getGroupDetailFromGroup(Group group, int userId) {
		GroupDetail groupDetail = JsonObjectMapper.getMapper().convertValue(group, GroupDetail.class);
		groupDetail.setMemberCount(groupDAO.getMemberCount(group.getId()));
		groupDetail.setPendingRequestCount(groupDAO.getGroupMembershipRequestCount(group.getId()));
		groupDetail.setMembershipStatus(getMembershipStatus(group.getId(), userId));
		return groupDetail;
	}

	/*
	 * This will get membership status of any user for a specific group
	 * 
	 */
	public MembershipStatus getMembershipStatus(int groupId, int userId) {
		MembershipStatus membershipStatus = new MembershipStatus();
		boolean memberStatus = isMember(groupId, userId);
		membershipStatus.setMember(memberStatus);
		if (memberStatus) {
			boolean adminStatus = isAdmin(groupId, userId);
			membershipStatus.setAdmin(adminStatus);
			membershipStatus.setVote(getVote(groupId, userId));
		} else {
			//This would not be applicable once user becomes member as we remove the invite
			UserDO userDO = new UserDO();
			boolean inviteStatus = userDO.isInvited(groupId, userId);
			membershipStatus.setInvited(inviteStatus);
		}
		//This would be applicable for all cases as we are not removing the request from the system
		//And lets send appropriate data for all calls
		MembershipRequest membershipRequest = getMembershipRequest(groupId, userId);
		if (membershipRequest!=null) {
			membershipStatus.setRequestSubmitted(true);
			membershipStatus.setApprovalStatus(membershipRequest.getStatus());
		} else {
			membershipStatus.setRequestSubmitted(false);
		}

		return membershipStatus;
	}

	public List<GroupInviteUserSearchResult> searchUserByNameForGroupInvite(int groupId, String name, int page){
		UserDO userDO = new UserDO();
		List<User> users = userDO.searchUserByName(name, page);
		List<GroupInviteUserSearchResult> results = new LinkedList<>();
		for (User user: users) {
			GroupInviteUserSearchResult result = new GroupInviteUserSearchResult();
			//IMP - Reason for not transferring the whole user data 
			//and just sending name and photo is from security concern
			BasicUser basicUser = new BasicUser();
			basicUser.setId(user.getId());
			basicUser.setFirstName(user.getFirstName());
			basicUser.setLastName(user.getLastName());
			basicUser.setPhoto(user.getPhoto());
			result.setUser(basicUser);
			result.setMember(isMember(groupId, user.getId()));
			result.setInvited(userDO.isInvited(groupId, user.getId()));
			result.setRequestSubmitted(isMembershipRequestSubmitted(groupId, user.getId()));
			results.add(result);
		}
		return results;
	}

	public List<GroupDetail> searchGroupByName(int userId, String name, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		Set<GroupEntity> groupEntities = groupDAO.searchGroupByName(name, startIndex);
		LinkedList<Group> groups = new LinkedList<>();
		groups = (LinkedList<Group>) groupMapper.getDomainModels(groups, groupEntities, false);
		Collections.sort(groups);
		return getGroupDetails(userId, groups);
	}

	/*
	 * This is for convinience of converting group to groupDetails by setting membership status against the user 
	 * 
	 */
	public List<GroupDetail> getGroupDetails(int userId, LinkedList<Group> groups) {
		LinkedList<GroupDetail> groupDetails = new LinkedList<>();
		for (Group group: groups) {
			groupDetails.add(getGroupDetailFromGroup(group, userId));
		}
		return groupDetails;
	}

	public List<BasicMembershipRequest> getGroupMembershipRequests(int groupId, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		Set<MembershipRequestEntity> membershipRequestEntities = groupDAO.getGroupMembershipRequests(groupId, startIndex);
		UserDO userDO = new UserDO();
		return userDO.getBasicMembershipRequests(membershipRequestEntities);
	}
}










































