package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.FriendRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.FriendRequest;

public class FriendRequestMapper implements Mapper<FriendRequest, FriendRequestEntity>{

	@Override
	public FriendRequestEntity getEntity(FriendRequest friendRequest, boolean fetchChild) {
		FriendRequestEntity friendRequestEntity = new FriendRequestEntity();
		friendRequestEntity.setCreatedDateTime(friendRequest.getCreatedDateTime());
		friendRequestEntity.setStatus(friendRequest.getStatus());
		UserMapper userMapper = new UserMapper();
		//We don't need user child, just basic user is fine
		friendRequestEntity.setFriend(userMapper.getEntity(friendRequest.getFriend(), false));
		return friendRequestEntity;
	}

	@Override
	public FriendRequestEntity getEntityChild(FriendRequest friendRequest, FriendRequestEntity friendRequestEntity) {
		return friendRequestEntity;
	}

	@Override
	public FriendRequest getDomainModel(FriendRequestEntity friendRequestEntity, boolean fetchChild) {
		FriendRequest friendRequest = new FriendRequest();
		friendRequest.setCreatedDateTime(friendRequestEntity.getCreatedDateTime());
		friendRequest.setStatus(friendRequestEntity.getStatus());
		UserMapper userMapper = new UserMapper();
		//We don't need user child, just basic user is fine
		friendRequest.setFriend(userMapper.getDomainModel(friendRequestEntity.getFriend(), false));
		return friendRequest;
	}

	@Override
	public FriendRequest getDomainModelChild(FriendRequest friendRequest, FriendRequestEntity entity) {
		return friendRequest;
	}

	@Override
	public Collection<FriendRequest> getDomainModels(Collection<FriendRequest> friendRequests,
			Collection<FriendRequestEntity> friendRequestEntities, boolean fetchChild) {
		for (FriendRequestEntity friendRequestEntity : friendRequestEntities) {
			FriendRequest friendRequest = new FriendRequest();
			friendRequest = getDomainModel(friendRequestEntity, fetchChild);
			friendRequests.add(friendRequest);
		}
		return friendRequests;
	}

	@Override
	public Collection<FriendRequestEntity> getEntities(Collection<FriendRequestEntity> friendRequestEntities,
			Collection<FriendRequest> friendRequests, boolean fetchChild) {
		for (FriendRequest friendRequest : friendRequests) {
			FriendRequestEntity friendRequestEntity = new FriendRequestEntity();
			friendRequestEntity = getEntity(friendRequest, fetchChild);
			friendRequestEntities.add(friendRequestEntity);
		}
		return friendRequestEntities;
	}

}
