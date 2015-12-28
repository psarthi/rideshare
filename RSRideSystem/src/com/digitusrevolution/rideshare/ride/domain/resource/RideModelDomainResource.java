package com.digitusrevolution.rideshare.ride.domain.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;

@Path("/model")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideModelDomainResource {

	@POST
	@Path("/dummypost")
	public Response post(String json){	
		return Response.ok().entity(json).build();
	}
	
	@GET
	@Path("/ride")
	public Response getRide(){
		Ride ride = new Ride();
		return Response.ok().entity(ride).build();
	}
	
	@GET
	@Path("/riderequest")
	public Response getRideRequest(){
		RideRequest rideRequest = new RideRequest();
		return Response.ok().entity(rideRequest).build();
	}

}
