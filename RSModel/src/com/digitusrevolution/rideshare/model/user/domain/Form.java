package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.FormEntity;

public class Form implements DomainModel{

	private FormEntity entity = new FormEntity();
	private int id;
	private Collection<String> questions = new HashSet<String>();
	private String remark;
	private boolean emailVerification;
	//This will store domain name, so that we can verify if the email belong to the same domain
	@SuppressWarnings("unused")
	private String emailDomain;
	
	public Collection<String> getQuestions() {
		return entity.getQuestions();
	}
	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
		entity.setQuestions(questions);
	}
	public String getRemark() {
		return entity.getRemark();
	}
	public void setRemark(String remark) {
		this.remark = remark;
		entity.setRemark(remark);
	}
	public boolean isEmailVerification() {
		return entity.isEmailVerification();
	}
	public void setEmailVerification(boolean emailVerification) {
		this.emailVerification = emailVerification;
		entity.setEmailVerification(emailVerification);
	}
	public int getId() {
		return entity.getId();
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getEmailDomain() {
		return entity.getEmailDomain();
	}
	public void setEmailDomain(String emailDomain) {
		this.emailDomain = emailDomain;
		entity.setEmailDomain(emailDomain);
	}
	public FormEntity getEntity() {
		return entity;
	}
	public void setEntity(FormEntity entity) {
		this.entity = entity;
	}
	@Override
	public void setUniqueInstanceVariable() {
		emailVerification = isEmailVerification();
		id = getId();
		questions = getQuestions();
		remark = getRemark();
	}
	@Override
	public int hashCode() {
		setUniqueInstanceVariable();
		final int prime = 31;
		int result = 1;
		result = prime * result + (emailVerification ? 1231 : 1237);
		result = prime * result + id;
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
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
		if (!(obj instanceof Form)) {
			return false;
		}
		Form other = (Form) obj;
		if (emailVerification != other.emailVerification) {
			return false;
		}
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
		return true;
	}

}
