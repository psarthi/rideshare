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

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.exception.NotAuthorizedException;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

@Path("/users/{userId}/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferBusinessResource {
	
	/**
	 * 
	 * @param rideOfferInfo containing Basic Ride with additional information e.g. google Direction
	 * @return OfferRideResult having created Ride Request and additional information
	 */
	@Secured
	@POST
	public Response offerRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			RideOfferInfo rideOfferInfo){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			long id = rideOfferBusinessService.offerRide(rideOfferInfo);
			//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
			RideOfferResult rideOfferResult = rideOfferBusinessService.getRideOfferResult(id);
			return Response.ok(rideOfferResult).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}

	@Secured
	@GET
	public Response getRides(@Context ContainerRequestContext requestContext, 
			@PathParam("userId") long userId,@QueryParam("page") int page){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			List<BasicRide> rides = rideOfferBusinessService.getRides(userId, page);
			GenericEntity<List<BasicRide>> entity = new GenericEntity<List<BasicRide>>(rides) {};
			return Response.ok(entity).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	@Secured
	@GET
	@Path("/{id}")
	public Response getRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("id") long id){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			FullRide ride = rideOfferBusinessService.getRide(id);
			return Response.ok(ride).build();
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@Secured
	@GET
	@Path("/current")
	public Response getCurrentRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			FullRide ride = rideOfferBusinessService.getCurrentRide(userId);
			if (ride==null) {
				throw new NotFoundException("No current ride for the user id:"+userId);
			}
			return Response.ok().entity(ride).build();			
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */
	@Secured
	@GET
	@Path("/start/{rideId}")
	public Response startRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.startRide(rideId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();							
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */	
	@Secured
	@GET
	@Path("/end/{rideId}")
	public Response endRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.endRide(rideId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();							
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */
	@Secured
	@GET
	@Path("/cancel/{rideId}")
	public Response cancelRide(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.cancelRide(rideId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();							
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return Updated Ride
	 */
	@Secured
	@GET
	@Path("{rideId}/cancelpassenger/{rideRequestId}")
	public Response cancelPassenger(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, 
			@QueryParam("rating") float rating){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.cancelPassenger(rideId, rideRequestId, rating);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();							
		}else {
			throw new NotAuthorizedException();
		}
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return Updated Ride
	 */
	@Secured
	@GET
	@Path("/{rideId}/pickup/{rideRequestId}")
	public Response pickupPassenger(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.pickupPassenger(rideId, rideRequestId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();				
		}else {
			throw new NotAuthorizedException();
		}
	}
	
	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return Updated Ride
	 */
	@Secured
	@GET
	@Path("/{rideId}/drop/{rideRequestId}")
	public Response dropPassenger(@Context ContainerRequestContext requestContext, @PathParam("userId") long userId,
			@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, 
			@QueryParam("ridemode") RideMode rideMode, @QueryParam("paymentcode") String paymentCode){
		if (AuthService.getInstance().validateTokenClaims(userId, requestContext)) {
			RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
			rideOfferBusinessService.dropPassenger(rideId, rideRequestId, rideMode, paymentCode);
			//Imp - We don't have to worry about payment success or failure as in failure case, we will provide option to pay to passenger 
			//Another important point, payment can only be done if its approved by passenger so this has to be done post the drop transaction
			//and also this would avoid issue with transactional rollbacks
			rideOfferBusinessService.makePayment(rideRequestId);
			//This will ensure that we are getting fully updated data once transaction is committed
			RideDomainService rideDomainService = new RideDomainService();
			FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
			return Response.ok(ride).build();						
		}else {
			throw new NotAuthorizedException();
		}
	}
}




































