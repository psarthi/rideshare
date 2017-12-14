package com.digitusrevolution.rideshare.ride.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/ridesystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemBusinessResource {
	
	/**
	 * 
	 * @param driverId Id of the driver
	 * @return list of rides
	 */
	@GET
	@Path("/ride/allupcoming/{driverId}")
	public Response getAllUpcomingRides(@PathParam("driverId") int driverId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		List<Ride> upcomingRides = rideSystemBusinessService.getAllUpcomingRides(driverId);
		return Response.ok().entity(upcomingRides).build();
	}

	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@GET
	@Path("/ride/current/{driverId}")
	public Response getCurrentRide(@PathParam("driverId") int driverId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		Ride upcomingRide = rideSystemBusinessService.getCurrentRide(driverId);
		return Response.ok().entity(upcomingRide).build();
	}

	
	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return status OK
	 */
	@POST
	@Path("/ride/reject/{rideId}/{rideRequestId}")
	public Response rejectRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		rideSystemBusinessService.rejectRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}


	
	/**
	 * 
	 * @param passengerId Id of the User
	 * @return current ride request
	 */
	@GET
	@Path("/riderequest/current/{passengerId}")
	public Response getUpcomingRideRequest(@PathParam("passengerId") int passengerId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		RideRequest upcomingRideRequest = rideSystemBusinessService.getCurrentRideRequest(passengerId);
		return Response.ok().entity(upcomingRideRequest).build();
	}

}
