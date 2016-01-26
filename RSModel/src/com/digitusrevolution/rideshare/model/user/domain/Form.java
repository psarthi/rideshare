package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.data.FormEntity;

public class Form{

	private FormEntity entity = new FormEntity();
	private int id;
	private Collection<String> questions = new HashSet<String>();
	private String remark;
	private boolean emailVerification;
	//This will store domain name, so that we can verify if the email belong to the same domain
	private String emailDomain;
	
	public Collection<String> getQuestions() {
		questions = entity.getQuestions();
		return questions;
	}
	public void setQuestions(Collection<String> questions) {
		this.questions = questions;
		entity.setQuestions(questions);
	}
	public String getRemark() {
		remark = entity.getRemark();
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
		entity.setRemark(remark);
	}
	public boolean isEmailVerification() {
		emailVerification = entity.isEmailVerification();
		return emailVerification;
	}
	public void setEmailVerification(boolean emailVerification) {
		this.emailVerification = emailVerification;
		entity.setEmailVerification(emailVerification);
	}
	public int getId() {
		id = entity.getId();
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getEmailDomain() {
		emailDomain = entity.getEmailDomain();
		return emailDomain;
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
	public int hashCode() {
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
