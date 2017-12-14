package com.digitusrevolution.rideshare.ride.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestBusinessResource {

	/**
	 * 
	 * @param Basic Ride Request domain model
	 * @return RideRquestResult having created Ride Request and additional information
	 */
	@POST
	public Response requestRide(BasicRideRequest rideRequest){
	
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		MatchedTripInfo matchedTripInfo = rideRequestBusinessService.requestRide(rideRequest);
		//Reason for doing in separate transaction, as we need ride and ride request for bill generation and ride would not be available till we commit
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		rideSystemBusinessService.generateBill(matchedTripInfo);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideRequestResult rideRequestResult = rideRequestBusinessService.getRideRequestResult(matchedTripInfo.getRideRequestId());
		return Response.ok(rideRequestResult).build();
	}

	@GET
	@Path("/user/{id}")
	public Response getRideRequests(@PathParam("id") int id, @QueryParam("page") int page){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		List<FullRideRequest> rideRequests = rideRequestBusinessService.getRideRequests(id, page);
		GenericEntity<List<FullRideRequest>> entity = new GenericEntity<List<FullRideRequest>>(rideRequests) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getRideRequest(@PathParam("id") int id){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		FullRideRequest rideRequest = rideRequestBusinessService.getRideRequest(id);
		return Response.ok(rideRequest).build();
	}


}
