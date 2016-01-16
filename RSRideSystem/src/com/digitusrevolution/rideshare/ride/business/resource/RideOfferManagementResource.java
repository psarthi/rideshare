package com.digitusrevolution.rideshare.ride.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.business.RideOfferManagementService;
import com.digitusrevolution.rideshare.ride.business.RideSystemService;

@Path("/rides")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideOfferManagementResource {
	
	@POST
	public Response offerRide(Ride ride){
	
		RideOfferManagementService rideOfferManagementService = new RideOfferManagementService();
		List<Integer> rideIds = rideOfferManagementService.offerRide(ride);
		FeatureCollection featureCollection = new FeatureCollection();
		for (Integer id : rideIds) {
			RideSystemService rideSystemService = new  RideSystemService();
			FeatureCollection rideFeatureCollection = rideSystemService.getRidePoints(id);
			featureCollection.addAll(rideFeatureCollection.getFeatures());
		}
		return Response.ok().entity(featureCollection).build();
	}

}
