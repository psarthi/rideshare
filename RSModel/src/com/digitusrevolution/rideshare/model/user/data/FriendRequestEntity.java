package com.digitusrevolution.rideshare.model.user.data;

import java.time.ZonedDateTime;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;

@Embeddable
public class FriendRequestEntity {
	
	@OneToOne
	private UserEntity friend;
	private ZonedDateTime createdDateTime;
	@Column
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;
	

	public UserEntity getFriend() {
		return friend;
	}
	public void setFriend(UserEntity friend) {
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
		if (!(obj instanceof FriendRequestEntity)) {
			return false;
		}
		FriendRequestEntity other = (FriendRequestEntity) obj;
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
