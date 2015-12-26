package com.digitusrevolution.rideshare.ride;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;

@Path("/dummy")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DummyRESTService {

	@POST
	public Response post(String json){	
		return Response.ok().entity(json).build();
	}
	
	@GET
	@Path("/getjson/ride")
	public Response getRide(){
		Ride ride = new Ride();
		return Response.ok().entity(ride).build();
	}
}
