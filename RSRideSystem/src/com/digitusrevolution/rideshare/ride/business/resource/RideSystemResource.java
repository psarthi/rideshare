package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.ride.business.RideSystemService;

@Path("/ridesystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemResource {

	@GET
	@Path("/rides/allpoints")
	public Response getAllRidePoints(){
		RideSystemService rideSystemService = new RideSystemService();
		FeatureCollection featureCollection = rideSystemService.getAllRidePoints();
		return Response.ok(featureCollection).build();
	}

	@GET
	@Path("/ride/route/{rideId}")
	public Response getRidePoints(@PathParam("rideId") int rideId){
		RideSystemService rideSystemService = new RideSystemService();
		FeatureCollection featureCollection = rideSystemService.getRidePoints(rideId);
		return Response.ok(featureCollection).build();
	}

	@GET
	@Path("/riderequests/allpoints")
	public Response getAllRideRequestPoints(){
		RideSystemService rideSystemService = new RideSystemService();
		FeatureCollection featureCollection = rideSystemService.getAllRideRequestPoints();
		return Response.ok(featureCollection).build();
	}
	
	@GET
	@Path("/riderequests/{rideRequestId}")
	public Response getRideRequestPoints(@PathParam("rideRequestId") int rideRequestId) {
		RideSystemService rideSystemService = new RideSystemService();
		FeatureCollection featureCollection = rideSystemService.getRideRequestPoints(rideRequestId);
		return Response.ok(featureCollection).build();
	}
	


}
