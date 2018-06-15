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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.auth.Secured;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.PreBookingRideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.UserRidesDurationInfo;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.domain.service.RideRequestDomainService;

@Path("/users/{userId}/riderequests")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideRequestBusinessResource {

	private static final Logger logger = LogManager.getLogger(RideRequestBusinessResource.class.getName());
	
	/**
	 * 
	 * @param Basic Ride Request domain model
	 * @return RideRquestResult having created Ride Request and additional information
	 */
	@Secured
	@POST
	public Response requestRide(BasicRideRequest rideRequest){
	
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		long id = rideRequestBusinessService.requestRide(rideRequest);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideRequestResult rideRequestResult = rideRequestBusinessService.getRideRequestResult(id);
		return Response.ok(rideRequestResult).build();
	}

	@Secured
	@GET
	public Response getRideRequests(@PathParam("userId") long userId,@QueryParam("page") int page){
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			List<BasicRideRequest> rideRequests = rideRequestBusinessService.getRideRequests(userId, page);
			GenericEntity<List<BasicRideRequest>> entity = new GenericEntity<List<BasicRideRequest>>(rideRequests) {};
			return Response.ok(entity).build();			
	}
	
	@Secured
	@GET
	@Path("/{id}")
	public Response getRideRequest(@PathParam("id") long id){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		RideRequestResult rideRequestResult = rideRequestBusinessService.getRideRequestResult(id);
		return Response.ok(rideRequestResult).build();
	}
	
	/**
	 * 
	 * @param passengerId Id of the User
	 * @return current ride request
	 */
	@Secured
	@GET
	@Path("/current")
	public Response getCurrentRideRequest(@PathParam("userId") long userId){
			RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
			FullRideRequest rideRequest = rideRequestBusinessService.getCurrentRideRequest(userId);
			if (rideRequest==null) {
				logger.debug("No current ride request for the user id:"+userId);
				return Response.status(Status.NOT_FOUND).build();
			}
			return Response.ok().entity(rideRequest).build();			
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return updated Ride Request
	 */
	@Secured
	@GET
	@Path("/cancel/{rideRequestId}")
	public Response cancelRideRequest(@PathParam("rideRequestId") long rideRequestId){
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
	@Secured
	@GET
	@Path("{rideRequestId}/canceldriver/{rideId}")
	public Response cancelDriver(@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, @QueryParam("rating") float rating){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		rideRequestBusinessService.cancelDriver(rideId, rideRequestId, rating);
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
	@Secured
	@POST
	@Path("{rideRequestId}/accept/{rideId}")
	public Response acceptRideRequest(@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, MatchedTripInfo matchedTripInfo){
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		rideRequestBusinessService.acceptRideRequest(rideId, rideRequestId, matchedTripInfo);
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
	@Secured
	@GET
	@Path("{rideRequestId}/validatepaymentcode/{code}")
	public Response validatePaymentConfirmationCode(@PathParam("rideRequestId") long rideRequestId, @PathParam("code") String code) {
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		boolean status = rideRequestBusinessService.validatePaymentConfirmationCode(rideRequestId, code);
		return Response.ok(status).build();
	}
	
	/**
	 * 
	 * @param rideRequest
	 * @return PrebookingResult
	 */
	@Secured
	@POST
	@Path("/prebookinginfo")
	public Response getPreBookingInfo(BasicRideRequest basicRideRequest) {
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		PreBookingRideRequestResult preBookingRideRequestResult = rideRequestBusinessService.getPreBookingInfo(basicRideRequest);
		return Response.ok(preBookingRideRequestResult).build();
	}

	@Secured
	@POST
	@Path("/count")
	public Response getUserRideRequestsCountInSpecificDuration(UserRidesDurationInfo ridesDurationInfo) {
		RideRequestBusinessService rideRequestBusinessService = new RideRequestBusinessService();
		int count = rideRequestBusinessService.getUserRideRequestsCountInSpecificDuration(ridesDurationInfo);
		return Response.ok(count).build();
	}

}
