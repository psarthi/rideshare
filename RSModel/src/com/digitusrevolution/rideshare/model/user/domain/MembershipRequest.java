package com.digitusrevolution.rideshare.model.user.domain;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class MembershipRequest implements Comparable<MembershipRequest>{

	private long id;
	private User user;
	private Group group; 
	//This would be visible to all group members e.g. employee id, flat number etc
	private String userUniqueIdentifier;
	private Map<String, String> questionAnswers = new HashMap<String, String>();
	private ApprovalStatus status;
	private ZonedDateTime createdDateTime;
	private String adminRemark;
	private String userRemark;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public ApprovalStatus getStatus() {
		return status;
	}
	public void setStatus(ApprovalStatus status) {
		this.status = status;
	}
	public ZonedDateTime getCreatedDateTime() {
		return createdDateTime;
	}
	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
	}
	public String getUserUniqueIdentifier() {
		return userUniqueIdentifier;
	}
	public void setUserUniqueIdentifier(String userUniqueIdentifier) {
		this.userUniqueIdentifier = userUniqueIdentifier;
	}
	public Map<String, String> getQuestionAnswers() {
		return questionAnswers;
	}
	public void setQuestionAnswers(Map<String, String> questionAnswers) {
		this.questionAnswers = questionAnswers;
	}
	public String getUserRemark() {
		return userRemark;
	}
	public void setUserRemark(String userRemark) {
		this.userRemark = userRemark;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof MembershipRequest)) {
			return false;
		}
		MembershipRequest other = (MembershipRequest) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	public String getAdminRemark() {
		return adminRemark;
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}
	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}

	@Override
	public int compareTo(MembershipRequest o) {
		//descending order
		return (int) (o.id - this.id);
	}
}
