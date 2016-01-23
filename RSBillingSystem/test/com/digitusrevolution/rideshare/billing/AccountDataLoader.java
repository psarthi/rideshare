package com.digitusrevolution.rideshare.billing;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import com.digitusrevolution.rideshare.billing.business.AccountService;
import com.digitusrevolution.rideshare.billing.domain.service.AccountDomainService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

@Path("/domain/loaddata/account")
public class AccountDataLoader {
	
	@GET
	public static void main(String[] args) {
		
		AccountService accountService = new AccountService();
		Account account = new Account();
		account.setBalance(1000);
		
		for(int i=0; i<6;i++){
			accountService.create(account);					
		}
	}

}
