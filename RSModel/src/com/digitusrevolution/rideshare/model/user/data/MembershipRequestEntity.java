package com.digitusrevolution.rideshare.model.user.data;

import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;

//Reason for creating entity and not using embedded as you can't have element collection inside embedded
@Entity
@Table(name="membership_request")
public class MembershipRequestEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@ElementCollection
	@JoinTable(name="membership_request_answer",joinColumns=@JoinColumn(name="request_id"))
	private Collection<String> answers = new HashSet<String>();
	@OneToOne
	private UserEntity user;
	@Column
	@Enumerated(EnumType.STRING)
	private ApprovalStatus status;
	private ZonedDateTime createdDateTime;
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
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
		if (!(obj instanceof MembershipRequestEntity)) {
			return false;
		}
		MembershipRequestEntity other = (MembershipRequestEntity) obj;
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
}
