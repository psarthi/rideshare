package com.digitusrevolution.rideshare.billing.domain.core;

public interface AccountDO {

	public void debit(int accountNumber, float amount, String remark);
	public void credit(int accountNumber, float amount, String remark);
}
