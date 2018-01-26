package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.model.ride.dto.FullRidesInfo;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/users/{userId}/ridesystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemBusinessResource {
	
	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@Secured
	@GET
	@Path("/currentrides")
	public Response getCurrentRide(@PathParam("userId") long userId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FullRidesInfo fullRidesInfo = rideSystemBusinessService.getCurrentRides(userId);
		return Response.ok().entity(fullRidesInfo).build();
	}
	
}
