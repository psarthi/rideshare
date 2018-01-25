package com.digitusrevolution.rideshare.ride.domain.resource;

import java.time.LocalTime;
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
import com.digitusrevolution.rideshare.model.ride.dto.RideOfferInfo;
import com.digitusrevolution.rideshare.ride.SampleLocalTimeModel;
import com.digitusrevolution.rideshare.ride.domain.service.RideSystemDomainService;

//Commenting this as all DO has changed to support proper system and input has also changed
//@Path("/domain/ridesystem")
//@Produces(MediaType.APPLICATION_JSON)
//@Consumes(MediaType.APPLICATION_JSON)
public class RideSystemDomainResource {

	/**
	 * 
	 * This function is just a dummy post
	 * 
	 * @param json Any Json
	 * @return same input json
	 */
	@POST
	@Path("/dummypost")
	public Response post(String json){	
		return Response.ok().entity(json).build();
	}
	
	/**
	 * 
	 * @return Ride domain model
	 */
	@GET
	@Path("/model/ride")
	public Response getRide(){
		Ride ride = new Ride();
		return Response.ok().entity(ride).build();
	}
	
	/**
	 * 
	 * @return Ride Request domain model
	 */
	@GET
	@Path("/model/riderequest")
	public Response getRideRequest(){
		RideRequest rideRequest = new RideRequest();
		return Response.ok().entity(rideRequest).build();
	}
	
	@GET
	@Path("/testget")
	public Response test(){
		SampleLocalTimeModel model = new SampleLocalTimeModel();
		model.setTime(LocalTime.of(0, 30));
		return Response.ok().entity(model).build();
	}

	@POST
	@Path("/testpost")
	public Response testDate(SampleLocalTimeModel model){
		return Response.ok().entity(model).build();
	}

	/**
	 * 
	 * @return FeatureCollection containing all rides of the system
	 */
	@GET
	@Path("/ride/allpoints")
	public Response getAllRidePoints(){
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getAllRidePoints();
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @param rideId Ride Id
	 * @return FeatureCollection containing specific ride information
	 */
	@GET
	@Path("/ride/route/{rideId}")
	public Response getRidePoints(@PathParam("rideId") long rideId){
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getRidePoints(rideId);
		return Response.ok(featureCollection).build();
	}

	/**
	 * 
	 * @return FeatureCollection containing all ride requests of the system
	 */
	@GET
	@Path("/riderequest/allpoints")
	public Response getAllRideRequestPoints(){
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getAllRideRequestPoints();
		return Response.ok(featureCollection).build();
	}
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing specific ride request information
	 */
	@GET
	@Path("/riderequest/{rideRequestId}")
	public Response getRideRequestPoints(@PathParam("rideRequestId") long rideRequestId) {
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getRideRequestPoints(rideRequestId);
		return Response.ok(featureCollection).build();
	}
	
	/**
	 * 
	 * @param rideOfferInfo Ride domain model with additional information e.g. google Direction
	 * @return FeatureCollection containing offered ride information
	 */
	@POST
	@Path("/ride")
	public Response offerRide(RideOfferInfo rideOfferInfo){
	
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		List<Long> rideIds = rideSystemDomainService.offerRide(rideOfferInfo);
		FeatureCollection featureCollection = new FeatureCollection();
		for (Long id : rideIds) {
			FeatureCollection rideFeatureCollection = rideSystemDomainService.getRidePoints(id);
			featureCollection.addAll(rideFeatureCollection.getFeatures());
		}
		return Response.ok().entity(featureCollection).build();
	}
	
	
	/**
	 * 
	 * @param rideRequestId Ride Request Id
	 * @return FeatureCollection containing matched rides information
	 */
	@GET
	@Path("/ride/search/{rideRequestId}")
	public Response getMatchingRides(@PathParam("rideRequestId") long rideRequestId){
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getMatchingRides(rideRequestId);
		return Response.ok(featureCollection).build();		
	}

	/**
	 * 
	 * @param rideRequest Ride Request domain model
	 * @return FeatureCollection containing requested ride information
	 */
	@POST
	@Path("/riderequest")
	public Response requestRide(RideRequest rideRequest){
		
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		long id = rideSystemDomainService.requestRide(rideRequest);
		FeatureCollection featureCollection = rideSystemDomainService.getRideRequestPoints(id);
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
	public Response getMatchingRideRequests(@PathParam("rideId") long rideId, @PathParam("lastSearchDistance")  double lastSearchDistance, 
											@PathParam("lastResultIndex") int lastResultIndex){
	
		RideSystemDomainService rideSystemDomainService = new RideSystemDomainService();
		FeatureCollection featureCollection = rideSystemDomainService.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
		return Response.ok().entity(featureCollection).build();

	}

}
