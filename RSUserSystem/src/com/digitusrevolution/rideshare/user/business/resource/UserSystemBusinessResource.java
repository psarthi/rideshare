package com.digitusrevolution.rideshare.user.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.user.business.UserSystemBusinessService;

@Path("/usersystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserSystemBusinessResource {
	
	@GET
	@Path("/countries")
	public Response getCountries() {
		UserSystemBusinessService userSystemBusinessService = new UserSystemBusinessService();
		List<Country> countries = userSystemBusinessService.getCountries();
		
		GenericEntity<List<Country>> entity = new GenericEntity<List<Country>>(countries) {};
		return Response.ok(entity).build();
	}

}
