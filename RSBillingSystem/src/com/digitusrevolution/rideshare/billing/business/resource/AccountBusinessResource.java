package com.digitusrevolution.rideshare.billing.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.AccountBusinessService;
import com.digitusrevolution.rideshare.billing.domain.service.AccountDomainService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountBusinessResource {
	
	/**
	 * 
	 * @param billNumber Bill Number of a Ride
	 * @return status OK
	 */
	@GET
	@Path("/{accountNumber}/addmoney/{amount}")
	public Response addMoneyToWallet(@PathParam("accountNumber") int accountNumber, @PathParam("amount") float amount) {
		AccountBusinessService accountBusinessService = new AccountBusinessService();
		accountBusinessService.addMoneyToWallet(accountNumber, amount);
		AccountDomainService accountDomainService = new AccountDomainService();
		Account account = accountDomainService.get(accountNumber, true);
		return Response.ok(account).build();
	}
}
