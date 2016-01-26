package com.digitusrevolution.rideshare.model.user.domain;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.MembershipRequestEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class MembershipRequest implements DomainModel{

	private MembershipRequestEntity entity = new MembershipRequestEntity();
	private int id;
	private User user = new User();
	@SuppressWarnings("unused")
	private Collection<String> answers = new HashSet<String>();
	@SuppressWarnings("unused")
	private ApprovalStatus status;
	@SuppressWarnings("unused")
	private EmailVerificationStatus emailVerificationStatus;
	@SuppressWarnings("unused")
	private String emailForVerification;
	@SuppressWarnings("unused")
	private ZonedDateTime createdDateTime;
	@SuppressWarnings("unused")
	private String adminRemark;
	
	public User getUser() {
		user.setEntity(entity.getUser());
		return user;
	}
	public void setUser(User user) {
		this.user = user;
		entity.setUser(user.getEntity());
	}
	public Collection<String> getAnswers() {
		return entity.getAnswers();
	}
	public void setAnswers(Collection<String> answers) {
		this.answers = answers;
		entity.setAnswers(answers);
	}
	public ApprovalStatus getStatus() {
		return entity.getStatus();
	}
	public void setStatus(ApprovalStatus status) {
		this.status = status;
		entity.setStatus(status);
	}
	public ZonedDateTime getCreatedDateTime() {
		return entity.getCreatedDateTime();
	}
	public void setCreatedDateTime(ZonedDateTime createdDateTime) {
		this.createdDateTime = createdDateTime;
		entity.setCreatedDateTime(createdDateTime);
	}
	public int getId() {
		return entity.getId();
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public EmailVerificationStatus getEmailVerificationStatus() {
		return entity.getEmailVerificationStatus();
	}
	public void setEmailVerificationStatus(EmailVerificationStatus emailVerificationStatus) {
		this.emailVerificationStatus = emailVerificationStatus;
		entity.setEmailVerificationStatus(emailVerificationStatus);
	}
	public String getEmailForVerification() {
		return entity.getEmailForVerification();
	}
	public void setEmailForVerification(String emailForVerification) {
		this.emailForVerification = emailForVerification;
		entity.setEmailForVerification(emailForVerification);
	}
	public String getAdminRemark() {
		return entity.getAdminRemark();
	}
	public void setAdminRemark(String adminRemark) {
		this.adminRemark = adminRemark;
		entity.setAdminRemark(adminRemark);
	}
	@Override
	public void setUniqueInstanceVariable() {
		id = getId();
	}
	@Override
	public int hashCode() {
		setUniqueInstanceVariable();
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
	public MembershipRequestEntity getEntity() {
		return entity;
	}
	public void setEntity(MembershipRequestEntity entity) {
		this.entity = entity;
	}
}
