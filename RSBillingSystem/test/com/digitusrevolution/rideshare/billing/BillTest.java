package com.digitusrevolution.rideshare.billing;

import com.digitusrevolution.rideshare.billing.business.BillingBusinessService;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.dto.BillInfo;

public class BillTest {
	
	public static void main(String[] args) {
		BillingBusinessService billingService = new BillingBusinessService();
		billingService.approveBill(1);
		BillInfo billInfo = new BillInfo();
		billInfo.setBillNumber(1);
		billInfo.setAccountType(AccountType.Virtual);
		billingService.makePayment(billInfo);
	}

}
