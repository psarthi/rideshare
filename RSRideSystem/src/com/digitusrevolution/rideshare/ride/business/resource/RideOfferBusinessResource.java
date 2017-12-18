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

import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferBusinessResource {
	
	/**
	 * 
	 * @param rideOfferInfo containing Basic Ride with additional information e.g. google Direction
	 * @return OfferRideResult having created Ride Request and additional information
	 */
	@POST
	public Response offerRide(RideOfferInfo rideOfferInfo){
	
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		int id = rideOfferBusinessService.offerRide(rideOfferInfo);
		//Since we are trying to get all data before even committing, all child objects may not come so its cleaner to have getAllData post commit in different transaction
		RideOfferResult rideOfferResult = rideOfferBusinessService.getRideOfferResult(id);
		return Response.ok(rideOfferResult).build();
	}
	
	@GET
	@Path("/user/{id}")
	public Response getRides(@PathParam("id") int id, @QueryParam("page") int page){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		List<FullRide> rides = rideOfferBusinessService.getRides(id, page);
		GenericEntity<List<FullRide>> entity = new GenericEntity<List<FullRide>>(rides) {};
		return Response.ok(entity).build();
	}
	
	@GET
	@Path("/{id}")
	public Response getRide(@PathParam("id") int id){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		FullRide ride = rideOfferBusinessService.getRide(id);
		return Response.ok(ride).build();
	}

	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@GET
	@Path("/current/{driverId}")
	public Response getCurrentRide(@PathParam("driverId") int driverId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		FullRide ride = rideOfferBusinessService.getCurrentRide(driverId);
		if (ride==null) {
			throw new NotFoundException("No current ride for the user id:"+driverId);
		}
		return Response.ok().entity(ride).build();
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return updated Ride
	 */
	@GET
	@Path("/start/{rideId}")
	public Response startRide(@PathParam("rideId") int rideId){
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
	@GET
	@Path("/end/{rideId}")
	public Response endRide(@PathParam("rideId") int rideId){
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
	@GET
	@Path("/cancel/{rideId}")
	public Response cancelRide(@PathParam("rideId") int rideId){
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
	@GET
	@Path("{rideId}/cancelpassenger/{rideRequestId}")
	public Response cancelPassenger(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.cancelPassenger(rideId, rideRequestId);
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
	@GET
	@Path("/{rideId}/pickup/{rideRequestId}")
	public Response pickupPassenger(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
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
	@GET
	@Path("/{rideId}/drop/{rideRequestId}")
	public Response dropPassenger(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		rideOfferBusinessService.dropPassenger(rideId, rideRequestId);
		//This will ensure that we are getting fully updated data once transaction is committed
		RideDomainService rideDomainService = new RideDomainService();
		FullRide ride = JsonObjectMapper.getMapper().convertValue(rideDomainService.get(rideId, true), FullRide.class);
		return Response.ok(ride).build();				
	}
}




































