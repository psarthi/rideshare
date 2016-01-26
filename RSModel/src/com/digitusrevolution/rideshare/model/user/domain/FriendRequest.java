package com.digitusrevolution.rideshare.model.user.domain;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.FriendRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class FriendRequest implements DomainModel{
	
	private FriendRequestEntity entity = new FriendRequestEntity();
	private User friend = new User();
	@SuppressWarnings("unused")
	private ZonedDateTime createdDateTime;
	@SuppressWarnings("unused")
	private ApprovalStatus status;
	
	public User getFriend() {
		friend.setEntity(entity.getFriend());
		return friend;
	}
	public void setFriend(User friend) {
		this.friend = friend;
		entity.setFriend(friend.getEntity());
	}
	public ZonedDateTime getCreatedDateTime() {
		return entity.getCreatedDateTime();
	}
	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
		entity.setCreatedDateTime(createdDateTime);
	}
	public ApprovalStatus getStatus() {
		return entity.getStatus();
	}
	public void setStatus(ApprovalStatus status) {
		this.status = status;
		entity.setStatus(status);
	}
	@Override
	public void setUniqueInstanceVariable() {
		//No instance variable, only reference variable
	}
	@Override
	public int hashCode() {
		setUniqueInstanceVariable();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friend == null) ? 0 : friend.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		setUniqueInstanceVariable();
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof FriendRequest)) {
			return false;
		}
		FriendRequest other = (FriendRequest) obj;
		if (friend == null) {
			if (other.friend != null) {
				return false;
			}
		} else if (!friend.equals(other.friend)) {
			return false;
		}
		return true;
	}
	public FriendRequestEntity getEntity() {
		return entity;
	}
	public void setEntity(FriendRequestEntity entity) {
		this.entity = entity;
	}

	
}
