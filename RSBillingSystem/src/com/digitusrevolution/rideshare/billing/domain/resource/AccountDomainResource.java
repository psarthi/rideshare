package com.digitusrevolution.rideshare.billing.domain.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.billing.domain.service.AccountDomainService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

@Path("/domain/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountDomainResource implements DomainResourceLong<Account>{
	
	/**
	 * 
	 * @param account User Account
	 * @return accountNumber
	 */
	@Secured
	@GET
	@Path("/create")
	public Response createVirtualAccount(){
		
		AccountDomainService accountDomainService = new AccountDomainService();
		long number = accountDomainService.createVirtualAccount();
		//Reason for doing get post create so that we get different transaction
		Account account = accountDomainService.get(number, true);
		return Response.ok(account).build();
	}

	@Override
	@Secured
	@GET
	@Path("/{number}")
	public Response get(@PathParam("number") long number, @QueryParam("fetchChild") String fetchChild){
		AccountDomainService accountDomainService = new AccountDomainService();
		Account account = accountDomainService.get(number, Boolean.valueOf(fetchChild));
		return Response.ok(account).build();
	}

	@Override
	@Secured
	@GET
	public Response getAll(){
		
		AccountDomainService accountDomainService = new AccountDomainService();
		List<Account> accounts = accountDomainService.getAll();
		GenericEntity<List<Account>> entity = new GenericEntity<List<Account>>(accounts) {};
		return Response.ok(entity).build();
	}
}
