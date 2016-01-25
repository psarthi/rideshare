package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

public class Form {

	private int id;
	private Collection<String> questions = new HashSet<String>();
	private String remark;
	private boolean emailVerification;
	
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
	public boolean isEmailVerification() {
		return emailVerification;
	}
	public void setEmailVerification(boolean emailVerification) {
		this.emailVerification = emailVerification;
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
