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
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.PreBookingRideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

@Path("/users/{userId}/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestBusinessResource {

	/**
	 * 
	 * @param Basic Ride Request domain model
	 * @return RideRquestResult having created Ride Request and additional information
	 */
	@Secured
	@POST
	public Response requestRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			BasicRideRequest rideRequest){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			long id = rideRequestBusinessService.requestRide(rideRequest);
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			RideRequestResult rideRequestResult = rideRequestBusinessService.getRideRequestResult(id);
			return Response.ok(rideRequestResult).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}

	@Secured
	@GET
	@Path("/user/{id}")
	public Response getRideRequests(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("id") long id, @QueryParam("page") int page){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			List<BasicRideRequest> rideRequests = rideRequestBusinessService.getRideRequests(id, page);
			GenericEntity<List<BasicRideRequest>> entity = new GenericEntity<List<BasicRideRequest>>(rideRequests) {};
			return Response.ok(entity).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	@Secured
	@GET
	@Path("/{id}")
	public Response getRideRequest(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("id") long id){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			FullRideRequest rideRequest = rideRequestBusinessService.getRideRequest(id);
			return Response.ok(rideRequest).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param passengerId Id of the User
	 * @return current ride request
	 */
	@Secured
	@GET
	@Path("/current/{passengerId}")
	public Response getCurrentRideRequest(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("passengerId") long passengerId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			FullRideRequest rideRequest = rideRequestBusinessService.getCurrentRideRequest(passengerId);
			if (rideRequest==null) {
				throw new NotFoundException("No current ride request for the user id:"+passengerId);
			}
			return Response.ok().entity(rideRequest).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return updated Ride Request
	 */
	@Secured
	@GET
	@Path("/cancel/{rideRequestId}")
	public Response cancelRideRequest(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideRequestId") long rideRequestId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			rideRequestBusinessService.cancelRideRequest(rideRequestId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
			FullRideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(rideRequestDomainService.get(rideRequestId, true), FullRideRequest.class);
			return Response.ok(rideRequest).build();				
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return Updated Ride Request
	 */
	@Secured
	@GET
	@Path("{rideRequestId}/canceldriver/{rideId}")
	public Response cancelDriver(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, 
			@QueryParam("rating") float rating){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			rideRequestBusinessService.cancelDriver(rideId, rideRequestId, rating);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideRequestDomainService rideRequestDomainService = new RideRequestDomainService();
			FullRideRequest rideRequest = JsonObjectMapper.getMapper().convertValue(rideRequestDomainService.get(rideRequestId, true), FullRideRequest.class);
			return Response.ok(rideRequest).build();				
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @param code is payment confirmation code
	 * @return true/false
	 */
	@Secured
	@GET
	@Path("{rideRequestId}/validatepaymentcode/{code}")
	public Response validatePaymentConfirmationCode(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideRequestId") long rideRequestId, @PathParam("code") String code) {
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			boolean status = rideRequestBusinessService.validatePaymentConfirmationCode(rideRequestId, code);
			return Response.ok(status).build();
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param rideRequest
	 * @return PrebookingResult
	 */
	@Secured
	@POST
	@Path("/prebookinginfo")
	public Response getPreBookingInfo(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			BasicRideRequest basicRideRequest) {
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			PreBookingRideRequestResult preBookingRideRequestResult = rideRequestBusinessService.getPreBookingInfo(basicRideRequest);
			return Response.ok(preBookingRideRequestResult).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}


}
