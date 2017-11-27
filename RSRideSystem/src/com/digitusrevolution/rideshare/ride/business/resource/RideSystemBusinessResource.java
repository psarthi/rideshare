package com.digitusrevolution.rideshare.ride.business.resource;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferDTO;
import com.digitusrevolution.rideshare.ride.business.RideOfferBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideRequestBusinessService;
import com.digitusrevolution.rideshare.ride.business.RideSystemBusinessService;

@Path("/ridesystem")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemBusinessResource {

	/**
	 * 
	 * @return FeatureCollection containing all rides of the system
	 */
	@GET
	@Path("/ride/allpoints")
	public Response getAllRidePoints(){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getAllRidePoints();
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return FeatureCollection containing specific ride information
	 */
	@GET
	@Path("/ride/route/{rideId}")
	public Response getRidePoints(@PathParam("rideId") int rideId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getRidePoints(rideId);
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @return FeatureCollection containing all ride requests of the system
	 */
	@GET
	@Path("/riderequest/allpoints")
	public Response getAllRideRequestPoints(){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getAllRideRequestPoints();
		return Response.ok(featureCollection).build();
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing specific ride request information
	 */
	@GET
	@Path("/riderequest/{rideRequestId}")
	public Response getRideRequestPoints(@PathParam("rideRequestId") int rideRequestId) {
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getRideRequestPoints(rideRequestId);
		return Response.ok(featureCollection).build();
	}
	
	/**
	 * 
	 * @param rideOfferDTO Ride domain model with additional information e.g. google Direction
	 * @return FeatureCollection containing offered ride information
	 */
	@POST
	@Path("/ride")
	public Response offerRide(RideOfferDTO rideOfferDTO){
	
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		List<Integer> rideIds = rideSystemBusinessService.offerRide(rideOfferDTO);
		FeatureCollection featureCollection = new FeatureCollection();
		for (Integer id : rideIds) {
			FeatureCollection rideFeatureCollection = rideSystemBusinessService.getRidePoints(id);
			featureCollection.addAll(rideFeatureCollection.getFeatures());
		}
		return Response.ok().entity(featureCollection).build();
	}
	
	/**
	 * 
	 * @param driverId Id of the driver
	 * @return list of rides
	 */
	@GET
	@Path("/ride/allupcoming/{driverId}")
	public Response getAllUpcomingRides(@PathParam("driverId") int driverId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		List<Ride> upcomingRides = rideSystemBusinessService.getAllUpcomingRides(driverId);
		return Response.ok().entity(upcomingRides).build();
	}

	/**
	 * 
	 * @param driverId Id of the driver
	 * @return current ride
	 */
	@GET
	@Path("/ride/current/{driverId}")
	public Response getCurrentRide(@PathParam("driverId") int driverId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		Ride upcomingRide = rideSystemBusinessService.getCurrentRide(driverId);
		return Response.ok().entity(upcomingRide).build();
	}

	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing matched rides information
	 */
	@GET
	@Path("/ride/search/{rideRequestId}")
	public Response getMatchingRides(@PathParam("rideRequestId") int rideRequestId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getMatchingRides(rideRequestId);
		return Response.ok(featureCollection).build();		
	}
	
	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return status OK
	 */
	@POST
	@Path("/ride/accept/{rideId}/{rideRequestId}")
	public Response acceptRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		rideSystemBusinessService.acceptRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @param rideRequestId Ride Request Id
	 * @return status OK
	 */
	@POST
	@Path("/ride/reject/{rideId}/{rideRequestId}")
	public Response rejectRideRequest(@PathParam("rideId") int rideId, @PathParam("rideRequestId") int rideRequestId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		rideSystemBusinessService.rejectRideRequest(rideId, rideRequestId);
		return Response.ok().build();				
	}

	/**
	 * 
	 * @param rideRequest Ride Request domain model
	 * @return FeatureCollection containing requested ride information
	 */
	@POST
	@Path("/riderequest")
	public Response requestRide(RideRequest rideRequest){
		
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		int id = rideSystemBusinessService.requestRide(rideRequest);
		RideSystemBusinessService rideSystemService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemService.getRideRequestPoints(id);
		return Response.ok().entity(featureCollection).build();
		
	}
	
	/**
	 * 
	 * @param rideId Ride Id for which we need to search ride requests 
	 * @param lastSearchDistance Last Search distance which is the distance of polygon from the route, For the first time request it should be 0
	 *  					  and subsequently this should be the value returned by previous result set, so that we don't start the search from scratch 
	 * @param lastResultIndex Last result index value, for the first time request it would be 0 and for subsequent request it should be actual value 
	 * 					   returned by previous result. This is used for pagination and providing incremental results instead of all at one go
	 * @return FeatureCollection containing matched ride requests
	 */
	@GET
	@Path("/riderequest/search/{rideId}/{lastSearchDistance}/{lastResultIndex}")
	public Response getMatchingRideRequests(@PathParam("rideId") int rideId, @PathParam("lastSearchDistance")  double lastSearchDistance, 
											@PathParam("lastResultIndex") int lastResultIndex){
	
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		FeatureCollection featureCollection = rideSystemBusinessService.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
		return Response.ok().entity(featureCollection).build();

	}
	
	/**
	 * 
	 * @param passengerId Id of the User
	 * @return current ride request
	 */
	@GET
	@Path("/riderequest/current/{passengerId}")
	public Response getUpcomingRideRequest(@PathParam("passengerId") int passengerId){
		RideSystemBusinessService rideSystemBusinessService = new RideSystemBusinessService();
		RideRequest upcomingRideRequest = rideSystemBusinessService.getCurrentRideRequest(passengerId);
		return Response.ok().entity(upcomingRideRequest).build();
	}

}
