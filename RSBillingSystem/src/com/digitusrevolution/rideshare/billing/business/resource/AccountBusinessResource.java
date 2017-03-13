package com.digitusrevolution.rideshare.billing.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.business.AccountBusinessService;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountBusinessResource {

	@POST
	public Response create(Account account){
		
		AccountBusinessService accountBusinessService = new AccountBusinessService();
		int number = accountBusinessService.create(account);
		return Response.ok(Integer.toString(number)).build();
	}
}
