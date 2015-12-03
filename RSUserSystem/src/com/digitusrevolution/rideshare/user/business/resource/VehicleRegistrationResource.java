package com.digitusrevolution.rideshare.user.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.business.VehicleRegistrationService;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class VehicleRegistrationResource {


	@POST
	public Response addVehicle(@PathParam("id") int userId, Vehicle vehicle){

		VehicleRegistrationService vehicleRegistrationService = new VehicleRegistrationService();
		vehicleRegistrationService.addVehicle(userId, vehicle);			
		return Response.ok().build();
	}

}
