package com.digitusrevolution.rideshare.billing.domain.core;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;

public interface AccountDO {

	public void debit(long accountNumber, float amount, Remark remark);
	public void credit(long accountNumber, float amount, Remark remark);
}
