package com.digitusrevolution.rideshare.model.user.domain;

import java.time.ZonedDateTime;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class FriendRequest {
	
	private User friend;
	private ZonedDateTime createdDateTime;
	private ApprovalStatus status;
	
	public User getFriend() {
		return friend;
	}
	public void setFriend(User friend) {
		this.friend = friend;
	}
	public ZonedDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public ApprovalStatus getStatus() {
		return status;
	}
	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friend == null) ? 0 : friend.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
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
	
}
