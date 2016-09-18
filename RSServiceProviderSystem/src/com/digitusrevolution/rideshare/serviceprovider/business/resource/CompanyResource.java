package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;
import com.digitusrevolution.rideshare.model.serviceprovider.dto.CompanyAccount;
import com.digitusrevolution.rideshare.serviceprovider.business.CompanyService;

@Path("/serviceprovider")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {
	
	@POST
	@Path("/{id}/accounts")
	public Response addAccount(CompanyAccount companyAccount){

		CompanyService companyService = new CompanyService();
		companyService.addAccount(companyAccount);
		return Response.ok().build();
	}

	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild) {
		CompanyService companyService = new CompanyService();
		Company company = companyService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(company).build();
	}


}
