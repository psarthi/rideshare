package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.business.RideRequestManagementService;

@Path("/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestManagementResource {
	
	@POST
	public Response requestRide(RideRequest rideRequest){
		
		RideRequestManagementService rideRequestManagementService = new RideRequestManagementService();
		int id = rideRequestManagementService.requestRide(rideRequest);
		return Response.ok().entity(Integer.toString(id)).build();
		
	}

}
