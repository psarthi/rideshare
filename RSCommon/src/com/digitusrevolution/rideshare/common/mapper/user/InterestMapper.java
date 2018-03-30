package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.InterestEntity;
import com.digitusrevolution.rideshare.model.user.domain.Interest;

public class InterestMapper implements Mapper<Interest, InterestEntity>{

	@Override
	public InterestEntity getEntity(Interest interest, boolean fetchChild) {
		InterestEntity interestEntity = new InterestEntity();
		interestEntity.setId(interest.getId());
		interestEntity.setName(interest.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		interestEntity.setPhoto(photoMapper.getEntity(interest.getPhoto(), fetchChild));
		
		if (fetchChild) {
			interestEntity = getEntityChild(interest, interestEntity);
		}
		
		return interestEntity;
	}

	@Override
	public InterestEntity getEntityChild(Interest interest, InterestEntity interestEntity) {
		UserMapper userMapper = new UserMapper();
		interestEntity.setUsers(userMapper.getEntities(interestEntity.getUsers(), interest.getUsers(), false));
		return interestEntity;
	}

	@Override
	public Interest getDomainModel(InterestEntity interestEntity, boolean fetchChild) {
		Interest interest = new Interest();
		interest.setId(interestEntity.getId());
		interest.setName(interestEntity.getName());
		PhotoMapper photoMapper = new PhotoMapper();
		interest.setPhoto(photoMapper.getDomainModel(interestEntity.getPhoto(), fetchChild));
		
		if (fetchChild) {
			interest = getDomainModelChild(interest, interestEntity);
		}
		
		return interest;
	}

	@Override
	public Interest getDomainModelChild(Interest interest, InterestEntity interestEntity) {
		UserMapper userMapper = new UserMapper();
		interest.setUsers(userMapper.getDomainModels(interest.getUsers(), interestEntity.getUsers(), false));
		return interest;
	}

	@Override
	public Collection<Interest> getDomainModels(Collection<Interest> interests, Collection<InterestEntity> interestEntities,
			boolean fetchChild) {
		for (InterestEntity interestEntity: interestEntities) {
			Interest interest = new Interest();
			interest = getDomainModel(interestEntity, fetchChild);
			interests.add(interest);
		}
		return interests;
	}

	@Override
	public Collection<InterestEntity> getEntities(Collection<InterestEntity> interestEntities, Collection<Interest> interests,
			boolean fetchChild) {
		for (Interest interest: interests) {
			InterestEntity interestEntity = new InterestEntity();
			interestEntity = getEntity(interest, fetchChild);
			interestEntities.add(interestEntity);
		}
		return interestEntities;
	}

}
