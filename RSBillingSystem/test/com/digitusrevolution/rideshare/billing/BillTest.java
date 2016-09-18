package com.digitusrevolution.rideshare.billing;

import com.digitusrevolution.rideshare.billing.business.BillingService;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.dto.BillDTO;

public class BillTest {
	
	public static void main(String[] args) {
		BillingService billingService = new BillingService();
		billingService.approveBill(1);
		BillDTO billDTO = new BillDTO();
		billDTO.setBillNumber(1);
		billDTO.setAccountType(AccountType.Virtual);
		billingService.makePayment(billDTO);
	}

}
