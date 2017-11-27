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
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferDTO;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.domain.core.RideDO;
import com.digitusrevolution.rideshare.ride.domain.service.RideDomainService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferBusinessResource {
	
	/**
	 * 
	 * @param rideOfferDTO Ride domain model with additional information e.g. google Direction
	 * @return FeatureCollection containing offered ride information
	 */
	@POST
	public Response offerRide(RideOfferDTO rideOfferDTO){
	
		RideOfferBusinessService rideOfferBusinessService = new RideOfferBusinessService();
		int id = rideOfferBusinessService.offerRide(rideOfferDTO);
		//This is outside the offerRide function as offerRide is doing insert and without commit data would not reflect for another get
		//i.e. accepted ride request is not reflecting on getAllData function as ride has not commited in the db
		//So by splitting the request in two different transaction has helped to get the full details of newly created ride post
		RideDomainService rideDomainService = new RideDomainService();
		Ride createdRide = rideDomainService.get(id, true);
		FullRide createdRideDTO = JsonObjectMapper.getMapper().convertValue(createdRide,FullRide.class);

		return Response.ok(createdRideDTO).build();
	}
	
}




































