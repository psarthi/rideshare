package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
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
import org.geojson.LineString;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.ride.data.RideDAO;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
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
	}

	@Override
	public List<Ride> getAll() {
		List<Ride> rides = new ArrayList<>();
		List<RideEntity> rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			//Purposefully not getting routes as it would be too much data
			getRidePoints();
			rides.add(ride);
		}
		return rides;

	}

	//Update of RidePoints needs to be well thought - TBD
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
	public void delete(Ride ride) {
		setRide(ride);
		rideDAO.delete(rideEntity);
	}
	
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
		getRidePoints();
		return ride;
	}
	
	private void getRidePoints(){
		RidePoint startPoint = ridePointDAO.getRidePointOfRide(ride.getStartPoint().get_id(),ride.getId());
		RidePoint endPoint = ridePointDAO.getRidePointOfRide(ride.getEndPoint().get_id(), ride.getId());		
		ride.setStartPoint(startPoint);
		ride.setEndPoint(endPoint);			
	}

	@Override
	public Ride getChild(int id) {
		get(id);
		fetchChild();
		getRoute();
		return ride;
	}

	private void getRoute() {
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
	public int offerRide(Ride ride, GoogleDirection direction){
		int userId = ride.getDriver().getId();
		Collection<Role> roles = RESTClientUtil.getRoles(userId);
		int id = 0;
		boolean driverStatus = false;
		for (Role role : roles) {
			if (role.getName().equals("Driver")){
				driverStatus = true;
				ride.setStatus("planned");
				ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
				ride.setStartTime(startTimeUTC);				
				//Check if ride is recurring, then create multiple rides as per the recurring details
				//**TBD - Recurring code needs to be written later
				int[] ids = new int[5];
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<5; i++){
						ride.setStartTime(startTimeUTC.plusDays(i));
						ids[i] = create(ride);		
						logger.debug("Ride has been created with id:" + ids[i]);						
					}
				}
				
				if (!ride.getRecur()){
					id = create(ride);		
					logger.debug("Ride has been created with id:" + id);
				}
				
				RouteDO routeDO = new RouteDO();
				List<RideBasicInfo> ridesBasicInfo = new ArrayList<>();
				//In case its a recurring ride, then create multiple rides and add all of them below
				//**TBD - Recurring scenarios has to be written later
				if(ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<5; i++){
						RideBasicInfo rideBasicInfo = new RideBasicInfo();
						rideBasicInfo.setId(ids[i]);
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
				//Below is imp, else it won't be able to update the ride which has been just created
				if (!ride.getRecur()){
					ride.setId(id);
					update(ride);
					logger.debug("Ride has been updated with id:"+ride.getId());					
				}
				//**TBD - Recurring scenarios has to be written later
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i=0; i<5; i++){
						ride.setId(ids[i]);
						update(ride);
						logger.debug("Ride has been updated with id:"+ride.getId());											
					}
				}
			} 
		}
		if (!driverStatus) {
			throw new NotAuthorizedException("Can't offer ride unless you are a Driver");
		} else {
			//**TBD - This will return 0 in case of recurring rides, needs to be thionk through
			return id;
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
	public List<RideMatchInfo> searchRides(RideRequest rideRequest){		

		//Get all rides around radius of pickup variation from pickup point
		Map<Integer, RidePointDTO> pickupRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getPickupPoint());
		Map<Integer, RidePointDTO> dropRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getDropPoint());
		logger.debug("[Matching Pickup Rides: Based on Distance]:"+pickupRidePoints.keySet());
		logger.debug("[Matching Drop Rides: Based on Distance]:"+dropRidePoints.keySet());

		pickupRidePoints.keySet().retainAll(dropRidePoints.keySet());
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.debug("[Valid Pickup Rides: Based on RideIds]:"+pickupRidePoints.keySet());
		logger.debug("[Valid Drop Rides: Based on RideIds]:"+dropRidePoints.keySet());

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
		Set<Integer> availableRideIds = getAvailableRides(rideIds);
		logger.debug("[Available Rides]:"+availableRideIds);
		//This will remove all unavailable rides from the list
		pickupRidePoints.keySet().retainAll(availableRideIds);
		
		//Remove invalid ride points again based on sequence and availability
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.debug("[Valid Pickup Rides: Based on Sequence]:"+pickupRidePoints.keySet());
		logger.debug("[Valid Drop Rides: Based on Sequence]:"+dropRidePoints.keySet());

		logger.debug("RideMatch Info List:" + rideMatchInfos);

		return rideMatchInfos;
	}
	
	private RideMatchInfo getRideMatchInfo(RideRequest rideRequest, RidePointDTO pickupRidePointDTO, RidePointDTO dropRidePointDTO, int rideId) {
		RideMatchInfo rideMatchInfo = new RideMatchInfo();
		rideMatchInfo.setRideId(rideId);
		rideMatchInfo.setRidePickupPoint(pickupRidePointDTO.getRidePoint());
		rideMatchInfo.setRideDropPoint(dropRidePointDTO.getRidePoint());
		rideMatchInfo.setRideRequestId(rideRequest.getId());
		rideMatchInfo.setPickupPointDistance(pickupRidePointDTO.getDistance());
		rideMatchInfo.setDropPointDistance(dropRidePointDTO.getDistance());
		rideMatchInfo.setTravelDistance(rideRequest.getTravelDistance());
		return rideMatchInfo;
	}	
	
	private Set<Integer> getAvailableRides(Set<Integer> rideIds){
		Set<RideEntity> availableRides = rideDAO.getAvailableRides(rideIds);
		Set<Integer> availableRideIds = new HashSet<>();
		for (RideEntity rideEntity : availableRides) {
			availableRideIds.add(rideEntity.getId());
		}
		return availableRideIds;
	}
	
	/*
	 * This method for testing purpose only
	 */
	public FeatureCollection getAllRidePoints(){
		FeatureCollection featureCollection = new FeatureCollection();
		List<Ride> rides = getAll();
		for (Ride ride : rides) {
			List<RidePoint> ridePoints = ridePointDAO.getAllRidePointsOfRide(ride.getId());
			List<Point> points = new ArrayList<>();
			for (RidePoint ridePoint : ridePoints) {
				points.add(ridePoint.getPoint());
			}
			LineString lineString = GeoJSONUtil.getLineStringFromPoints(points);
			Map<String, Object> properties = new HashMap<>();
			properties.put("Ride Id", ride.getId());
			properties.put("Start DateTime in UTC", ride.getStartTime());
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(lineString, properties);
			featureCollection.add(feature);
		}	
		return featureCollection;
	}
	
	/*
	 * This method for testing purpose only
	 */
	public FeatureCollection getGeoJsonForRideSearch(RideRequest rideRequest){
		FeatureCollection featureCollection = new FeatureCollection();
		List<RideMatchInfo> rideMatchInfos = searchRides(rideRequest);
		for (RideMatchInfo rideMatchInfo : rideMatchInfos) {
			List<Point> points = new ArrayList<>();
			points.add(rideMatchInfo.getRidePickupPoint().getPoint());
			points.add(rideMatchInfo.getRideDropPoint().getPoint());
			LineString lineString = GeoJSONUtil.getLineStringFromPoints(points);
			Map<String, Object> properties = new HashMap<>();
			properties.put("Ride Id", rideMatchInfo.getRideId());
			properties.put("Ride Request Id", rideMatchInfo.getRideRequestId());
			properties.put("Pickup Distance", rideMatchInfo.getPickupPointDistance());
			properties.put("Drop Distance", rideMatchInfo.getDropPointDistance());
			properties.put("Travel Distance", rideMatchInfo.getTravelDistance());
			Feature feature = GeoJSONUtil.getFeatureFromGeometry(lineString, properties);
			featureCollection.add(feature);
		}
		return featureCollection;
	}
}
