package com.digitusrevolution.rideshare.billing.domain.core;

import com.digitusrevolution.rideshare.model.billing.domain.core.Remark;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public interface AccountDO {

	public Transaction debit(User user, long accountNumber, float amount, Remark remark);
	public Transaction credit(User user, long accountNumber, float amount, Remark remark);
}
