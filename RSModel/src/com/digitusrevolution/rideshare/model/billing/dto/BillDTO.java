package com.digitusrevolution.rideshare.model.billing.dto;

import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;

public class BillDTO {
	private int billNumber;
	private AccountType accountType;
	public int getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}
	public AccountType getAccountType() {
		return accountType;
	}
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}
	
}
