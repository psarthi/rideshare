package com.digitusrevolution.rideshare.model.user.domain;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class MembershipRequest {

	private int id;
	private User user;
	private Collection<String> answers = new HashSet<String>();
	private ApprovalStatus status;
	private EmailVerificationStatus emailVerificationStatus;
	private String emailForVerification;
	private ZonedDateTime createdDateTime;
	private String adminRemark;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public Collection<String> getAnswers() {
		return answers;
	}
	public void setAnswers(Collection<String> answers) {
		this.answers = answers;
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
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		if (user == null) {
			if (other.user != null) {
				return false;
			}
		} else if (!user.equals(other.user)) {
			return false;
		}
		return true;
	}
	public EmailVerificationStatus getEmailVerificationStatus() {
		return emailVerificationStatus;
	}
	public void setEmailVerificationStatus(EmailVerificationStatus emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
	}
	public String getEmailForVerification() {
		return emailForVerification;
	}
	public void setEmailForVerification(String emailForVerification) {
		this.emailForVerification = emailForVerification;
	}
	public String getAdminRemark() {
		return adminRemark;
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
	}
}
