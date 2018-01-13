package com.digitusrevolution.rideshare.model.user.data;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Table;

//Reason for creating entity and not using embedded as you can't have element collection inside embedded
@Entity
@Table(name="group_membership_form")
public class FormEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	//This would be visible to all group members e.g. employee id, flat number etc
	private String userUniqueIdentifierName;
	//IMP - Its important to set fetch type as EAGER, else it fails to load when you are trying to get Forms while getting user
	//but this will not have an issue if you get group directly, so we need to set the fetch type here so that it works from everywhere
	@ElementCollection(fetch = FetchType.EAGER)
	@JoinTable(name="group_membership_form_question",joinColumns=@JoinColumn(name="form_id"))
	private Collection<String> questions = new HashSet<String>();
	private String remark;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public Collection<String> getQuestions() {
		return questions;
	}
	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((userUniqueIdentifierName == null) ? 0 : userUniqueIdentifierName.hashCode());
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
		if (!(obj instanceof FormEntity)) {
			return false;
		}
		FormEntity other = (FormEntity) obj;
		if (id != other.id) {
			return false;
		}
		if (questions == null) {
			if (other.questions != null) {
				return false;
			}
		} else if (!questions.equals(other.questions)) {
			return false;
		}
		if (remark == null) {
			if (other.remark != null) {
				return false;
			}
		} else if (!remark.equals(other.remark)) {
			return false;
		}
		if (userUniqueIdentifierName == null) {
			if (other.userUniqueIdentifierName != null) {
				return false;
			}
		} else if (!userUniqueIdentifierName.equals(other.userUniqueIdentifierName)) {
			return false;
		}
		return true;
	}
	
	
	
}
