package com.digitusrevolution.rideshare.ride.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

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
		int id = rideRequestBusinessService.requestRide(rideRequest);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideRequestResult rideRequestResult = rideRequestBusinessService.getRideRequestResult(id);
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
	
	/**
	 * 
	 * @param passengerId Id of the User
	 * @return current ride request
	 */
	@GET
	@Path("/current/{passengerId}")
	public Response getCurrentRideRequest(@PathParam("passengerId") int passengerId){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		FullRideRequest rideRequest = rideRequestBusinessService.getCurrentRideRequest(passengerId);
		if (rideRequest==null) {
			throw new NotFoundException("No current ride request for the user id:"+passengerId);
		}
		return Response.ok().entity(rideRequest).build();
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return updated Ride Request
	 */
	@GET
	@Path("/cancel/{rideRequestId}")
	public Response cancelRideRequest(@PathParam("rideRequestId") int rideRequestId){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		rideRequestBusinessService.cancelRideRequest(rideRequestId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		FullRideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(rideRequestDomainService.get(rideRequestId, true), FullRideRequest.class);
		return Response.ok(rideRequest).build();				
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return Updated Ride Request
	 */
	@GET
	@Path("{rideRequestId}/canceldriver/{rideId}")
	public Response cancelDriver(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId, @QueryParam("rating") float rating){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		rideRequestBusinessService.cancelDriver(rideId, rideRequestId, rating);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
		FullRideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(rideRequestDomainService.get(rideRequestId, true), FullRideRequest.class);
		return Response.ok(rideRequest).build();				
	}

	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @param code is payment confirmation code
	 * @return true/false
	 */
	@GET
	@Path("{rideRequestId}/validatepaymentcode/{code}")
	public Response validatePaymentConfirmationCode(@PathParam("rideRequestId") int rideRequestId, @PathParam("code") String code) {
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		boolean status = rideRequestBusinessService.validatePaymentConfirmationCode(rideRequestId, code);
		return Response.ok(status).build();
	}


}
