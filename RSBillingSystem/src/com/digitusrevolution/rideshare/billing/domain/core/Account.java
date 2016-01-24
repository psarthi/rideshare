package com.digitusrevolution.rideshare.billing.domain.core;

public interface Account {

	public void debit(int accountNumber, float amount, String remark);
	public void credit(int accountNumber, float amount, String remark);
}
