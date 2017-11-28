package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;

@Path("/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestBusinessResource {

	/**
	 * 
	 * @param Basic Ride Request domain model
	 * @return RideRquestResult having created Ride Request and additional information
	 */
	@POST
	public Response requestRide(BasicRideRequest rideRequest){
	
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		int id = rideRequestBusinessService.requestRide(rideRequest);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideRequestResult rideRequestResult = rideRequestBusinessService.getRideReuqestResult(id);
		return Response.ok(rideRequestResult).build();
	}
	
}
