package com.digitusrevolution.rideshare.model.user.data;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.core.GroupEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.ApprovalStatus;
import com.digitusrevolution.rideshare.model.user.domain.core.Group;

//Reason for creating entity and not using embedded as you can't have element collection inside embedded
@Entity
@Table(name="membership_request")
public class MembershipRequestEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String userUniqueIdentifier;
	@ElementCollection(fetch=FetchType.EAGER)
	@JoinTable(name="membership_request_answer",joinColumns=@JoinColumn(name="request_id"))
	@MapKeyColumn(name="question")
	@Column(name="answer")
	private Map<String, String> questionAnswers = new HashMap<String, String>();
	@OneToOne
	private UserEntity user;
	@OneToOne
	private GroupEntity group; 
	@Column
	@Enumerated(EnumType.STRING)
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
	public UserEntity getUser() {
		return user;
	}
	public void setUser(UserEntity user) {
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
		if (!(obj instanceof MembershipRequestEntity)) {
			return false;
		}
		MembershipRequestEntity other = (MembershipRequestEntity) obj;
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
	public GroupEntity getGroup() {
		return group;
	}
	public void setGroup(GroupEntity group) {
		this.group = group;
	}
}
