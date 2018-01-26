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

import com.digitusrevolution.rideshare.billing.domain.service.BillDomainService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;

@Path("/domain/bills")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BillDomainResource implements DomainResourceLong<Bill>{

	@Override
	@Secured
	@GET
	@Path("/{number}")
	public Response get(@PathParam("number") long number, @QueryParam("fetchChild") String fetchChild){
		BillDomainService billDomainService = new BillDomainService();
		Bill bill = billDomainService.get(number, Boolean.valueOf(fetchChild));
		return Response.ok(bill).build();
	}

	@Override
	@Secured
	@GET
	public Response getAll(){
	
		BillDomainService billDomainService = new BillDomainService();
		List<Bill> bills = billDomainService.getAll();
		GenericEntity<List<Bill>> entity = new GenericEntity<List<Bill>>(bills) {};
		return Response.ok(entity).build();
	}
}











