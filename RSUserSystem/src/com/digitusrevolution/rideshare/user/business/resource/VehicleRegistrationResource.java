package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleRegistrationResource {

	private VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
	
}
