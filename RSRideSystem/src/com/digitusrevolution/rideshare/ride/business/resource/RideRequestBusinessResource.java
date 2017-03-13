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
	
	@POST
	public Response requestRide(RideRequest rideRequest){
		
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		int id = rideRequestBusinessService.requestRide(rideRequest);
		RideSystemBusinessService rideSystemService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemService.getRideRequestPoints(id);
		return Response.ok().entity(featureCollection).build();
		
	}
	
	@GET
	@Path("/search/{rideId}/{lastSearchDistance}/{lastResultIndex}")
	public Response getMatchingRideRequests(@PathParam("rideId") int rideId, @PathParam("lastSearchDistance")  double lastSearchDistance, 
											@PathParam("lastResultIndex") int lastResultIndex){
	
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		FeatureCollection featureCollection = rideRequestBusinessService.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
		return Response.ok().entity(featureCollection).build();

	}

}
