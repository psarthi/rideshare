package com.digitusrevolution.rideshare.billing;

import com.digitusrevolution.rideshare.billing.business.AccountService;
import com.digitusrevolution.rideshare.billing.domain.service.AccountDomainService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

public class AccountTest {
	
	public static void main(String[] args) {
		
		AccountService accountService = new AccountService();
		Account account = new Account();
	//	account.setBalance(1000);
	//	accountService.create(account);
		
		AccountDomainService accountDomainService = new AccountDomainService();
		account = accountDomainService.get(1, true);
		
	}

}
