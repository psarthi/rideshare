package com.digitusrevolution.rideshare.ride.business.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferResult;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
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
	
}




































