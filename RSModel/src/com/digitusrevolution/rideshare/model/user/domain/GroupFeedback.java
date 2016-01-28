package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class GroupFeedback{

	private User givenByUser;
	private Vote vote;

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
	}

	public User getGivenByUser() {
		return givenByUser;
	}

	public void setGivenByUser(User givenByUser) {
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
		if (!(obj instanceof GroupFeedback)) {
			return false;
		}
		GroupFeedback other = (GroupFeedback) obj;
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
