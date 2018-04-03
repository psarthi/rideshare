package com.digitusrevolution.rideshare.model.billing.data.core;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.digitusrevolution.rideshare.model.billing.domain.core.Purpose;

@Embeddable
public class RemarkEntity {
	
	@Column
	@Enumerated(EnumType.STRING)
	private Purpose purpose;
	private String message;
	private String paidBy;
	private String paidTo;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPaidBy() {
		return paidBy;
	}
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public String getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}
	public Purpose getPurpose() {
		return purpose;
	}
	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}
}
