package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.GroupMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class MembershipRequestMapper implements Mapper<MembershipRequest, MembershipRequestEntity>{

	@Override
	public MembershipRequestEntity getEntity(MembershipRequest membershipRequest, boolean fetchChild) {
		MembershipRequestEntity membershipRequestEntity = new MembershipRequestEntity();
		membershipRequestEntity.setId(membershipRequest.getId());
		membershipRequestEntity.setCreatedDateTime(membershipRequest.getCreatedDateTime());
		membershipRequestEntity.setUserUniqueIdentifier(membershipRequest.getUserUniqueIdentifier());
		membershipRequestEntity.setQuestionAnswers(membershipRequest.getQuestionAnswers());
		membershipRequestEntity.setStatus(membershipRequest.getStatus());
		//Don't get child else, we will end up into recursive loop as Request has User and Group and its also vice versa
		UserMapper userMapper = new UserMapper();
		membershipRequestEntity.setUser(userMapper.getEntity(membershipRequest.getUser(), false));
		GroupMapper groupMapper = new GroupMapper();
		membershipRequestEntity.setGroup(groupMapper.getEntity(membershipRequest.getGroup(), false));
		membershipRequestEntity.setAdminRemark(membershipRequest.getAdminRemark());
		return membershipRequestEntity;
	}

	@Override
	public MembershipRequestEntity getEntityChild(MembershipRequest membershipRequest, MembershipRequestEntity membershipRequestEntity) {
		return membershipRequestEntity;
	}

	@Override
	public MembershipRequest getDomainModel(MembershipRequestEntity membershipRequestEntity, boolean fetchChild) {
		MembershipRequest membershipRequest = new MembershipRequest();
		membershipRequest.setId(membershipRequestEntity.getId());
		membershipRequest.setCreatedDateTime(membershipRequestEntity.getCreatedDateTime());
		membershipRequest.setUserUniqueIdentifier(membershipRequestEntity.getUserUniqueIdentifier());
		membershipRequest.setQuestionAnswers(membershipRequestEntity.getQuestionAnswers());
		membershipRequest.setStatus(membershipRequestEntity.getStatus());
		UserMapper userMapper = new UserMapper();
		membershipRequest.setUser(userMapper.getDomainModel(membershipRequestEntity.getUser(), false));
		GroupMapper groupMapper = new GroupMapper();
		membershipRequest.setGroup(groupMapper.getDomainModel(membershipRequestEntity.getGroup(), false));
		membershipRequest.setAdminRemark(membershipRequestEntity.getAdminRemark());
		return membershipRequest;
	}

	@Override
	public MembershipRequest getDomainModelChild(MembershipRequest membershipRequest, MembershipRequestEntity entity) {
		return membershipRequest;
	}

	@Override
	public Collection<MembershipRequest> getDomainModels(Collection<MembershipRequest> membershipRequests,
			Collection<MembershipRequestEntity> membershipRequestEntities, boolean fetchChild) {
		for (MembershipRequestEntity membershipRequestEntity : membershipRequestEntities) {
			MembershipRequest membershipRequest = new MembershipRequest();
			membershipRequest = getDomainModel(membershipRequestEntity, fetchChild);
			membershipRequests.add(membershipRequest);
		}
		return membershipRequests;
	}

	@Override
	public Collection<MembershipRequestEntity> getEntities(Collection<MembershipRequestEntity> membershipRequestEntities,
			Collection<MembershipRequest> membershipRequests, boolean fetchChild) {
		for (MembershipRequest membershipRequest : membershipRequests) {
			MembershipRequestEntity membershipRequestEntity = new MembershipRequestEntity();
			membershipRequestEntity = getEntity(membershipRequest, fetchChild);
			membershipRequestEntities.add(membershipRequestEntity);
		}
		return membershipRequestEntities;
	}

}


















