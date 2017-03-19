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
import com.digitusrevolution.rideshare.serviceprovider.business.CompanyBusinessService;

@Path("/serviceprovider")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyBusinessResource {
	
	/**
	 * 
	 * @param companyAccount Account of the company which is different than normal user Account domain model
	 * @return status OK
	 */
	@POST
	@Path("/{id}/accounts")
	public Response addAccount(CompanyAccount companyAccount){

		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		companyBusinessService.addAccount(companyAccount);
		return Response.ok().build();
	}

	/**
	 * 
	 * @param id Id of the company
	 * @param fetchChild value should be either true or false, this value should be passed as query parameter e.g. url?fetchchild=true. True value would return all data and false would return basic data of the model
	 * @return Company domain model
	 */
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") int id, @QueryParam("fetchChild") String fetchChild) {
		CompanyBusinessService companyBusinessService = new CompanyBusinessService();
		Company company = companyBusinessService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(company).build();
	}


}
