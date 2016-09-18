package com.digitusrevolution.rideshare.model.serviceprovider.dto;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class CompanyAccount {
	
	private int companyId;
	private Account account;
	public int getCompanyId() {
		return companyId;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}
	
	

}
