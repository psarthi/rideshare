package com.digitusrevolution.rideshare.billing.domain.core;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;

public interface AccountDO {

	public long debit(long accountNumber, float amount, Remark remark);
	public long credit(long accountNumber, float amount, Remark remark);
}
