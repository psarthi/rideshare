package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.ride.data.RideDAO;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.core.comp.RideGeoJSON;
import com.digitusrevolution.rideshare.ride.dto.RideMatchInfo;
import com.digitusrevolution.rideshare.ride.dto.RidePointDTO;
import com.digitusrevolution.rideshare.ride.dto.google.GoogleDirection;

public class RideDO implements DomainObjectPKInteger<Ride>{

	private static final Logger logger = LogManager.getLogger(RideDO.class.getName());
	private Ride ride;
	private RideEntity rideEntity;
	private RideMapper rideMapper;
	private final RideDAO rideDAO;
	private final RidePointDAO ridePointDAO;

	public RideDO() {
		ride = new Ride();
		rideEntity = new RideEntity();
		rideMapper = new RideMapper();
		rideDAO = new RideDAO();
		ridePointDAO = new RidePointDAO();
	}

	public void setRide(Ride ride) {
		this.ride = ride;
		rideEntity = rideMapper.getEntity(ride);
	}

	private void setRideEntity(RideEntity rideEntity) {
		this.rideEntity = rideEntity;
		ride = rideMapper.getDomainModel(rideEntity);
	}

	@Override
	public void fetchChild() {
		ride = rideMapper.getDomainModelChild(ride, rideEntity);
		setRoute(ride);
	}

