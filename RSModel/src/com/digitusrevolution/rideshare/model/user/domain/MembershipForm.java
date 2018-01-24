package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

public class MembershipForm {

	private long id;
	//This would be visible to all group members e.g. employee id, flat number etc
	//This should not be changed but if required then all members needs to update their identifier details
	private String userUniqueIdentifierName;
	private Collection<String> questions = new HashSet<String>();
	private String remark;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Collection<String> getQuestions() {
		return questions;
	}
	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getUserUniqueIdentifierName() {
		return userUniqueIdentifierName;
	}
	public void setUserUniqueIdentifierName(String userUniqueIdentifierName) {
		this.userUniqueIdentifierName = userUniqueIdentifierName;
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
		if (!(obj instanceof MembershipForm)) {
			return false;
		}
		MembershipForm other = (MembershipForm) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
}
