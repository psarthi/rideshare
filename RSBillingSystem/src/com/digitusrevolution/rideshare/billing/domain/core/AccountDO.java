package com.digitusrevolution.rideshare.billing.domain.core;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;

public interface AccountDO {

	public void debit(int accountNumber, float amount, Remark remark);
	public void credit(int accountNumber, float amount, Remark remark);
}