	@Override
	public List<Ride> getAll() {
		List<Ride> rides = new ArrayList<>();
		List<RideEntity> rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			//Purposefully not getting routes as it would be too much data
			setRidePickupAndDropPoints(ride);
			rides.add(ride);
		}
		return rides;
	}
	
	public List<Ride> getAllWithRoute() {
		List<Ride> rides = getAll();
		for (Ride ride : rides) {
			setRoute(ride);
		}
		return rides;
	}


	/*
	 * This method should be only used internally by this class only. 
	 * Update functionality is not supported once ride is created as its too complex
	 * 
	 * Note -
	 *
	 * 1. Update of RidePoints needs to be well thought - TBD
	 * 
	 */
	@Override
	public void update(Ride ride) {
		if (ride.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+ride.getId());
		}
		setRide(ride);
		rideDAO.update(rideEntity);
	}

	//Delete of RidePoints needs to be well thought as it may involves multiple rides as well - TBD
	@Override
	public void delete(int id) {
		ride = get(id);
		setRide(ride);
		rideDAO.delete(rideEntity);
	}

	/*
	 * This method should not be used from external classes and instead use offerRide method
	 * This method is only used internally from offerRide
	 * 
	 * Issue - 
	 * 
	 * 1. How to make this method private
	 * 
	 */
	@Override
	public int create(Ride ride) {
		logger.entry();
		setRide(ride);
		int id = rideDAO.create(rideEntity);
		logger.exit();		
		return id;
	}

	@Override
	public Ride get(int id) {
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideEntity(rideEntity);
		//Purposefully not getting routes as it would be too much data
		setRidePickupAndDropPoints(ride);
		return ride;
	}

	/*
	 * This method doesn't return anything, but actually set the ride points in the ride object itself
	 */
	private void setRidePickupAndDropPoints(Ride ride){
		RidePoint startPoint = ridePointDAO.getRidePointOfRide(ride.getStartPoint().get_id(),ride.getId());
		RidePoint endPoint = ridePointDAO.getRidePointOfRide(ride.getEndPoint().get_id(), ride.getId());		
		ride.setStartPoint(startPoint);
		ride.setEndPoint(endPoint);			
	}

	@Override
	public Ride getChild(int id) {
		get(id);
		fetchChild();
		return ride;
	}

	/*
	 * This method doesn't return anything, but actually set route in the ride object itself
	 */
	private void setRoute(Ride ride) {
		List<RidePoint> ridePoints = ridePointDAO.getAllRidePointsOfRide(ride.getId());
		Route route = new Route();
		route.setRidePoints(ridePoints);
		ride.setRoute(route);
	}

	/*
	 * Purpose - Goal of this function is to create ride as well as all the associated ride points in database
	 * 
	 * High Level Logic - 
	 * 
	 * - Check if the user has Driver role
	 * - if user is not driver, then ride can't be offered
	 * - if driver, then check for ride recurrence
	 * - if its recurring ride, generate multiple rides (TBD) 
	 * - Create all rides in database without start/end point 
	 * - Get all the ride point and generate the primary key for them for each rides (In case of recurring rides - TBD)
	 * - Update all the ride point in the database 
	 * - On success, update the ride with start point and end point primary key  
	 * 
	 * TBD -
	 *
	 * 1. Should we store starttime in Ride DB or it should be part of MongoDB startPoint
	 *    - Need to think on how to avoid multiple transaction between DBs for basic purpose as its two different DBs
	 * 2. What needs to be done when ride gets deleted in terms of its ride point or its updated
	 * 
	 */
	public List<Integer> offerRide(Ride ride, GoogleDirection direction){
		int userId = ride.getDriver().getId();
		Collection<Role> roles = RESTClientUtil.getRoles(userId);
		int id = 0;
		//**This field should be replaced by actual ride recurring information
		int recurringDays = 5;
		List<Integer> rideIds = new ArrayList<>();
		boolean driverStatus = false;
		//This will get travel distance from the first route and first leg
		int travelDistance = direction.getRoutes().get(0).getLegs().get(0).getDistance().getValue();
		for (Role role : roles) {
			if (role.getName().equals("Driver")){
				driverStatus = true;
				ride.setStatus("planned");
				ride.setTravelDistance(travelDistance);
				ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
				ride.setStartTime(startTimeUTC);				
				//Check if ride is recurring, then create multiple rides as per the recurring details
				//**TBD - Recurring code needs to be written later
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<recurringDays; i++){
						//No need to create multiple ride object as the change is only time
						//This time needs to updated again while updating at later part else all rides would get
						//last ride date and time
						ride.setStartTime(startTimeUTC.plusDays(i));
						id = create(ride);	
						rideIds.add(id);
						logger.debug("Ride has been created with id:" + id);						
					}
				}

				if (!ride.getRecur()){
					id = create(ride);	
					rideIds.add(id);
					//Below is imp, else it won't be able to update the ride which has been just created
					ride.setId(id);
					logger.debug("Ride has been created with id:" + id);
				}

				RouteDO routeDO = new RouteDO();
				List<RideBasicInfo> ridesBasicInfo = new ArrayList<>();
				//In case its a recurring ride, then create multiple rides and add all of them below
				//**TBD - Recurring scenarios has to be written later
				if(ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<recurringDays; i++){
						RideBasicInfo rideBasicInfo = new RideBasicInfo();
						rideBasicInfo.setId(rideIds.get(i));
						rideBasicInfo.setDateTime(startTimeUTC.plusDays(i));
						ridesBasicInfo.add(rideBasicInfo);
					}					
				}			
				if(!ride.getRecur()){
					RideBasicInfo rideBasicInfo = new RideBasicInfo();
					rideBasicInfo.setId(id);
					rideBasicInfo.setDateTime(startTimeUTC);
					ridesBasicInfo.add(rideBasicInfo);					
				}
				Route route = routeDO.getRoute(direction, ridesBasicInfo);
				Collection<RidePoint> ridePoints = route.getRidePoints();
				//Insert primary key here itself, so that we can update the start and end location in the ride table
				String startPointId;
				String endPointId = null;
				for (RidePoint ridePoint : ridePoints) {					
					ObjectId _id = new ObjectId();
					ridePoint.set_id(_id.toString());
					//This will keep overwriting the endPointId and last value would be the endPointId
					//This is done here to avoid reiterating the loop again just to get this id
					endPointId = _id.toString();
				}
				startPointId = ridePoints.iterator().next().get_id();
				ridePointDAO.createBulk(ridePoints);
				ride.getStartPoint().set_id(startPointId);
				ride.getEndPoint().set_id(endPointId);
				if (!ride.getRecur()){
					update(ride);
					logger.debug("Ride has been updated with id:"+ride.getId());					
				}
				//**TBD - Recurring scenarios has to be written later
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<recurringDays; i++){
						//Here we need to update start and end point reference
						//Apart from ride start and end point reference, which is same for all rides, 
						//We also need to update ride start time again else all rides would get the last ride start time
						//While creating multiple rides, we used the same ride object and only changed the start time
						//so current ride object is having last ride date and time
						//Other option is to deep copy ride object and create multiple rides while creating
						//This option can be relooked later but for now, we can just update the starttime again while updating
						ride.setId(rideIds.get(i));
						ride.setStartTime(startTimeUTC.plusDays(i));
						update(ride);
						logger.debug("Ride has been updated with id:"+rideIds.get(i));
					}
				}
			} 
		}
		if (!driverStatus) {
			throw new NotAuthorizedException("Can't offer ride unless you are a Driver");
		} else {
			return rideIds;
		}
	}


	public List<Ride> getUpcomingRides(int userId){

		return null;
	}

	/*
	 * Purpose - Search all rides which is matching ride requests criteria
	 * 
	 * High level logic -
	 * 
	 * - Get all rides matching pickup points
	 * - Get all rides matching drop points
	 * - Remove all the rides from pickup and drop points which is not there in both of them
	 * - Check if pickup ride point is before the drop ride point to validate the direction of ride
	 * - Remove all the rides which is not in the right direction
	 * - Check the availability of rides and remove those which is not available
	 * - Now all the rides remaining are the valid rides
	 * 
	 * 
	 */
	public List<RideMatchInfo> searchRides(int rideRequestId){		

		logger.debug("[Searching Rides for Ride Request Id]:"+ rideRequestId);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.get(rideRequestId);
		//Get all rides around radius of pickup variation from pickup point
		Map<Integer, RidePointDTO> pickupRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getPickupPoint());
		Map<Integer, RidePointDTO> dropRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getDropPoint());
		logger.debug("[Matching Pickup Rides: Based on Distance]:"+pickupRidePoints.keySet());
		logger.debug("[Matching Drop Rides: Based on Distance]:"+dropRidePoints.keySet());

		pickupRidePoints.keySet().retainAll(dropRidePoints.keySet());
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.debug("[Valid Pickup Rides: Based on Matching Pickup and Drop Point]:"+pickupRidePoints.keySet());
		logger.debug("[Valid Drop Rides: Based on Matching Pickup and Drop Point]:"+dropRidePoints.keySet());

		Iterator<Integer> iterator = pickupRidePoints.keySet().iterator();
		List<RideMatchInfo> rideMatchInfos = new ArrayList<>();


		while (iterator.hasNext()) {			
			Integer rideId = iterator.next();
			if (pickupRidePoints.get(rideId).getRidePoint().getSequence() >= dropRidePoints.get(rideId).getRidePoint().getSequence()){
				iterator.remove();
			} else {
				RidePointDTO pickupRidePointDTO = pickupRidePoints.get(rideId);
				RidePointDTO dropRidePointDTO =  dropRidePoints.get(rideId);
				RideMatchInfo rideMatchInfo = getRideMatchInfo(rideRequest, pickupRidePointDTO, dropRidePointDTO, rideId);
				rideMatchInfos.add(rideMatchInfo);
			}
		}

		Set<Integer> rideIds = pickupRidePoints.keySet();

		if (!rideIds.isEmpty()){
			Set<Integer> validRideIds = getValidRides(rideIds);
			logger.debug("[Valid Rides]:"+validRideIds);
			//This will remove all invalid rides from the list
			pickupRidePoints.keySet().retainAll(validRideIds);
		}

		//Remove invalid ride points again based on sequence and availability
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.debug("[Valid Pickup Rides: Based on Sequence of Pickup and Drop Points]:"+pickupRidePoints.keySet());
		logger.debug("[Valid Drop Rides: Based on Sequence of Pickup and Drop Points]:"+dropRidePoints.keySet());

		logger.debug("Final RideMatch List:" + rideMatchInfos);

		return rideMatchInfos;
	}
	
	/*
	 * Purpose - This function creates a DTO for passing the information back to the requester
	 */
	private RideMatchInfo getRideMatchInfo(RideRequest rideRequest, RidePointDTO pickupRidePointDTO, RidePointDTO dropRidePointDTO, int rideId) {
		RideMatchInfo rideMatchInfo = new RideMatchInfo();
		rideMatchInfo.setRideId(rideId);
		rideMatchInfo.setRidePickupPoint(pickupRidePointDTO.getRidePoint());
		rideMatchInfo.setRideDropPoint(dropRidePointDTO.getRidePoint());
		rideMatchInfo.setRideRequestId(rideRequest.getId());
		rideMatchInfo.setPickupPointDistance(pickupRidePointDTO.getDistance());
		rideMatchInfo.setDropPointDistance(dropRidePointDTO.getDistance());
		rideMatchInfo.setRideRequestTravelDistance(rideRequest.getTravelDistance());
		return rideMatchInfo;
	}	

	/*
	 * Purpose - This will get all valid ride Ids based on business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	private Set<Integer> getValidRides(Set<Integer> rideIds){
		Set<RideEntity> validRideEntities = rideDAO.getValidRides(rideIds);
		Set<Integer> validRideIds = new HashSet<>();
		for (RideEntity rideEntity : validRideEntities) {
			validRideIds.add(rideEntity.getId());
		}
		return validRideIds;
	}

	public List<RidePoint> getAllRidePointsOfRide(int rideId) {
		return ridePointDAO.getAllRidePointsOfRide(rideId);
	}
	
	public FeatureCollection getAllRidePoints(){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getAllRidePoints();
	}
	
	public FeatureCollection getMatchingRides(int rideRequestId){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getMatchingRides(rideRequestId);
	}

	public FeatureCollection getRidePoints(int rideId){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getRidePoints(rideId);
	}
	
	public List<Feature> getRideGeoJson(Ride ride) {
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getRideGeoJson(ride);
	}
	
	public FeatureCollection getRideMatchInfoGeoJSON(List<RideMatchInfo> rideMatchInfos) {
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getRideMatchInfoGeoJSON(rideMatchInfos);
		
	}
}
