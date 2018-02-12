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
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

@Path("/users/{userId}/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferBusinessResource {
	
	private static final Logger logger = LogManager.getLogger(RideOfferBusinessResource.class.getName());
	
	/**
	 * 
	 * @param rideOfferInfo containing Basic Ride with additional information e.g. google Direction
	 * @return OfferRideResult having created Ride Request and additional information
	 */
	@Secured
	@POST
	public Response offerRide(RideOfferInfo rideOfferInfo){

		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		long id = rideOfferBusinessService.offerRide(rideOfferInfo);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideOfferResult rideOfferResult = rideOfferBusinessService.getRideOfferResult(id);
		return Response.ok(rideOfferResult).build();
	}

	@Secured
	@GET
	public Response getRides(@PathParam("userId") long userId, @QueryParam("page") int page){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		List<BasicRide> rides = rideOfferBusinessService.getRides(userId, page);
		GenericEntity<List<BasicRide>> entity = new GenericEntity<List<BasicRide>>(rides) {};
		return Response.ok(entity).build();
	}

	@Secured
	@GET
	@Path("/{id}")
	public Response getRide(@PathParam("id") long id){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		FullRide ride = rideOfferBusinessService.getRide(id);
		return Response.ok(ride).build();
	}

	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@Secured
	@GET
	@Path("/current")
	public Response getCurrentRide(@PathParam("userId") long userId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		FullRide ride = rideOfferBusinessService.getCurrentRide(userId);
		if (ride==null) {
			logger.debug("No Current ride found for userId:"+userId);
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok().entity(ride).build();			
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */
	@Secured
	@GET
	@Path("/start/{rideId}")
	public Response startRide(@PathParam("rideId") long rideId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.startRide(rideId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */	
	@Secured
	@GET
	@Path("/end/{rideId}")
	public Response endRide(@PathParam("rideId") long rideId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.endRide(rideId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */
	@Secured
	@GET
	@Path("/cancel/{rideId}")
	public Response cancelRide(@PathParam("rideId") long rideId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.cancelRide(rideId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
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
	public Response cancelPassenger(@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, @QueryParam("rating") float rating){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.cancelPassenger(rideId, rideRequestId, rating);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
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
	public Response pickupPassenger(@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.pickupPassenger(rideId, rideRequestId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
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
	public Response dropPassenger(@PathParam("rideId") long rideId, @PathParam("rideRequestId") long rideRequestId, 
			@QueryParam("ridemode") RideMode rideMode, @QueryParam("paymentcode") String paymentCode){
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
	}
}




































