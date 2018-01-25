package com.digitusrevolution.rideshare.user.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
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
	
	@Secured
	@GET
	@Path("/vehiclecategores")
	public Response getVehicleCategories(@Context ContainerRequestContext requestContext, @PathParam("id") long userId) {
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			UserSystemBusinessService userSystemBusinessService = new UserSystemBusinessService();
			 List<VehicleCategory> vehicleCategories = userSystemBusinessService.getVehicleCategories();
			
			GenericEntity<List<VehicleCategory>> entity = new GenericEntity<List<VehicleCategory>>(vehicleCategories) {};
			return Response.ok(entity).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}


}
