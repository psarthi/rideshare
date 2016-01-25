package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class MembershipRequestMapper implements Mapper<MembershipRequest, MembershipRequestEntity>{

	@Override
	public MembershipRequestEntity getEntity(MembershipRequest membershipRequest, boolean fetchChild) {
		MembershipRequestEntity membershipRequestEntity = new MembershipRequestEntity();
		membershipRequestEntity.setId(membershipRequest.getId());
		membershipRequestEntity.setCreatedDateTime(membershipRequest.getCreatedDateTime());
		membershipRequestEntity.setAnswers(membershipRequest.getAnswers());
		membershipRequestEntity.setStatus(membershipRequest.getStatus());
		UserMapper userMapper = new UserMapper();
		membershipRequestEntity.setUser(userMapper.getEntity(membershipRequest.getUser(), false));
		membershipRequestEntity.setEmailForVerification(membershipRequest.getEmailForVerification());
		membershipRequestEntity.setEmailVerificationStatus(membershipRequest.getEmailVerificationStatus());
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
		membershipRequest.setAnswers(membershipRequestEntity.getAnswers());
		membershipRequest.setStatus(membershipRequestEntity.getStatus());
		UserMapper userMapper = new UserMapper();
		membershipRequest.setUser(userMapper.getDomainModel(membershipRequestEntity.getUser(), false));
		membershipRequest.setEmailForVerification(membershipRequestEntity.getEmailForVerification());
		membershipRequest.setEmailVerificationStatus(membershipRequestEntity.getEmailVerificationStatus());
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


















