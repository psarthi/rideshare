package com.digitusrevolution.rideshare.billing.domain.core;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;

public interface AccountDO {

	public Transaction debit(long accountNumber, float amount, Remark remark);
	public Transaction credit(long accountNumber, float amount, Remark remark);
}
