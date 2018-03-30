package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.InterestMapper;
import com.digitusrevolution.rideshare.model.user.data.InterestEntity;
import com.digitusrevolution.rideshare.model.user.domain.Interest;

public class InterestDO implements DomainObjectPKLong<Interest>{
	
	private Interest interest;
	private InterestEntity interestEntity;
	private InterestMapper interestMapper;
	private final GenericDAO<InterestEntity, Long> genericDAO;
	
	public InterestDO() {
		interest = new Interest();
		interestEntity = new InterestEntity();
		interestMapper = new InterestMapper();
		genericDAO = new GenericDAOImpl<>(InterestEntity.class);
	}

	public void setInterest(Interest interest) {
		this.interest = interest;
		interestEntity = interestMapper.getEntity(interest, true);
	}

	public void setInterestEntity(InterestEntity interestEntity) {
		this.interestEntity = interestEntity;
		interest = interestMapper.getDomainModel(interestEntity, false);
	}

	@Override
	public List<Interest> getAll() {
		List<Interest> interests = new ArrayList<>();
		List<InterestEntity> interestEntities = genericDAO.getAll();
		for (InterestEntity interestEntity : interestEntities) {
			setInterestEntity(interestEntity);
			interests.add(interest);
		}
		return interests;
	}

	@Override
	public void update(Interest interest) {
		if (interest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+interest.getId());
		}
		setInterest(interest);
		genericDAO.update(interestEntity);				
	}

	@Override
	public void fetchChild() {
		interest = interestMapper.getDomainModelChild(interest, interestEntity);	
	}

	@Override
	public long create(Interest interest) {
		setInterest(interest);
		long id = genericDAO.create(interestEntity);
		return id;
	}

	@Override
	public Interest get(long id) {
		interestEntity = genericDAO.get(id);
		if (interestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setInterestEntity(interestEntity);
		return interest;
	}

	@Override
	public Interest getAllData(long id) {
		get(id);
		fetchChild();
		return interest;
	}

	@Override
	public void delete(long id) {
		interest = get(id);
		setInterest(interest);
		genericDAO.delete(interestEntity);			
	}
	
}
