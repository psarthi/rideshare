package com.digitusrevolution.rideshare.ride.domain.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.inf.DomainResourceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

@Path("/domain/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestDomainResource implements DomainResourceLong<RideRequest>{

	@Override
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") long id, @QueryParam("fetchChild") String fetchChild) {
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		RideRequest rideRequest = rideRequestDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(rideRequest).build();
	}

	@Override
	@GET
	public Response getAll() {
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		List<RideRequest> rideRequests = rideRequestDomainService.getAll();
		GenericEntity<List<RideRequest>> entity = new GenericEntity<List<RideRequest>>(rideRequests) {};
		return Response.ok(entity).build();
	}	
}
