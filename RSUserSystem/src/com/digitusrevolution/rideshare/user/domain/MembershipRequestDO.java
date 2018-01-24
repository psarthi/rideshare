package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.MembershipRequestMapper;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.MembershipRequest;

public class MembershipRequestDO implements DomainObjectPKLong<MembershipRequest>{

	private MembershipRequest membershipRequest;
	private MembershipRequestEntity membershipRequestEntity;
	private MembershipRequestMapper membershipRequestMapper;
	private GenericDAO<MembershipRequestEntity, Long> genericDAO;

	public void setMembershipRequest(MembershipRequest membershipRequest) {
		this.membershipRequest = membershipRequest;
		membershipRequestEntity = membershipRequestMapper.getEntity(membershipRequest, true);
	}

	public void setMembershipRequestEntity(MembershipRequestEntity membershipRequestEntity) {
		this.membershipRequestEntity = membershipRequestEntity;
		membershipRequest = membershipRequestMapper.getDomainModel(membershipRequestEntity, false);
	}

	public MembershipRequestDO() {
		membershipRequest = new MembershipRequest();
		membershipRequestEntity = new MembershipRequestEntity();
		membershipRequestMapper = new MembershipRequestMapper();
		genericDAO = new GenericDAOImpl<>(MembershipRequestEntity.class);
	}

	@Override
	public List<MembershipRequest> getAll() {
		List<MembershipRequest> membershipRequests = new ArrayList<>();
		List<MembershipRequestEntity> membershipRequestEntities = genericDAO.getAll();
		for (MembershipRequestEntity membershipRequestEntity : membershipRequestEntities) {
			setMembershipRequestEntity(membershipRequestEntity);
			membershipRequests.add(membershipRequest);
		}
		return membershipRequests;	
	}

	@Override
	public void update(MembershipRequest membershipRequest) {
		if (membershipRequest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+membershipRequest.getId());
		}
		setMembershipRequest(membershipRequest);
		genericDAO.update(membershipRequestEntity);				
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub

	}

	@Override
	public long create(MembershipRequest membershipRequest) {
		setMembershipRequest(membershipRequest);
		long id = genericDAO.create(membershipRequestEntity);
		return id;
	}

	@Override
	public MembershipRequest get(long id) {
		membershipRequestEntity = genericDAO.get(id);
		if (membershipRequestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setMembershipRequestEntity(membershipRequestEntity);
		return membershipRequest;
	}

	@Override
	public MembershipRequest getAllData(long id) {
		get(id);
		fetchChild();
		return membershipRequest;
	}

	@Override
	public void delete(long id) {
		membershipRequest = get(id);
		setMembershipRequest(membershipRequest);
		genericDAO.delete(membershipRequestEntity);			
	}

}
