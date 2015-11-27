package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;
import com.digitusrevolution.rideshare.user.business.dto.VehicleDTO;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleRegistrationResource {

	private VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
	
	@POST
	public Response addVehicle(VehicleDTO vehicleDTO){
		vehicleRegistrationService.addVehicle(vehicleDTO);
		return Response.ok().build();
	}

	
}
