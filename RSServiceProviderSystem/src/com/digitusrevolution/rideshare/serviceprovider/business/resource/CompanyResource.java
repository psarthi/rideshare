package com.digitusrevolution.rideshare.serviceprovider.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.serviceprovider.business.CompanyService;
import com.digitusrevolution.rideshare.serviceprovider.dto.CompanyAccount;

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


}
