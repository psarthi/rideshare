package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.user.domain.Vote;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class GroupFeedbackInfo{

	private BasicUser givenByUser;
	private Vote vote;

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public BasicUser getGivenByUser() {
		return givenByUser;
	}

	public void setGivenByUser(BasicUser givenByUser) {
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
		if (!(obj instanceof GroupFeedbackInfo)) {
			return false;
		}
		GroupFeedbackInfo other = (GroupFeedbackInfo) obj;
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
