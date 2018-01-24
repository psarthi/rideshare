package com.digitusrevolution.rideshare.user.domain.resource;

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

import com.digitusrevolution.rideshare.common.inf.DomainResourceInteger;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.user.domain.service.CurrencyDomainService;

@Path("/domain/currencies")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CurrencyDomainResource implements DomainResourceInteger<Currency>{
	
	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild){
		
		CurrencyDomainService currencyDomainService = new CurrencyDomainService();
		Currency currency = currencyDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(currency).build();
	}

	@Override
	@GET
	public Response getAll(){
		
		CurrencyDomainService currencyDomainService = new CurrencyDomainService();
		List<Currency> currencies = currencyDomainService.getAll();
		GenericEntity<List<Currency>> entity = new GenericEntity<List<Currency>>(currencies) {};
		return Response.ok(entity).build();
	}


}
