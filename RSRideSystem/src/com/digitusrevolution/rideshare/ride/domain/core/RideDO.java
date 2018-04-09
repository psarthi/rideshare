package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.types.ObjectId;
import org.geojson.Feature;
import org.geojson.FeatureCollection;

import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.service.NotificationService;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RidePointProperty;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideSeatStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideStatus;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RidePointInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDirection;
import com.digitusrevolution.rideshare.model.ride.dto.google.Leg;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Interest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.ride.data.RideDAO;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustNetworkDO;
import com.digitusrevolution.rideshare.ride.domain.core.comp.RideAction;
import com.digitusrevolution.rideshare.ride.domain.core.comp.RideGeoJSON;

public class RideDO implements DomainObjectPKLong<Ride>{

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
		rideEntity = rideMapper.getEntity(ride, true);
	}

	private void setRideEntity(RideEntity rideEntity) {
		this.rideEntity = rideEntity;
		ride = rideMapper.getDomainModel(rideEntity, false);
		//Purposefully not getting routes as it would be too much data
		setRideStartAndEndPoints(ride);
	}

	//This will set all Ride Requests points references inside the Ride model
	private void setRideRequestPoints() {
		RideRequestDO rideRequestDO = new RideRequestDO();
		for (RideRequest rideRequest:ride.getAcceptedRideRequests()) {
			rideRequestDO.setRideRequestPoints(rideRequest);
		}
		for (RideRequest rideRequest:ride.getCancelledRideRequests()) {
			rideRequestDO.setRideRequestPoints(rideRequest);
		}
	}

	@Override
	public void fetchChild() {
		ride = rideMapper.getDomainModelChild(ride, rideEntity);
		setRoute(ride);
		//This will set All Ride Request points of all reference ride request in this Ride
		setRideRequestPoints();
	}

	@Override
	public List<Ride> getAll() {
		List<Ride> rides = new ArrayList<>();
		List<RideEntity> rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
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
	public void delete(long id) {
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
	public long create(Ride ride) {
		logger.entry();
		setRide(ride);
		long id = rideDAO.create(rideEntity);
		logger.exit();		
		return id;
	}

	@Override
	public Ride get(long id) {
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideEntity(rideEntity);
		return ride;
	}

	/*
	 * This method doesn't return anything, but actually set the ride points in the ride object itself
	 */
	public void setRideStartAndEndPoints(Ride ride){
		RidePoint startPoint = ridePointDAO.getRidePointOfRide(ride.getStartPoint().get_id(),ride.getId());
		RidePoint endPoint = ridePointDAO.getRidePointOfRide(ride.getEndPoint().get_id(), ride.getId());		
		ride.setStartPoint(startPoint);
		ride.setEndPoint(endPoint);			
	}

	@Override
	public Ride getAllData(long id) {
		get(id);
		fetchChild();
		return ride;
	}

	/*
	 * This method doesn't return anything, but actually set route in the ride object itself
	 */
	private void setRoute(Ride ride) {
		List<RidePoint> ridePoints = ridePointDAO.getAllRidePointsOfRide(ride.getId());
		//VERY IMP - This will sort the route points by sequence number otherwise you will 
		//get zig-zag line while drawing route in map 
		Collections.sort(ridePoints);
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
	public List<Long> offerRide(Ride ride, GoogleDirection direction){
		long userId = ride.getDriver().getId();
		Collection<Role> roles = RESTClientUtil.getRoles(userId);
		long id = 0;
		//**This field should be replaced by actual ride recurring information
		int recurringDays = 5;
		List<Long> rideIds = new ArrayList<>();
		boolean driverStatus = false;
		//This will get travel distance from the first route and first leg
		Leg leg = direction.getRoutes().get(0).getLegs().get(0);
		int travelDistance = leg.getDistance().getValue();
		long travelDuration = leg.getDuration().getValue();
		for (Role role : roles) {
			if (role.getName().equals(RoleName.Driver)){
				driverStatus = true;
				//This will set the ride status
				ride.setStatus(RideStatus.Planned);
				//This will set the ride seat status if there is any seats offered
				if (ride.getSeatOffered() > 0){
					ride.setSeatStatus(RideSeatStatus.Available);
				}
				ride.setTravelDistance(travelDistance);
				ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
				ZonedDateTime endTimeUTC = startTimeUTC.plusSeconds(travelDuration);
				ride.setStartTime(startTimeUTC);
				ride.setEndTime(endTimeUTC);

				//This will set Start and End Address
				//Actually address should be set from Places API in Android itself, to avoid ambiguity
				if (ride.getStartPointAddress()==null) ride.setStartPointAddress(leg.getStartAddress());
				if (ride.getEndPointAddress()==null) ride.setEndPointAddress(leg.getEndAddress());

				//**IMP Problem - Trustnetwork gets created while creating the ride but we don't have its id and without id it will 
				//recreate the trust network while updating the ride at later part of the this function as trust network id is the primary key
				//for trust network entity. 
				//Solution - Create trust network first and set that to ride. Remove cascade property from ride for trust network, so that ride creation 
				//can happen properly, else it will throw error if it tries to recreated the same trustnetwork
				TrustNetwork trustNetwork = ride.getTrustNetwork();
				TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
				long trustNetworkId = trustNetworkDO.create(trustNetwork);
				TrustNetwork trustNetworkWithId = trustNetworkDO.get(trustNetworkId);
				ride.setTrustNetwork(trustNetworkWithId);

				//Check if ride is recurring, then create multiple rides as per the recurring details
				//**TBD - Recurring code needs to be written later
				if (ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<recurringDays; i++){
						//No need to create multiple ride object as the change is only time
						//This time needs to updated again while updating at later part else all rides would get
						//last ride date and time
						ride.setStartTime(startTimeUTC.plusDays(i));
						ride.setEndTime(endTimeUTC.plusDays(i));
						id = create(ride);	
						rideIds.add(id);
						logger.info("Ride has been created with id:" + id);						
					}
				}

				if (!ride.getRecur()){
					id = create(ride);	
					rideIds.add(id);
					//Below is imp, else it won't be able to update the ride which has been just created
					ride.setId(id);
					logger.info("Ride has been created with id:" + id);
				}

				RouteDO routeDO = new RouteDO();
				List<RidePointProperty> ridePointProperties = new ArrayList<>();
				//In case its a recurring ride, then create multiple rides and add all of them below
				//**TBD - Recurring scenarios has to be written later
				if(ride.getRecur()){
					//For testing purpose, needs to be written properly
					for (int i = 0; i<recurringDays; i++){
						RidePointProperty ridePointProperty = new RidePointProperty();
						ridePointProperty.setId(rideIds.get(i));
						ridePointProperty.setDateTime(startTimeUTC.plusDays(i));
						ridePointProperties.add(ridePointProperty);
					}					
				}			
				if(!ride.getRecur()){
					RidePointProperty ridePointProperty = new RidePointProperty();
					ridePointProperty.setId(id);
					ridePointProperty.setDateTime(startTimeUTC);
					ridePointProperties.add(ridePointProperty);					
				}
				Route route = routeDO.getRoute(direction, ridePointProperties);
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
				//This is important as using iterator().next doesn't guarantee the sequence
				startPointId = ridePoints.stream().findFirst().get().get_id();
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
						ride.setEndTime(endTimeUTC.plusDays(i));
						update(ride);
						logger.debug("Ride has been updated with id:"+rideIds.get(i));
					}
				}
			} 
		}
		if (!driverStatus) {
			throw new WebApplicationException("Can't offer ride as user is not a driver. User Id:" + ride.getDriver().getId());
		} else {
			NotificationService.sendRideOfferAdminNotification(ride);
			return rideIds;
		}
	}

	/*
	 * Purpose - This will get upcoming rides from current time in UTC. 
	 * Result set is limited as configured, so that we don't get all the upcoming rides of the user 
	 * 
	 * TBD - Do we need to support pagination.
	 */
	public List<Ride> getAllUpcomingRides(long driverId){		
		int limit = Integer.parseInt(PropertyReader.getInstance().getProperty("UPCOMING_RIDE_RESULT_LIMIT"));
		User driver = RESTClientUtil.getUser(driverId);
		UserMapper userMapper = new UserMapper();
		//We don't need child object of User entity, just the basic user entity is fine as it primarily needs only PK
		UserEntity driverEntity = userMapper.getEntity(driver, false);
		Set<RideEntity> rideEntities = rideDAO.getAllUpcomingRides(driverEntity, limit);
		List<Ride> rides = new LinkedList<>();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			rides.add(ride);
		}
		return rides;
	}

	/*
	 * Purpose - Get current ride
	 */
	public Ride getCurrentRide(long driverId){		
		User driver = RESTClientUtil.getUser(driverId);
		UserMapper userMapper = new UserMapper();
		//We don't need child object of User entity, just the basic user entity is fine as it primarily needs only PK
		UserEntity driverEntity = userMapper.getEntity(driver, false);
		RideEntity rideEntity = rideDAO.getCurrentRide(driverEntity);
		if (rideEntity != null) {
			setRideEntity(rideEntity);
			fetchChild();
			return ride;
		}
		return null;
	}

	public List<Ride> getRides(long driverId, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount;
		//Not required as we are fetching the result with max result set defined
		//int endIndex = (page+1)*itemsCount;

		User driver = RESTClientUtil.getUser(driverId);
		UserMapper userMapper = new UserMapper();
		//We don't need child object of User entity, just the basic user entity is fine as it primarily needs only PK
		UserEntity driverEntity = userMapper.getEntity(driver, false);
		List<RideEntity> rideEntities = rideDAO.getRides(driverEntity, startIndex);
		List<Ride> rides = new LinkedList<>();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			//Don't fetch child objects be it from mySQL or MongoDB as it will become very resource intensive job
			rides.add(ride);
		}
		//This will sort the element as per the comparator written in Ride class
		Collections.sort(rides);
		return rides;
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
	public List<MatchedTripInfo> searchRides(long rideRequestId, boolean flexible){		

		logger.info("[Searching Rides for Ride Request Id]:"+ rideRequestId + "With Flexible options: " +flexible);
		RideRequestDO rideRequestDO = new RideRequestDO();
		//This is important else you will not get cancelled rides info
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		//Get all rides around radius of pickup variation from pickup point
		Map<Long, RidePointInfo> pickupRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getPickupPoint(), flexible);
		Map<Long, RidePointInfo> dropRidePoints = ridePointDAO.getAllMatchingRidePointNearGivenPoint(rideRequest.getDropPoint(), flexible);
		logger.info("[Matching Pickup Rides: Based on Time and Distance]:"+pickupRidePoints.keySet());
		logger.info("[Matching Drop Rides: Based on Time and Distance]:"+dropRidePoints.keySet());

		//Step 1 - This will remove all rides which doesn't have corresponding pair i.e. if prickup and drop both doesn't exist then its invalid
		pickupRidePoints.keySet().retainAll(dropRidePoints.keySet());
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.info("[Valid Pickup Rides: Based on Matching Pickup and Drop Point]:"+pickupRidePoints.keySet());
		logger.info("[Valid Drop Rides: Based on Matching Pickup and Drop Point]:"+dropRidePoints.keySet());

		Iterator<Long> iterator = pickupRidePoints.keySet().iterator();

		//Step 2 - This will remove all rides which is not in right direction
		while (iterator.hasNext()) {			
			Long rideId = iterator.next();
			if (pickupRidePoints.get(rideId).getRidePoint().getSequence() >= dropRidePoints.get(rideId).getRidePoint().getSequence()){
				iterator.remove();
			}		
		}
		logger.info("[Valid Pickup Rides: Based on Sequence of Pickup and Drop Points]:"+pickupRidePoints.keySet());
		logger.info("[Valid Drop Rides: Based on Sequence of Pickup and Drop Points]:"+dropRidePoints.keySet());

		Set<Long> rideIds = pickupRidePoints.keySet(); 

		Collection<Ride> cancelledRides = rideRequest.getCancelledRides();
		Set<Long> cancelledRideIds = new HashSet<>();

		for (Ride ride: cancelledRides) {
			cancelledRideIds.add(ride.getId());
		}

		logger.info("Cancelled Rides List:"+cancelledRideIds);
		rideIds.removeAll(cancelledRideIds);
		logger.info("Valid Rides List after exclusion of cancelled Rides:"+rideIds);

		//Step 3 - This will remove all rides which doesn't match the business criteria e.g. if its not available
		if (!rideIds.isEmpty()){
			TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
			if (flexible) {
				//IMP - This will overwrite Trustcategory but since this is not being used anywhere to update so we are fine
				//but in case its getting updated then we need create a new copy of the same to avoid overwriting issue
				trustNetwork.getTrustCategories().iterator().next().setName(TrustCategoryName.Anonymous);
			}
			Set<Long> validRideIds = getValidRides(rideIds, rideRequest.getSeatRequired(), rideRequest.getRideMode(), 
					rideRequest.getPassenger(), trustNetwork);
			logger.info("[Valid Rides Based on Business Criteria]:"+validRideIds);
			//This will remove all invalid rides from the list
			pickupRidePoints.keySet().retainAll(validRideIds);
		}

		//Basic Cleanup - Remove invalid drop points again based on sequence and business criteria
		dropRidePoints.keySet().retainAll(pickupRidePoints.keySet());
		logger.info("[Valid Pickup Rides: Based on Business Criteria]:"+pickupRidePoints.keySet());
		logger.info("[Valid Drop Rides: Based on Business Criteria]:"+dropRidePoints.keySet());

		iterator = pickupRidePoints.keySet().iterator();
		//TODO Later find a better logic to evenly fill the seats
		//VERY IMP - This will sort the ride ids based on their ids i.e. first come first server 
		//but this also has a catch if first ride has offered 4 seats, then until all 4 seats are filled
		//other rides would not get filled in that route. So its better to keep it random only for now
		//so that all ride owners gets chance to fill the seats
		//List<Long> sortedRideIds = new ArrayList<>(pickupRidePoints.keySet());
		List<MatchedTripInfo> matchedTripInfos = new ArrayList<>();

		//Step 4 - This will create matching trip info for all valid rides
		while (iterator.hasNext()) {			
			Long rideId = iterator.next();
			RidePointInfo pickupRidePointInfo = pickupRidePoints.get(rideId);
			RidePointInfo dropRidePointInfo =  dropRidePoints.get(rideId);
			MatchedTripInfo matchedTripInfo = getMatchedTripInfo(rideRequest, pickupRidePointInfo, dropRidePointInfo, rideId);
			matchedTripInfos.add(matchedTripInfo);
		}

		JSONUtil<MatchedTripInfo> jsonUtil = new JSONUtil<>(MatchedTripInfo.class);
		for (MatchedTripInfo matchedTripInfo : matchedTripInfos) {
			logger.info("Final Matching Trip Info:"+ jsonUtil.getJson(matchedTripInfo));	
		}
		if (matchedTripInfos.size() == 0) {
			logger.info("No matching Ride found for Ride Request Id:"+ rideRequestId);
		}
		//This will sort by Pickup time
		//No need to do this sorting in case of standard option without flexibility 
		//as this would be called from auto match and there we are already doing sorting, so its a waste of processing
		if (flexible) {
			matchedTripInfos = getSortedMatchedListByPickupTime(matchedTripInfos);	
		}
		return matchedTripInfos;
	}

	/*
	 * Purpose - This function creates a DTO for passing the information back to the requester
	 */
	private MatchedTripInfo getMatchedTripInfo(RideRequest rideRequest, RidePointInfo pickupRidePointInfo, RidePointInfo dropRidePointInfo, long rideId) {
		MatchedTripInfo matchedTripInfo = new MatchedTripInfo();
		matchedTripInfo.setRideId(rideId);
		matchedTripInfo.setRidePickupPoint(pickupRidePointInfo.getRidePoint());
		matchedTripInfo.setRideDropPoint(dropRidePointInfo.getRidePoint());
		matchedTripInfo.setRideRequestId(rideRequest.getId());
		matchedTripInfo.setPickupPointDistance(pickupRidePointInfo.getDistance());
		matchedTripInfo.setDropPointDistance(dropRidePointInfo.getDistance());
		matchedTripInfo.setRideRequestTravelDistance(rideRequest.getTravelDistance());
		return matchedTripInfo;
	}	

	/*
	 * Purpose - This will get all valid ride Ids based on business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	private Set<Long> getValidRides(Set<Long> rideIds, int seatRequired, RideMode rideRequestMode, User passenger, TrustNetwork trustNetwork){
		UserMapper userMapper = new UserMapper();
		UserEntity passengerEntity = userMapper.getEntity(passenger, false); 
		Set<RideEntity> validRideEntities = rideDAO.getValidRides(rideIds, seatRequired, rideRequestMode, passengerEntity);

		//This will get the first element of trust category and any ride / ride request can have only one trust category for the moment 
		//as we have removed the friend category, so logically it would be either All or Group 	
		TrustCategory rideRequestTrustCategory = trustNetwork.getTrustCategories().iterator().next();

		//Logic for matching Group
		//Get Group of Passenger irrespective of his request trust category is groups or all
		//Case 1 - Check if Ride Request Trust Category is All & Ride Trust Category is All - then its valid
		//Case 2 - If Ride Request Trust category is Groups
		//Then check ride driver groups and see if there is any common groups
		//If there is common group, then its valid 
		//Case 3 - If Ride Trust category is Groups 
		//Then check ride driver groups and see if there is any common groups
		//If there is common group, then its valid 
		
		List<GroupDetail> passengerGroups = null;
		//Get groups of passenger
		passengerGroups = RESTClientUtil.getGroups(passenger.getId());

		Set<Long> validRideIds = new HashSet<>();
		for (RideEntity rideEntity : validRideEntities) {
			TrustCategoryEntity rideTrustCategoryEntity = rideEntity.getTrustNetwork().getTrustCategories().iterator().next();
			//Case 2 - Ride Request Trust category is Groups OR Case 3 - Ride Trust category is Groups
			if (rideRequestTrustCategory.getName().equals(TrustCategoryName.Groups) || rideTrustCategoryEntity.getName().equals(TrustCategoryName.Groups)) {
				List<GroupDetail> driverGroups = RESTClientUtil.getGroups(rideEntity.getDriver().getId());
				//This will check if there is any common groups between driver and passenger
				if (passengerGroups!=null && !Collections.disjoint(passengerGroups, driverGroups)) {
					validRideIds.add(rideEntity.getId());
				}
			}
			//Case 1 - Ride Request & Ride both trust category is Anonymous
			else {
				validRideIds.add(rideEntity.getId());
			}
		}
		return validRideIds;
	}

	public MatchedTripInfo autoMatchRide(long rideRequestId) {		
		List<MatchedTripInfo> matchedTripInfos = searchRides(rideRequestId, false);
		//IMP - This will sort the matched list based on seats occupied, so that we fill the seats evenly
		//matchedTripInfos = getSortedMatchedListBySeatsOccupied(matchedTripInfos);
		//IMP - This will sort the matched list based on matched interest count, so that we find most compatible ride partners
		matchedTripInfos = getSortedMatchedListByMatchedInterest(matchedTripInfos);
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequest rideRequest = rideRequestDO.getAllData(rideRequestId);
		if (matchedTripInfos.size() > 0) {
			for (int i=0; i < matchedTripInfos.size(); i++) {
				MatchedTripInfo matchedTripInfo = matchedTripInfos.get(i);
				try {
					Ride ride = getAllData(matchedTripInfo.getRideId());
					acceptRideRequest(ride, rideRequest, matchedTripInfo);
					logger.info("Found Matching Ride for Ride Request ID:"+rideRequestId);
					return matchedTripInfo;				
				} catch (Exception e) {
					if (e instanceof RideUnavailableException || e instanceof InSufficientBalanceException) {
						continue;
					}
					//This will only come into effect when ride request itself has become unavailable which can happen 
					//if we are trying to match the ride request with multiple ride at the same ride. This may be the scenario of future
					else {
						throw e;
					}
				}
			}
		} 
		logger.info("No Matching Ride Found for Ride Request ID:"+rideRequestId);
		return null;
	}

	//This will sort the matched list based on seats occupied, so that we fill the seats evenly 
	public List<MatchedTripInfo> getSortedMatchedListBySeatsOccupied(List<MatchedTripInfo> matchedTripInfos) {

		logger.info("Pre Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());	
		}

		Collections.sort(matchedTripInfos, new Comparator<MatchedTripInfo>() {
			@Override
			public int compare(MatchedTripInfo m1, MatchedTripInfo m2) {
				Ride r1 = getAllData(m1.getRideId());
				Ride r2 = getAllData(m2.getRideId());
				int r1SeatOccupied = 0;
				for (RideRequest acceptedRideRequest : r1.getAcceptedRideRequests()) {
					r1SeatOccupied += acceptedRideRequest.getSeatRequired();
				}

				int r2SeatOccupied = 0;
				for (RideRequest acceptedRideRequest : r2.getAcceptedRideRequests()) {
					r2SeatOccupied += acceptedRideRequest.getSeatRequired();
				}

				logger.info("Seats Occupied of R1,R2 -"+r1.getId()+","+r2.getId()+"["+r1SeatOccupied+","+r2SeatOccupied+"]");
				//This will ensure we get sorted list in asc order i.e. seats which has less occupancy would show up first
				return r1SeatOccupied - r2SeatOccupied;
			}
		});

		logger.info("Post Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());
		}

		return matchedTripInfos;
	}
	
	//This will sort the matched list based on pickup time in asc order
	public List<MatchedTripInfo> getSortedMatchedListByPickupTime(List<MatchedTripInfo> matchedTripInfos) {

		logger.info("Pre Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());	
		}

		//This will return in asc order
		Collections.sort(matchedTripInfos, new Comparator<MatchedTripInfo>() {
			@Override
			public int compare(MatchedTripInfo m1, MatchedTripInfo m2) {
				if (m1.getRidePickupPoint().getRidePointProperties().get(0).getDateTime().toInstant()
						.isAfter(m2.getRidePickupPoint().getRidePointProperties().get(0).getDateTime().toInstant())) {
					return 1;
				} else if (m1.getRidePickupPoint().getRidePointProperties().get(0).getDateTime().toInstant()
						.isBefore(m2.getRidePickupPoint().getRidePointProperties().get(0).getDateTime().toInstant())) {
					return -1;
				}
				return  0;
			}
		});

		logger.info("Post Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());
		}

		return matchedTripInfos;
	}
	
	//This will sort the matched list based on matched interest, so that we get the most compatible people matched 
	public List<MatchedTripInfo> getSortedMatchedListByMatchedInterest(List<MatchedTripInfo> matchedTripInfos) {

		logger.info("Pre Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());	
		}

		Collections.sort(matchedTripInfos, new Comparator<MatchedTripInfo>() {
			@Override
			public int compare(MatchedTripInfo m1, MatchedTripInfo m2) {
				int c1 = getCommonInterestCount(m1);
				int c2 = getCommonInterestCount(m2);
				logger.info("Matched Interest Count of M1,M2 -"+m1.getRideId()+","+m2.getRideId()+"["+c1+","+c2+"]");
				//This will ensure we get sorted list in desc order i.e. more matched interest count would show up first
				return c2 - c1;
			}

		});

		logger.info("Post Sorted ride list-");
		for (MatchedTripInfo tripInfo : matchedTripInfos) {
			logger.info("Ride Id:"+tripInfo.getRideId());
		}

		return matchedTripInfos;
	}
	
	private int getCommonInterestCount(MatchedTripInfo matchedTripInfo) {
		User u1 = get(matchedTripInfo.getRideId()).getDriver();
		RideRequestDO rideRequestDO = new RideRequestDO();
		User u2 = rideRequestDO.get(matchedTripInfo.getRideRequestId()).getPassenger();
		
		Collection<Interest> u1Interests = u1.getInterests();
		Collection<Interest> u2Interests = u2.getInterests();
		u1Interests.retainAll(u2Interests);
		int matchedInterestCount = u1Interests.size();
		return matchedInterestCount;
	}
	
	
	public void processAllPendingInvoicePayment() {
		List<RideEntity> ridesWithPendingInvoice = rideDAO.getRidesWithPendingInvoice();
		for (RideEntity rideEntity: ridesWithPendingInvoice) {
			setRideEntity(rideEntity);
			logger.info("Processing Payments of pending invoice with ride id:"+ride.getId());
			RESTClientUtil.makePayment(ride);
		}
	}


	public List<RidePoint> getAllRidePointsOfRide(long rideId) {
		return ridePointDAO.getAllRidePointsOfRide(rideId);
	}

	public FeatureCollection getAllRidePoints(){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getAllRidePoints();
	}

	public FeatureCollection getMatchingRides(long rideRequestId){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getMatchingRides(rideRequestId);
	}

	public FeatureCollection getRidePoints(long rideId){
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getRidePoints(rideId);
	}

	public List<Feature> getRideGeoJson(Ride ride) {
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getRideGeoJson(ride);
	}

	public FeatureCollection getMatchedTripInfoGeoJSON(List<MatchedTripInfo> matchedTripInfos) {
		RideGeoJSON rideGeoJSON = new RideGeoJSON(this);
		return rideGeoJSON.getMatchedTripInfoGeoJSON(matchedTripInfos);

	}

	/*
	 * Purpose - Get the status of ride
	 */
	public RideStatus getStatus(long rideId){
		return rideDAO.getStatus(rideId);
	}

	public void acceptRideRequest(Ride ride, RideRequest rideRequest, MatchedTripInfo matchedTripInfo){
		RideAction rideAction = new RideAction(this);
		rideAction.acceptRideRequest(ride, rideRequest, matchedTripInfo);
	}

	public void startRide(long rideId){
		RideAction rideAction = new RideAction(this);
		rideAction.startRide(rideId);
	}

	public void pickupPassenger(long rideId, long rideRequestId){
		RideAction rideAction = new RideAction(this);
		rideAction.pickupPassenger(rideId, rideRequestId);
	}

	public void dropPassenger(long rideId, long rideRequestId, RideMode rideMode, String paymentCode){
		RideAction rideAction = new RideAction(this);
		rideAction.dropPassenger(rideId, rideRequestId, rideMode, paymentCode);
	}

	public void endRide(long rideId){
		RideAction rideAction = new RideAction(this);
		rideAction.endRide(rideId);
	}

	public void cancelAcceptedRideRequest(long rideId, long rideRequestId, CancellationType cancellationType){
		RideAction rideAction = new RideAction(this);
		rideAction.cancelAcceptedRideRequest(rideId, rideRequestId, cancellationType);
	}

	public void cancelRide(long rideId){
		RideAction rideAction = new RideAction(this);
		rideAction.cancelRide(rideId);
	}

	public float getPrice(RideRequest rideRequest) {
		RideAction rideAction = new RideAction(this);
		return rideAction.getPrice(rideRequest);
	}
}






































