package com.digitusrevolution.rideshare.billing;

import com.digitusrevolution.rideshare.billing.business.BillingBusinessService;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.billing.dto.BillDTO;

public class BillTest {
	
	public static void main(String[] args) {
		BillingBusinessService billingService = new BillingBusinessService();
		billingService.approveBill(1);
		BillDTO billDTO = new BillDTO();
		billDTO.setBillNumber(1);
		billDTO.setAccountType(AccountType.Virtual);
		billingService.makePayment(billDTO);
	}

}
