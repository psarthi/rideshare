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

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.inf.DomainResourceInteger;
import com.digitusrevolution.rideshare.common.inf.DomainResourceLong;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

@Path("/domain/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideDomainResource implements DomainResourceLong<Ride>{

	@Override
	@Secured
	@GET
	@Path("/{id}")
	public Response get(@PathParam("id") long id, @QueryParam("fetchChild") String fetchChild) {
		RideDomainService rideDomainService = new RideDomainService();
		Ride ride = rideDomainService.get(id, Boolean.valueOf(fetchChild));
		return Response.ok(ride).build();
	}

	@Override
	@Secured
	@GET
	public Response getAll() {
		RideDomainService rideDomainService = new RideDomainService();
		List<Ride> rides = rideDomainService.getAll();
		GenericEntity<List<Ride>> entity = new GenericEntity<List<Ride>>(rides) {};
		return Response.ok(entity).build();
	}
	
	@Secured
	@GET
	@Path("match/{id}")
	public Response autoMatchRide(@PathParam("id") long rideRequestId) {
		RideDomainService rideDomainService = new RideDomainService();
		MatchedTripInfo matchedTripInfo = rideDomainService.autoMatchRide(rideRequestId);
		return Response.ok(matchedTripInfo).build();	
	}

}
