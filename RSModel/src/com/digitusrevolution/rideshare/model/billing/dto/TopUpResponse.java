package com.digitusrevolution.rideshare.model.billing.dto;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class TopUpResponse {

	private Account account;
	private String message;
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
}
