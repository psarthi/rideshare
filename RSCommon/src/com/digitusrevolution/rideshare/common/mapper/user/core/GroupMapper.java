package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.FormMapper;
import com.digitusrevolution.rideshare.common.mapper.user.GroupFeedbackMapper;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.PhotoMapper;
import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;

public class GroupMapper implements Mapper<Group, GroupEntity>{

	@Override
	public GroupEntity getEntity(Group group, boolean fetchChild) {
		GroupEntity groupEntity = new GroupEntity();
		groupEntity.setId(group.getId());
		groupEntity.setName(group.getName());
		groupEntity.setCreatedDateTime(group.getCreatedDateTime());
		groupEntity.setUrl(group.getUrl());
		groupEntity.setInformation(group.getInformation());
		groupEntity.setGenuineVotes(group.getGenuineVotes());
		groupEntity.setFakeVotes(group.getFakeVotes());
		
		PhotoMapper photoMapper = new PhotoMapper();
		if (group.getPhoto()!=null) groupEntity.setPhoto(photoMapper.getEntity(group.getPhoto(), fetchChild));
		
		FormMapper formMapper = new FormMapper();
		if (group.getMembershipForm()!=null) groupEntity.setMembershipForm(formMapper.getEntity(group.getMembershipForm(), fetchChild));
		
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as User has group and group has user
		groupEntity.setOwner(userMapper.getEntity(group.getOwner(), false));
		groupEntity.setAdmins(userMapper.getEntities(groupEntity.getAdmins(), group.getAdmins(), false));

		if (fetchChild){
			groupEntity = getEntityChild(group, groupEntity);
		}
		return groupEntity;
	}

	@Override
	public GroupEntity getEntityChild(Group group, GroupEntity groupEntity) {

		UserMapper userMapper = new UserMapper();
		//Don't fetch child as User has group and group has user 
		groupEntity.setMembers(userMapper.getEntities(groupEntity.getMembers(), group.getMembers(), false));

		GroupFeedbackMapper groupFeedbackMapper = new GroupFeedbackMapper();
		groupEntity.setFeedbacks(groupFeedbackMapper.getEntities(groupEntity.getFeedbacks(), 
				group.getFeedbacks(), true));

		MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
		groupEntity.setMembershipRequests(membershipRequestMapper.getEntities(groupEntity.getMembershipRequests(),
				group.getMembershipRequests(), true));

		return groupEntity;
	}

	@Override
	public Group getDomainModel(GroupEntity groupEntity, boolean fetchChild) {
		Group group = new Group();
		group.setId(groupEntity.getId());
		group.setName(groupEntity.getName());
		group.setCreatedDateTime(groupEntity.getCreatedDateTime());
		group.setUrl(groupEntity.getUrl());
		group.setInformation(groupEntity.getInformation());
		group.setGenuineVotes(groupEntity.getGenuineVotes());
		group.setFakeVotes(groupEntity.getFakeVotes());
		
		PhotoMapper photoMapper = new PhotoMapper();
		if (groupEntity.getPhoto()!=null)  group.setPhoto(photoMapper.getDomainModel(groupEntity.getPhoto(), fetchChild));
		
		FormMapper formMapper = new FormMapper();
		if (groupEntity.getMembershipForm()!=null) group.setMembershipForm(formMapper.getDomainModel(groupEntity.getMembershipForm(), fetchChild));

		UserMapper userMapper = new UserMapper();
		//Don't fetch child as User has group and group has user 
		group.setOwner(userMapper.getDomainModel(groupEntity.getOwner(), false));
		group.setAdmins(userMapper.getDomainModels(group.getAdmins(), groupEntity.getAdmins(), false));

		if (fetchChild){
			group = getDomainModelChild(group, groupEntity);
		}
		return group;
	}

	@Override
	public Group getDomainModelChild(Group group, GroupEntity groupEntity) {
		
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as User has group and group has user 
		group.setMembers(userMapper.getDomainModels(group.getMembers(), groupEntity.getMembers(), false));

		GroupFeedbackMapper groupFeedbackMapper = new GroupFeedbackMapper();
		group.setFeedbacks(groupFeedbackMapper.getDomainModels(group.getFeedbacks(), 
				groupEntity.getFeedbacks(), true));

		MembershipRequestMapper membershipRequestMapper = new MembershipRequestMapper();
		group.setMembershipRequests(membershipRequestMapper.getDomainModels(group.getMembershipRequests(),
				groupEntity.getMembershipRequests(), true));

		return group;
	}

	@Override
	public Collection<Group> getDomainModels(Collection<Group> groups, Collection<GroupEntity> groupEntities,
			boolean fetchChild) {
		for (GroupEntity groupEntity : groupEntities) {
			Group group = new Group();
			group = getDomainModel(groupEntity, fetchChild);
			groups.add(group);
		}
		return groups;
	}

	@Override
	public Collection<GroupEntity> getEntities(Collection<GroupEntity> groupEntities, Collection<Group> groups,
			boolean fetchChild) {
		for (Group group : groups) {
			GroupEntity groupEntity = new GroupEntity();
			groupEntity = getEntity(group, fetchChild);
			groupEntities.add(groupEntity);
		}
		return groupEntities;	
	}

}




















