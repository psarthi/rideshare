package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class UserAccount {
	
	private int userId;
	private Account account;
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
