package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.business.RideOfferManagementService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferManagementResource {
	
	@POST
	public Response offerRide(Ride ride){
	
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		int id = rideOfferManagementService.offerRide(ride);
		return Response.ok().entity(Integer.toString(id)).build();
	}

}
