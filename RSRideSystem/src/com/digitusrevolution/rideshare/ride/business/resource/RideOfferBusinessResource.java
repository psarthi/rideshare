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
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferDTO;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferBusinessResource {
	
	/**
	 * 
	 * @param rideOfferDTO Ride domain model with additional information e.g. google Direction
	 * @return FeatureCollection containing offered ride information
	 */
	@POST
	public Response offerRide(RideOfferDTO rideOfferDTO){
	
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		List<Integer> rideIds = rideOfferBusinessService.offerRide(rideOfferDTO);
		FeatureCollection featureCollection = new FeatureCollection();
		for (Integer id : rideIds) {
			RideSystemBusinessService rideSystemBusinessService = new  RideSystemBusinessService();
			FeatureCollection rideFeatureCollection = rideSystemBusinessService.getRidePoints(id);
			featureCollection.addAll(rideFeatureCollection.getFeatures());
		}
		return Response.ok().entity(featureCollection).build();
	}
	
	/**
	 * 
	 * @param driverId Id of the driver
	 * @return list of rides
	 */
	@GET
	@Path("/upcoming/{driverId}")
	public Response getUpcomingRides(@PathParam("driverId") int driverId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		List<Ride> upcomingRides = rideOfferBusinessService.getUpcomingRides(driverId);
		return Response.ok().entity(upcomingRides).build();
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing matched rides information
	 */
	@GET
	@Path("/search/{rideRequestId}")
	public Response getMatchingRides(@PathParam("rideRequestId") int rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		FeatureCollection featureCollection = rideOfferBusinessService.getMatchingRides(rideRequestId);
		return Response.ok(featureCollection).build();		
	}
	
	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return status OK
	 */
	@POST
	@Path("/accept/{rideId}/{rideRequestId}")
	public Response acceptRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.acceptRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return status OK
	 */
	@POST
	@Path("/reject/{rideId}/{rideRequestId}")
	public Response rejectRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.rejectRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

}




































