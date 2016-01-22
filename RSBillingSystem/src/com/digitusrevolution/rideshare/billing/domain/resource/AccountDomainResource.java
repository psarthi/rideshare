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
import com.digitusrevolution.rideshare.common.inf.DomainResource;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;

@Path("/domain/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountDomainResource implements DomainResource<Account>{

	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild){
		AccountDomainService accountDomainService = new AccountDomainService();
		Account account = accountDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(account).build();
	}

	@Override
	@GET
	public Response getAll(){
		
		AccountDomainService accountDomainService = new AccountDomainService();
		List<Account> accounts = accountDomainService.getAll();
		GenericEntity<List<Account>> entity = new GenericEntity<List<Account>>(accounts) {};
		return Response.ok(entity).build();
	}
}
