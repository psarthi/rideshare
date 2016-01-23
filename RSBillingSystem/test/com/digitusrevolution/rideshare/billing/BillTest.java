package com.digitusrevolution.rideshare.billing;

import com.digitusrevolution.rideshare.billing.business.BillingService;

public class BillTest {
	
	public static void main(String[] args) {
		BillingService billingService = new BillingService();
		billingService.approveBill(3);
		billingService.makePayment(3);
	}

}
