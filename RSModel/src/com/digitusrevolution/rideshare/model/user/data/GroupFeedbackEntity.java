package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Vote;

@Embeddable
public class GroupFeedbackEntity{

	@OneToOne
	private UserEntity givenByUser;
	@Column
	@Enumerated(EnumType.STRING)
	private Vote vote;
	
	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public UserEntity getGivenByUser() {
		return givenByUser;
	}

	public void setGivenByUser(UserEntity givenByUser) {
		this.givenByUser = givenByUser;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((givenByUser == null) ? 0 : givenByUser.hashCode());
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
		if (!(obj instanceof GroupFeedbackEntity)) {
			return false;
		}
		GroupFeedbackEntity other = (GroupFeedbackEntity) obj;
		if (givenByUser == null) {
			if (other.givenByUser != null) {
				return false;
			}
		} else if (!givenByUser.equals(other.givenByUser)) {
			return false;
		}
		return true;
	}

}
