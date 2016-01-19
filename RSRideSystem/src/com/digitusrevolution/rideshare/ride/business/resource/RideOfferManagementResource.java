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

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.business.RideOfferManagementService;
import com.digitusrevolution.rideshare.ride.business.RideSystemService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferManagementResource {
	
	@POST
	public Response offerRide(Ride ride){
	
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		List<Integer> rideIds = rideOfferManagementService.offerRide(ride);
		FeatureCollection featureCollection = new FeatureCollection();
		for (Integer id : rideIds) {
			RideSystemService rideSystemService = new  RideSystemService();
			FeatureCollection rideFeatureCollection = rideSystemService.getRidePoints(id);
			featureCollection.addAll(rideFeatureCollection.getFeatures());
		}
		return Response.ok().entity(featureCollection).build();
	}
	
	@GET
	@Path("/upcoming/{driverId}")
	public Response getUpcomingRides(@PathParam("driverId") int driverId){
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		List<Ride> upcomingRides = rideOfferManagementService.getUpcomingRides(driverId);
		return Response.ok().entity(upcomingRides).build();
	}
	
	@GET
	@Path("/search/{rideRequestId}")
	public Response getMatchingRides(@PathParam("rideRequestId") int rideRequestId){
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		FeatureCollection featureCollection = rideOfferManagementService.getMatchingRides(rideRequestId);
		return Response.ok(featureCollection).build();		
	}
	
	@GET
	@Path("/accept/{rideId}/{rideRequestId}")
	public Response acceptRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		rideOfferManagementService.acceptRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

	@GET
	@Path("/reject/{rideId}/{rideRequestId}")
	public Response rejectRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		rideOfferManagementService.rejectRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

}




































