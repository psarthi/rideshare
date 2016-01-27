package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.GroupFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class GroupFeedback implements DomainModel{

	private GroupFeedbackEntity entity = new GroupFeedbackEntity();
	private User givenByUser = new User();
	private Vote vote;

	public Vote getVote() {
		return vote;
	}

	public void setVote(Vote vote) {
		this.vote = vote;
		entity.setVote(vote);
	}

	public User getGivenByUser() {
		givenByUser.setEntity(entity.getGivenByUser());
		return givenByUser;
	}

	public void setGivenByUser(User givenByUser) {
		this.givenByUser = givenByUser;
		entity.setGivenByUser(givenByUser.getEntity());
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

	public GroupFeedbackEntity getEntity() {
		return entity;
	}

	public void setEntity(GroupFeedbackEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}

	@Override
	public void setDomainModelPrimitiveVariable() {
		vote = entity.getVote();
		
	}

}
