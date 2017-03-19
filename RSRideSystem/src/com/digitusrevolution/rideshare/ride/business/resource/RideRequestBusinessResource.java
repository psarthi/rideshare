package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestBusinessResource {
	
	/**
	 * 
	 * @param rideRequest Ride Request domain model
	 * @return FeatureCollection containing requested ride information
	 */
	@POST
	public Response requestRide(RideRequest rideRequest){
		
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		int id = rideRequestBusinessService.requestRide(rideRequest);
		RideSystemBusinessService rideSystemService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemService.getRideRequestPoints(id);
		return Response.ok().entity(featureCollection).build();
		
	}
	
	/**
	 * 
	 * @param rideId Ride Id for which we need to search ride requests 
	 * @param lastSearchDistance Last Search distance which is the distance of polygon from the route, For the first time request it should be 0
	 *  					  and subsequently this should be the value returned by previous result set, so that we don't start the search from scratch 
	 * @param lastResultIndex Last result index value, for the first time request it would be 0 and for subsequent request it should be actual value 
	 * 					   returned by previous result. This is used for pagination and providing incremental results instead of all at one go
	 * @return FeatureCollection containing matched ride requests
	 */
	@GET
	@Path("/search/{rideId}/{lastSearchDistance}/{lastResultIndex}")
	public Response getMatchingRideRequests(@PathParam("rideId") int rideId, @PathParam("lastSearchDistance")  double lastSearchDistance, 
											@PathParam("lastResultIndex") int lastResultIndex){
	
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		FeatureCollection featureCollection = rideRequestBusinessService.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
		return Response.ok().entity(featureCollection).build();

	}

}
