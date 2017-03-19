package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/ridesystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemBusinessResource {

	/**
	 * 
	 * @return FeatureCollection containing all rides of the system
	 */
	@GET
	@Path("/rides/allpoints")
	public Response getAllRidePoints(){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getAllRidePoints();
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return FeatureCollection containing specific ride information
	 */
	@GET
	@Path("/ride/route/{rideId}")
	public Response getRidePoints(@PathParam("rideId") int rideId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getRidePoints(rideId);
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @return FeatureCollection containing all ride requests of the system
	 */
	@GET
	@Path("/riderequests/allpoints")
	public Response getAllRideRequestPoints(){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getAllRideRequestPoints();
		return Response.ok(featureCollection).build();
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing specific ride request information
	 */
	@GET
	@Path("/riderequests/{rideRequestId}")
	public Response getRideRequestPoints(@PathParam("rideRequestId") int rideRequestId) {
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getRideRequestPoints(rideRequestId);
		return Response.ok(featureCollection).build();
	}
	


}
