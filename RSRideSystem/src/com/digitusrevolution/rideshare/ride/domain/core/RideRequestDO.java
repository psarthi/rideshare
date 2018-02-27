package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotAcceptableException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeometryCollection;
import org.geojson.LineString;
import org.geojson.MultiPolygon;
import org.geojson.Polygon;

import com.digitusrevolution.rideshare.common.exception.InSufficientBalanceException;
import com.digitusrevolution.rideshare.common.exception.RideRequestUnavailableException;
import com.digitusrevolution.rideshare.common.exception.RideUnavailableException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.JsonObjectMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.common.util.external.LatLngBounds;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.model.billing.domain.core.Bill;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.CancellationType;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.model.ride.dto.BasicRideRequest;
import com.digitusrevolution.rideshare.model.ride.dto.MatchedTripInfo;
import com.digitusrevolution.rideshare.model.ride.dto.PreBookingRideRequestResult;
import com.digitusrevolution.rideshare.model.ride.dto.RideRequestSearchResult;
import com.digitusrevolution.rideshare.model.ride.dto.RidesInfo;
import com.digitusrevolution.rideshare.model.ride.dto.google.Element;
import com.digitusrevolution.rideshare.model.ride.dto.google.GoogleDistance;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.VehicleCategory;
import com.digitusrevolution.rideshare.model.user.domain.VehicleSubCategory;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.dto.BasicUser;
import com.digitusrevolution.rideshare.model.user.dto.GroupDetail;
import com.digitusrevolution.rideshare.ride.data.RidePointDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.RouteDO;
import com.digitusrevolution.rideshare.ride.domain.TrustNetworkDO;
import com.digitusrevolution.rideshare.ride.domain.core.comp.RideRequestGeoJSON;


public class RideRequestDO implements DomainObjectPKLong<RideRequest>{

	private static final Logger logger = LogManager.getLogger(RideRequestDO.class.getName());
	private RideRequest rideRequest;
	private RideRequestEntity rideRequestEntity;
	private RideRequestMapper rideRequestMapper;
	private final RideRequestDAO rideRequestDAO;
	private final RideRequestPointDAO rideRequestPointDAO;

	public RideRequestDO() {
		rideRequest = new RideRequest();
		rideRequestEntity = new RideRequestEntity();
		rideRequestMapper = new RideRequestMapper();
		rideRequestDAO = new RideRequestDAO();
		rideRequestPointDAO = new RideRequestPointDAO();
	}

	public void setRideRequest(RideRequest rideRequest) {
		this.rideRequest = rideRequest;
		rideRequestEntity = rideRequestMapper.getEntity(rideRequest, true);
	}

	private void setRideRequestEntity(RideRequestEntity rideRequestEntity) {
		this.rideRequestEntity = rideRequestEntity;
		rideRequest = rideRequestMapper.getDomainModel(rideRequestEntity, false);
		setRideRequestPoints(rideRequest);
	}

	//This will set Ride Start and End Points of all Rides reference the Ride Request
	private void setRideStartAndEndPoints() {
		RideDO rideDO = new RideDO();
		if (rideRequest.getAcceptedRide() != null) rideDO.setRideStartAndEndPoints(rideRequest.getAcceptedRide());	
		for (Ride ride : rideRequest.getCancelledRides()) {
			rideDO.setRideStartAndEndPoints(ride);
		}
	}

	@Override
	public void fetchChild() {
		rideRequest = rideRequestMapper.getDomainModelChild(rideRequest, rideRequestEntity);
		//This will set Ride Start and End Points details in all reference Rides for this Ride Request
		setRideStartAndEndPoints();
	}

	@Override
	public List<RideRequest> getAll() {
		List<RideRequest> rideRequests = new ArrayList<>();
		List<RideRequestEntity> rideRequestEntities = rideRequestDAO.getAll();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			setRideRequestEntity(rideRequestEntity);
			rideRequests.add(rideRequest);
		}
		return rideRequests;
	}

	/*
	 * This method should be only used from this class
	 * Update functionality is not supported once ride request has been created
	 * 
	 * Note -
	 *
	 * 1. Update of Ride Request Points needs to be well thought - TBD
	 * 
	 */
	@Override
	public void update(RideRequest rideRequest) {
		if (rideRequest.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rideRequest.getId());
		}
		setRideRequest(rideRequest);
		rideRequestDAO.update(rideRequestEntity);

	}

	@Override
	public void delete(long id) {
		rideRequest = get(id);
		setRideRequest(rideRequest);
		rideRequestDAO.delete(rideRequestEntity);
		rideRequestPointDAO.deletePointsOfRideRequest(id);
	}

	/*
	 * This method should not be used from external classes and instead use requestRide method
	 * This method is only used internally from requestRide
	 * 
	 * Issue - 
	 * 
	 * 1. How to make this method private
	 * 
	 */
	@Override
	public long create(RideRequest rideRequest) {
		logger.entry();
		setRideRequest(rideRequest);
		long id = rideRequestDAO.create(rideRequestEntity);
		logger.exit();
		return id;
	}

	@Override
	public RideRequest get(long id) {
		rideRequestEntity = rideRequestDAO.get(id);
		if (rideRequestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideRequestEntity(rideRequestEntity);
		return rideRequest;
	}

	/*
	 * This method doesn't return but set ride request points in the ride request object itself 
	 */
	public void setRideRequestPoints(RideRequest rideRequest) {
		RideRequestPoint pickupPoint = rideRequestPointDAO.get(rideRequest.getPickupPoint().get_id());
		RideRequestPoint dropPoint = rideRequestPointDAO.get(rideRequest.getDropPoint().get_id());
		rideRequest.setPickupPoint(pickupPoint);
		rideRequest.setDropPoint(dropPoint);
		//No need to match for both the points as if one is there by default another one would also be there
		if (rideRequest.getRidePickupPoint().get_id() != null) {
			RidePointDAO ridePointDAO = new RidePointDAO();
			RidePoint ridePickupPoint = ridePointDAO.get(rideRequest.getRidePickupPoint().get_id());
			RidePoint rideDropPoint = ridePointDAO.get(rideRequest.getRideDropPoint().get_id());
			rideRequest.setRidePickupPoint(ridePickupPoint);
			rideRequest.setRideDropPoint(rideDropPoint);			
		}
	}

	@Override
	public RideRequest getAllData(long id) {
		get(id);
		fetchChild();
		return rideRequest;
	}

	/* 
	 * Purpose - Ride Request needs to be created with corresponding ride request points
	 * 
	 * High Level Logic -
	 * 
	 * - Convert pickup time to UTC
	 * - Set the status
	 * - Create Ride Request in DB
	 * - Set Ride Request point properties and then create them in NoSQL DB 
	 * 
	 */
	public long requestRide(RideRequest rideRequest){
		ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
		//Storing dateTime in UTC
		rideRequest.setPickupTime(pickupTimeUTC);
		rideRequest.setStatus(RideRequestStatus.Unfulfilled);
		rideRequest.setPassengerStatus(PassengerStatus.Unconfirmed);

		//**IMP Problem - Trustnetwork gets created while creating the ride but we don't have its id and without id it will 
		//recreate the trust network while updating the ride at later part of the this function as trust network id is the primary key
		//for trust network entity. 
		//Solution - Create trust network first and set that to ride ride request. Remove cascade property from ride for trust network, 
		//so that ride request creation can happen properly, else it will throw error if it tries to recreated the same trustnetwork
		TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		long trustNetworkId = trustNetworkDO.create(trustNetwork);
		TrustNetwork trustNetworkWithId = trustNetworkDO.get(trustNetworkId);
		rideRequest.setTrustNetwork(trustNetworkWithId);

		long id = create(rideRequest);
		rideRequest.setId(id);

		//No need to get update Ride request as return type as in java its pass by reference, so data would be updated in the original ride request
		setRideRequestPointProperties(rideRequest);

		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.info("Ride Request has been created with id:" + id);
		return id;
	}

	private void setRideRequestPointProperties(RideRequest rideRequest) {
		rideRequest.getPickupPoint().setDateTime(rideRequest.getPickupTime());		
		rideRequest.getDropPoint().setDateTime(rideRequest.getPickupTime().plusSeconds(rideRequest.getTravelTime()));
		rideRequest.getPickupPoint().setRideRequestId(rideRequest.getId());
		rideRequest.getDropPoint().setRideRequestId(rideRequest.getId());
		rideRequest.getPickupPoint().setDistanceVariation(rideRequest.getPickupPointVariation());
		rideRequest.getDropPoint().setDistanceVariation(rideRequest.getDropPointVariation());
		rideRequest.getPickupPoint().setTimeVariation(rideRequest.getPickupTimeVariation());
		//This will add buffer to the user defined variation for the drop point so that we are able to match rides
		//So logically while searching e.g. 9:00 with 15 Mins user variation and 30 Mins system buffer
		//we will search rides between 8:15 to 9:45 for drop point only. Note - This buffer is not applicable to pickup point
		long dropTimeBuffer = Long.parseLong(PropertyReader.getInstance().getProperty("DROP_TIME_BUFFER"));
		rideRequest.getDropPoint().setTimeVariation(rideRequest.getPickupTimeVariation().plusSeconds(dropTimeBuffer));
	}


	public List<RideRequestPoint> getPointsOfRideRequest(long rideRequestId) {
		return rideRequestPointDAO.getPointsOfRideRequest(rideRequestId);
	}


	/*
	 * Purpose - Get all Matching ride requests for specific ride
	 * 
	 * Parameter - 
	 * 
	 * @ rideId - Ride Id for which we need to search ride requests 
	 * @ lastSearchDistance - Last Search distance which is the distance of polygon from the route, For the first time request it should be 0
	 *  					  and subsequently this should be the value returned by previous result set, so that we don't start the search from scratch 
	 * @ lastResultIndex - Last result index value, for the first time request it would be 0 and for subsequent request it should be actual value 
	 * 					   returned by previous result
	 * 
	 * 
	 * High Level Logic -
	 * 
	 * 1. Initializing all Variables
	 * 2. Update search distance and index based on last search request
	 * 3. Get Ride Requests inside polygon around the route starting at required search distance
	 * 4. Get Ride Pickup/Drop Points which is nearest to the ride request pickup/drop points by comparing with each ride points
	 * 5. Get valid ride requests based on direction and business criteria
	 * 6. Repeat step 3-5 till we get expected valid result count or completed search at max distance
	 * 7. Prepare the final search result by adding some additional field helpful for sorting etc. 
	 * 	  and result set is based on pagination as well i.e. additional result set request
	 * 
	 * 
	 * Low Level Logic -
	 * 
	 * - Get min and max distance variation for that ride as it varies from ride to ride and based on total travel distance of ride
	 * 	 Note - All matching ride request would fall within that max distance
	 * - Get incremental distance which will be used to increment the polygon size around the route in case result count is less than expected count
	 * - Get expected result count, which would be used to compare the result count with final valid ride requests 
	 * - To support pagination, we need to ensure we capture last search distance as well as last result index
	 * - So min distance for starting search would be changed to last search distance in case its a additional result set request
	 *   In that case, last search distance would not be ZERO. Same thing applies to last result index, if its no ZERO then start index would 
	 *   be last result index + 1, which would then be used as start index for getting sub list from the final valid ride requests
	 * - Get the multi polygon for minimum distance from the ride [Multi polygon is created around route using routeboxer, 
	 * 	 so that minimum distance is covered and in general its around 3x the required distance]
	 * - Pass the multi polygon to mongoDB and get all the valid ride requests within the polygon
	 * - Check if the result set is less than required result count
	 * - If its less, then check if its below 50% of the required count and if the answer is yes, then increment the distance at higher proportion
	 * 	 Else, increment at normal rate. [Normal is 10, 20, 30 ... 100] vs [Proportional rate is 10%, 30%, 50%, 70%, 90%, 100%]
	 * - Increase the polygon size based on incremental distance and get the ride requests for the same
	 * - Repeat this, till you get the result set greater than equal to expected count or you have searched at max distance
	 * - In case you have completed the search at max distance and result set is still less than expected count, then also process all the ride requests
	 * - Once you get the result set more than expected count, then process all the ride requests and stop searching further
	 *   Note - For incremental processing, ensure only new ride requests are getting processed
	 * - First remove the old ones which has already been processed, so that we process only new ones
	 *   **** Ride Request Process Logic ****
	 * - For each ride points, calculate the ride pickup and drop distance
	 * - Compare those distance with earlier distances and ensure you store the shortest distance for pickup and drop point and its associated ride points
	 * - Once all ride request points are processed against each ride points, you will have shortest ride pickup and drop points against each ride request
	 * - For each ride request, validate if ride pickup and drop point both exist and if it does then find out if the its going in the right direction
	 *   by comparing the ride pickup sequence number with drop sequence number
	 * - Remove all the invalid ride requests which doesn't fit the above criteria
	 * - Check if you have any valid ride as of now, if yes, then validate the ride requests against business criteria such as profile rating, trust group etc.
	 * - Now you will get all the valid ride request, add them into the final result set
	 * - Compare the final result set count with expected result set 
	 * - If its less, then check if search has been completed at max distance, if no, then fetch more data and do the processing of new ride requests
	 * - Repeat this till you have expected result count or search is completed 
	 * - Once all ride request is processed then save the valid ride request into final list based on start index and result set limit
	 * 
	 * 
	 * Key Strategy -
	 * 
	 * - Get all the points at min. distance and then increment the distance till we find certain number of matching ride requests.
	 * - Don't get all the points first with max distance else number of points may be too high and complexity of finding the matching
	 * 	 rides would be MxN i.e. for 1 million ride request point in a 400 ride points of ride would cost 400 Millions times Big O time complexity
	 * 
	 */
	public RideRequestSearchResult searchRideRequests(long rideId, double lastSearchDistance, int lastResultIndex){

		// ***** Initializing Variables - Start

		logger.info("[Searching Rides Requests for Ride Id]:"+ rideId);
		RideDO rideDO = new RideDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(rideId);
		//This is important else you will not get cancelled ride requests info
		Ride ride = rideDO.getAllData(rideId);
		double minDistancePercent = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_MIN_DISTANCE_VARIATION"));
		double maxDistancePercent = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_MAX_DISTANCE_VARIATION"));
		double minDistance = ride.getTravelDistance() * minDistancePercent;
		double maxDistance = ride.getTravelDistance() * maxDistancePercent;
		//This will get number of partition total distance has to be broken into
		int partitionCount = Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_REQUEST_DISTANCE_PARTITION_COUNT"));
		// Get incremental distance which will be used to increment the polygon size around the route in case result count is less than expected count
		double incrementalDistance = maxDistance / partitionCount;
		//Distance from route
		double distance = 0;
		int resultSetLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_REQUEST_RESULT_LIMIT"));
		//This will set the expected result set count to initial limit e.g. 10
		int expectedResultCount = resultSetLimit;
		//This is the start index of the result set
		int resultStartIndex = 0;
		//If last result Index is not equal to 0, that means its a request for additional result set
		//If its 0, then its the first result set request

		int rideRequestResultValidCount = 0;
		//Not initializing this as its getting initialized later on, so not required here
		int rideRequestResultCount = 0;

		//This will hold the ride requests which is inside multi polygon
		Map<Long, List<RideRequestPoint>> rideRequestsMap = new HashMap<>();
		//This will hold the ride requests Ids from the previous ride search based on distance, which would be used to get incremental ids only
		Set<Long> rideRequestsIdsFromPreviousResult = new HashSet<>();

		//This will hold the final valid ride requests
		List<MatchedTripInfo> validMatchedTripInfos = new LinkedList<>();

		//This will define if the ride search has been completed at max distance
		boolean rideRequestSearchCompleted = false;
		MultiPolygon polygonAroundRoute = new MultiPolygon();

		// **** Initializing Variables - End

		// ***** Update search distance and index based on last search request

		//If its first time search, lastSearchDistance = 0 && lastResultIndex=0
		if (lastSearchDistance!=0 && lastResultIndex!=0){
			logger.info("Additional result set requested. Last search distance and index is: "+lastSearchDistance+","+lastResultIndex);
			//This is the search request for additional result set, so it will start from last searched distance instead of starting from scratch again
			minDistance = lastSearchDistance;
			expectedResultCount=expectedResultCount+lastResultIndex;
			//LastresultIndex value is based on starting index 0
			//But resultStartIndex/EndIndex is based on index 0
			//So if the lastResultIndex is 9, then starting index would be 10, that's why we need to increase the count by 1
			resultStartIndex = lastResultIndex + 1;
		}

		// ***** Get Valid Ride Requests inside the polygon around route with nearest ride pickup and drop points - Start

		//Reason for having value 0 so that first distance is the min distance only 
		int counter = 0;
		//Run this loop till the time we get valid request more than or equal expected result count or search is completed at max distance
		//In case search is not completed at max distance and valid result count has not exceeded expected count, then fetch more result and process it
		logger.info("Min, Max, Incremental Distance:" + minDistance+","+maxDistance +","+incrementalDistance);
		while (rideRequestResultValidCount <= expectedResultCount && !rideRequestSearchCompleted){
			//Run this loop till the time we have reached max distance or result set is more than expected result count

			//**** Get Ride Requests inside polygon around the route - Start			
			while (rideRequestResultCount <= expectedResultCount && !rideRequestSearchCompleted){
				if (rideRequestResultValidCount <= expectedResultCount / 2){
					logger.debug("Multiple Increment:"+counter);
					//This will increment at higher rates than standard value i.e. 10% , 30%, 50%, 70%, 90%, 100%
					distance = minDistance + counter * incrementalDistance * 2;
				} else {
					//This will increment by standard value e.g. 10% from previous value
					distance = minDistance + counter * incrementalDistance;	
					logger.debug("Normal Increment:"+counter);
				}
				if (distance >= maxDistance){
					//This will ensure we don't search for larger distance than required
					distance = maxDistance;
					//***Imp - This value is very important else it will get into infinite loop
					//This will set the search status completed so that outer loop would exit, 
					//else it would go in infinite loop in case you never get expected count e.g. final valid count = 2 < would always be less than 10
					rideRequestSearchCompleted = true;
				}
				logger.info("Ride Id and Distance:" + rideId+","+distance);
				polygonAroundRoute = getPolygonAroundRouteUsingRouteBoxer(ridePoints, distance);
				rideRequestsMap = rideRequestPointDAO.getAllMatchingRideRequestWithinMultiPolygonOfRide(ride,polygonAroundRoute);

				rideRequestResultCount = rideRequestsMap.size();
				logger.info("Ride Request Result Count:"+rideRequestResultCount);

				counter++;
			}		
			//**** Get Ride Requests inside polygon around the route - End 

			//**** Get valid ride request with nearest ride pickup and drop points - Start
			if (rideRequestResultCount > 0){

				logger.info("Ride Request Ids from Previous Result:"+rideRequestsIdsFromPreviousResult);
				//*** This is important - It will remove all the old ride requests so that only new ones get processed again
				rideRequestsMap.keySet().removeAll(rideRequestsIdsFromPreviousResult);
				logger.info("New Ride Request Ids for Processing:"+rideRequestsMap.keySet());
				//This will store the previous result ride request Ids, which would be used to get only new results so that we process only new ride requests
				//Don't just copy map keyset to this set here, as that would be copy by reference and removing from any set would effect both of them
				//So, below method would ensure that we get seperate copy of data
				for (Long rideRequestId: rideRequestsMap.keySet()){
					rideRequestsIdsFromPreviousResult.add(rideRequestId);
				}

				//This will process only new records
				if (rideRequestsMap.size() > 0) {
					validMatchedTripInfos = processAndGetValidRideRequests(ride, ridePoints, rideRequestsMap,validMatchedTripInfos);
					rideRequestResultValidCount = validMatchedTripInfos.size();										
				}

			} //**** Get valid ride request with nearest ride pickup and drop points - End

			//This will reset the result count for inside while loop else it will get into infinite loop
			//once this value is more than expected count and distance has not reached max
			rideRequestResultCount = 0;

		}//End of loop for (rideRequestResultValidCount < expectedResultCount)

		//*** Get Valid Ride Requests inside the polygon around route with closest ride pickup and drop points - End

		//**** Create Final Search Result Set	
		return getRideRequestSearchResult(rideId, distance, resultSetLimit, resultStartIndex, validMatchedTripInfos);
	}

	/*
	 * Purpose - 
	 * 
	 * 1. Get nearest ride pickup/drop points for each ride requests
	 * 2. Validate all the ride requests for availability of pickup & drop points, their direction and business criteria
	 * 
	 */
	private List<MatchedTripInfo> processAndGetValidRideRequests(Ride ride, List<RidePoint> ridePoints,
			Map<Long, List<RideRequestPoint>> rideRequestsMap, List<MatchedTripInfo> validMatchedTripInfos) {

		//Note - Don't reinitialize the validMatedTripInfos otherwise previous valid result would be lost

		//Reason for doing this as Phase - 0 so that we remove all invalid ride request's initially itself
		//and save unnecessary processing of them in next step which is an extensive operation
		validateBusinessCriteria(ride, rideRequestsMap);

		//This will get ridepoint which is having shortest distance from pickup and drop point of each ride requests
		//Final result would be stored into MatchedTripInfo Map

		//**** Process all ride request for each ride points
		Map<Long, MatchedTripInfo> matchedTripInfoMap = processMatchedRideRequests(ride.getId(), ridePoints,
				rideRequestsMap);

		//This will get all valid ride requests based on ride pickup and drop point availability as well as sequence of ride pickup and drop point

		//**** Validate ride requests based on ride direction, ride request time variation, distance variation, pickup and drop point availability
		//Note - This will not validate on business criteria again which has already been done in Phase - 0
		matchedTripInfoMap = validateProcessedRideRequests(ride, matchedTripInfoMap, rideRequestsMap);

		//*** Add valid points to the final result set
		validMatchedTripInfos.addAll(matchedTripInfoMap.values());
		logger.trace("Ride Request Final Result Count:"+validMatchedTripInfos.size());
		int index =0;
		for (MatchedTripInfo matchedTripInfo : validMatchedTripInfos) {
			logger.info("Final Ride Request ["+index+"]:"+matchedTripInfo.getRideRequestId());
			index++;
		}
		return validMatchedTripInfos;
	}

	/*
	 * Purpose - Get Final Result Set supporting pagination i.e. additional search request
	 * 
	 */
	private RideRequestSearchResult getRideRequestSearchResult(long rideId, double distance, int resultSetLimit,
			int resultStartIndex, List<MatchedTripInfo> validMatchedTripInfos) {
		//This will update the ride request travel distance which can be used for sorting
		for (MatchedTripInfo matchedTripInfo : validMatchedTripInfos) {
			double rideRequestTravelDistance = get(matchedTripInfo.getRideRequestId()).getTravelDistance();
			matchedTripInfo.setRideRequestTravelDistance(rideRequestTravelDistance);
		}	
		//Preparing Search result
		//Need to decide on Pagination - TBD
		RideRequestSearchResult rideRequestSearchResult = new RideRequestSearchResult();
		//This will get result from last index value (would be 0 in case of first time search) to result set limit 
		int resultEndIndex = resultStartIndex + resultSetLimit;
		//This will check if the total valid result set is more than result set limit, else change the end index to actual value
		//So that we don't get index out of bound exception while fetching the result which is not even there
		if (validMatchedTripInfos.size() < resultEndIndex){
			resultEndIndex = validMatchedTripInfos.size();
		}
		logger.info("[Search Completed Rides Requests for Ride Id]:"+ rideId);
		logger.debug("StartIndex,EndIndex:"+resultStartIndex+","+resultEndIndex);
		logger.debug("Result count:"+(resultEndIndex - resultStartIndex));
		rideRequestSearchResult.setMatchedTripInfos(validMatchedTripInfos.subList(resultStartIndex, resultEndIndex));
		rideRequestSearchResult.setSearchDistance(distance);
		rideRequestSearchResult.setResultLastIndex(resultEndIndex);
		//rideRequestSearchResult.setMultiPolygon(polygonAroundRoute);

		JSONUtil<MatchedTripInfo> jsonUtil = new JSONUtil<>(MatchedTripInfo.class);
		for (MatchedTripInfo matchedTripInfo : validMatchedTripInfos) {
			logger.info("Final Matching Trip Info:"+ jsonUtil.getJson(matchedTripInfo));	
		}
		if (validMatchedTripInfos.size() == 0) {
			logger.info("No matching Ride Request found for Ride Id:"+ rideId);
		}

		return rideRequestSearchResult;
	}

	/*
	 * Purpose - Validate all the ride requests for specific ride and get all valid rides at the end
	 * 
	 * Parameter -
	 * 
	 * @ride = Ride for which we need to validate ride requests
	 * @matchedTripInfoMap = Contains shortest ride pickup/drop points details along with ride request details
	 * 
	 * Return - 
	 * 
	 * @matchedTripInfoMap = Contains valid ride pickup/drop points details along with ride request details
	 * 
	 * High level logic -
	 * 
	 * - For each ride request, validate if ride pickup and drop point both exist and if it does then find out if the its going in the right direction
	 *   by comparing the ride pickup sequence number with drop sequence number
	 * - Remove all the invalid ride requests which doesn't fit the above criteria
	 * - Check if you have any valid ride as of now, if yes, then validate the ride requests against business criteria such as profile rating, trust group etc.
	 * - Now you will get all the valid ride request
	 * 
	 */
	private Map<Long, MatchedTripInfo> validateProcessedRideRequests(Ride ride, Map<Long, MatchedTripInfo> matchedTripInfoMap, 
			Map<Long, List<RideRequestPoint>> rideRequestsMap) {
		//Use iterator instead of using for loop with entrySet as you can't remove an entry while iterating on the same Map
		Iterator<Map.Entry<Long, MatchedTripInfo>> iterator = matchedTripInfoMap.entrySet().iterator();

		//This will get all valid ride requests based on ride pickup and drop point availability as well as sequence of ride pickup and drop point
		//*** Validating ride requests based on ride direction as well as pickup and drop point availability 
		while(iterator.hasNext()){
			Entry<Long, MatchedTripInfo> entry = iterator.next();
			MatchedTripInfo matchedTripInfo = entry.getValue();
			//Validate if Ride pickup and Ride drop both exist
			//If there is any valid Ride pickup or Ride drop point then value would not be null
			//i.e. both point exist
			logger.info("Phase 1 - Checking status of Ride Request Id:"+entry.getKey());
			if (matchedTripInfo.getRidePickupPoint()!=null && matchedTripInfo.getRideDropPoint()!=null){
				//Validate if Ride pickup is before Ride drop
				//Ride pickup sequence number should be smaller than drop sequence number, then we can say pickup point is before drop point
				if (matchedTripInfo.getRidePickupPoint().getSequence() < matchedTripInfo.getRideDropPoint().getSequence()){
					logger.info("Valid Ride Request Id based on ride direction:"+entry.getKey());
					logger.info("Ride Pickup and Drop Sequence number:"+matchedTripInfo.getRidePickupPoint().getSequence()+","
							+matchedTripInfo.getRideDropPoint().getSequence());

					//Get Ride Request Points from Request Map
					RideRequestPoint pickupPoint = null;
					RideRequestPoint dropPoint = null;
					for (Map.Entry<Long, List<RideRequestPoint>> rideRequestEntry: rideRequestsMap.entrySet()) {
						//Pickup and drop point would always be in the same order which is taken care by rideRequestPointDAO
						Long rideRequestId = rideRequestEntry.getKey();
						if (rideRequestId == matchedTripInfo.getRideRequestId()) {
							pickupPoint = rideRequestEntry.getValue().get(0);
							dropPoint = rideRequestEntry.getValue().get(1);							
						}
					}

					boolean pickupPointValidation = validateRideRequestPointTimeAndDistanceCondition(pickupPoint, 
							matchedTripInfo.getRidePickupPoint(), matchedTripInfo.getPickupPointDistance());
					boolean dropPointValidation = validateRideRequestPointTimeAndDistanceCondition(dropPoint, 
							matchedTripInfo.getRideDropPoint(), matchedTripInfo.getDropPointDistance());

					if (!pickupPointValidation || !dropPointValidation) {
						//Remove the invalid ride request ids entry from the map based on Time & Distance variation condition
						logger.info("InValid Ride Request Id as its not meeting distance variation and time variation criteria:"+entry.getKey());
						logger.info("Phase 1 - InValid Ride Request Id:"+entry.getKey());
						iterator.remove();
					} else {
						logger.info("Phase 1 - Valid Ride Request Id:"+entry.getKey());
					}
				} else {
					//Note - This can't be done before finding out the shortest ride point for each ride request points, as sequence is of ride point and ride request points
					//And direction can be found only when we know which are the matching ride points corresponding to pickup and drop points of ride request
					//Remove the invalid ride request ids entry from the map
					logger.info("Phase 1 - InValid Ride Request Id as its going in opp direction:"+entry.getKey());
					logger.info("Ride Pickup and Drop Sequence number:"+matchedTripInfo.getRidePickupPoint().getSequence()+","
							+matchedTripInfo.getRideDropPoint().getSequence());
					iterator.remove();
				}
			}
			else {
				//Remove the invalid ride request ids entry from the map
				logger.info("Phase 1 - InValid Ride Request Id as there is no matching ride pickup and drop point :"+entry.getKey());
				iterator.remove();
			}
		}
		logger.info("Phase 1 - Valid Ride Request Ids of Ride Id["+ride.getId()+"]:"+matchedTripInfoMap.keySet());
		return matchedTripInfoMap;
	}

	//No need to return anything as we are using shared map, and by removing invalid ride request Ids it would reflect everywhere
	private void validateBusinessCriteria(Ride ride, Map<Long, List<RideRequestPoint>> rideRequestsMap) {
		//*** Validating ride requests based on business criteria
		Collection<RideRequest> cancelledRideRequests = ride.getCancelledRideRequests();
		Set<Long> cancelledRideRequestIds = new HashSet<>();

		for (RideRequest rideRequest: cancelledRideRequests) {
			cancelledRideRequestIds.add(rideRequest.getId());
		}
		//This will just create a single common list of all excluded list
		Set<Long> excludedRideRequestIds = new HashSet<>();
		excludedRideRequestIds.addAll(cancelledRideRequestIds);
		logger.info("Excluded List of Ride Request Ids:"+excludedRideRequestIds);

		//IMP - This will remove all the entries of excluded list of ride requests from the ride requestMap
		rideRequestsMap.keySet().removeAll(excludedRideRequestIds);
		logger.info("Final List of Ride Request Ids for validation:"+rideRequestsMap.keySet());

		if (!rideRequestsMap.keySet().isEmpty()){
			int seatOccupied = 0;
			for (RideRequest acceptedRideRequest : ride.getAcceptedRideRequests()) {
				seatOccupied += acceptedRideRequest.getSeatRequired();
			}
			int availableSeats = ride.getSeatOffered() - seatOccupied;
			//Getting valid ride request Ids based on all business criteria
			Set<Long> validRideRequestIds = getValidRideRequests(rideRequestsMap.keySet(), availableSeats, ride.getRideMode(), 
					ride.getDriver(), ride.getTrustNetwork());
			//Removing all the invalid ride request Ids
			rideRequestsMap.keySet().retainAll(validRideRequestIds);
			logger.info("Phase 0 - Valid Ride Request Ids based on all business criteria of Ride Id["+ride.getId()+"]:"+rideRequestsMap.keySet());
		}
	}

	private boolean validateRideRequestPointTimeAndDistanceCondition(RideRequestPoint rideRequestPoint, RidePoint ridePoint, double pointDistance) {

		//Check if Ride Pickup time is within range
		long variationInSeconds = DateTimeUtil.getSeconds(rideRequestPoint.getTimeVariation());
		ZonedDateTime currentTimeInUTC = DateTimeUtil.getCurrentTimeInUTC();
		ZonedDateTime rideRequestPointEarliestTime = rideRequestPoint.getDateTime().minusSeconds(variationInSeconds);
		ZonedDateTime rideRequestPointLatestTime = rideRequestPoint.getDateTime().plusSeconds(variationInSeconds);
		//Imp. No need to overwrite latestTime as during validation it checks for both times and if one fails, other has no meaning
		//e.g. validation condition below - ridePointTime.isAfter(rideRequestPointEarliestTime) && ridePointTime.isBefore(rideRequestPointLatestTime)
		//This will take care of not matching any ride request before the current time
		if (currentTimeInUTC.isAfter(rideRequestPointEarliestTime)) {
			rideRequestPointEarliestTime = currentTimeInUTC;
		}
		
		int distanceVariation = rideRequestPoint.getDistanceVariation();

		//Check if Ride Pickup distance is within range
		ZonedDateTime ridePointTime = ridePoint.getRidePointProperties().get(0).getDateTime();
		logger.info("\nridePointTime:"+ridePointTime+
				"\nrideRequestPointEarliestTime:"+rideRequestPointEarliestTime+
				"\nrideRequestPointLatestTime:"+rideRequestPointLatestTime+
				"\npointDistance:"+pointDistance+
				"\ndistanceVariation:"+distanceVariation);
		//Note - We are validating all this with the shortest distance ride point, so this doesn't match then no other ride point can match
		if (ridePointTime.isAfter(rideRequestPointEarliestTime) && ridePointTime.isBefore(rideRequestPointLatestTime) && pointDistance <= distanceVariation) {
			logger.info("Valid Ride Request Point based on Time and distance variation criteria:[Ride Request Id, Point Id]"+rideRequestPoint.getRideRequestId()+","+rideRequestPoint.get_id());
			return true;
		}
		logger.info("InValid Ride Request Point based on Time and distance variation criteria:[Ride Request Id, Point Id]"+rideRequestPoint.getRideRequestId()+","+rideRequestPoint.get_id());
		return false;
	}

	/*
	 * Purpose - Get shortest ride pickup and drop points of the ride for each ride request 
	 * 
	 * Parameter -
	 * 
	 * @rideId = Ride id of the ride
	 * @ridePoints = All Ride points of the ride
	 * @rideRequestsMap = All ride request details with ride request Id
	 * 
	 * Return -
	 * 
	 * @matchedTripInfoMap = Contains shortest ride pickup/drop points details along with ride request details
	 * 
	 * 
	 * High level logic - 
	 * 
	 * - For each ride points, calculate the ride pickup and drop distance
	 * - Compare those distance with earlier distances and ensure you store the shortest distance for pickup and drop point and its associated ride points
	 * - Once all ride request points are processed against each ride points, you will have shortest ride pickup and drop points against each ride request
	 * 
	 */
	private Map<Long, MatchedTripInfo> processMatchedRideRequests(long rideId, List<RidePoint> ridePoints,
			Map<Long, List<RideRequestPoint>> rideRequestsMap) {
		//Its important to have this as map else you can't get matched trip info for each ride request ids
		Map<Long, MatchedTripInfo> matchedTripInfoMap = new HashMap<>();
		int count=0;
		//This will get ridepoint which is having shortest distance from pickup and drop point of each ride requests
		//Final result would be stored into MatchedTripInfo Map
		//***Processing all ride request for each ride points
		for (RidePoint ridePoint : ridePoints) {
			LatLng from = new LatLng(ridePoint.getPoint().getLatitude(), ridePoint.getPoint().getLongitude());

			for (Map.Entry<Long, List<RideRequestPoint>> entry: rideRequestsMap.entrySet()) {
				//Pickup and drop point would always be in the same order which is taken care by rideRequestPointDAO
				RideRequestPoint pickupPoint = entry.getValue().get(0);
				RideRequestPoint dropPoint = entry.getValue().get(1);
				Long rideRequestId = entry.getKey();
				LatLng pickupPointCordinates = new LatLng(pickupPoint.getPoint().getLatitude(), pickupPoint.getPoint().getLongitude());
				double pickupDistance = SphericalUtil.computeDistanceBetween(from, pickupPointCordinates);
				LatLng dropPointCordinates = new LatLng(dropPoint.getPoint().getLatitude(), dropPoint.getPoint().getLongitude());
				double dropDistance = SphericalUtil.computeDistanceBetween(from, dropPointCordinates);

				//Don't use ridePoints.getIndex(0).equals(ridepoint) or ridePoints.lastIndexOf(ridePoint)==0
				//as ride points equal method has been overridden to compare only by ride Id
				//So in that case, all ride points would match 
				if (count==0){
					//This will add an entry to the map while going through first ride point only
					MatchedTripInfo matchedTripInfo = new MatchedTripInfo();	
					//Ensure all the fields of matchedTripInfo Info is filled up properly, so that it can be utilized later
					//We are not updating travel distance as for that we need to make db call, so this will be updated for only valid ride request Ids
					matchedTripInfo.setRideRequestId(rideRequestId);
					matchedTripInfo.setRideId(rideId);
					matchedTripInfoMap.put(rideRequestId, matchedTripInfo);
				} 				
				logger.trace("[Ride RequestId]:"+rideRequestId);
				logger.trace("[Pickup Distance, Variation]:"+pickupDistance+","+pickupPoint.getDistanceVariation());
				//This will validate if pickupDistance is within the pickup variation range
				//*** Processing pickup points
				if (pickupDistance <= pickupPoint.getDistanceVariation()){
					logger.trace("Pickup Distance is within range");
					logger.trace("[Previous Distance, Current Distance]:"+pickupDistance+","+matchedTripInfoMap.get(rideRequestId).getPickupPointDistance());
					//This will validate if pickupDistance from current ridePoint is smallest as of this iteration
					if (pickupDistance < matchedTripInfoMap.get(rideRequestId).getPickupPointDistance() || matchedTripInfoMap.get(rideRequestId).getPickupPointDistance() == 0){

						if (matchedTripInfoMap.get(rideRequestId).getPickupPointDistance() == 0){
							logger.trace("First Matched Pickup Point for ride point Sequence:"+ridePoint.getSequence());
						}else {
							logger.trace("Pickup Distance is smaller that previous smallest distance for ride point Sequence:"+ridePoint.getSequence());
						}
						logger.trace("Updating new Ride Pickup Point");
						//This will overwrite the previous values, so that at the end we will have smallest distance ride point
						matchedTripInfoMap.get(rideRequestId).setRidePickupPoint(ridePoint);
						matchedTripInfoMap.get(rideRequestId).setPickupPointDistance(pickupDistance);
					} else {
						logger.trace("Pickup Distance is bigger that previous smallest distance");
					}					
				} else {
					logger.trace("Pickup Distance out of range");
				}			
				logger.trace("[Drop Distance, Variation]:"+dropDistance+","+dropPoint.getDistanceVariation());
				//This will validate if dropDistance is within the drop variation range
				//*** Processing drop points
				if (dropDistance <= dropPoint.getDistanceVariation()){
					logger.trace("Drop Distance is within range");
					logger.trace("[Previous Distance, Current Distance]:"+dropDistance+","+matchedTripInfoMap.get(rideRequestId).getDropPointDistance());
					//This will validate if dropDistance from current ridePoint is smallest as of this iteration
					if (dropDistance < matchedTripInfoMap.get(rideRequestId).getDropPointDistance() || matchedTripInfoMap.get(rideRequestId).getDropPointDistance() == 0){

						if (matchedTripInfoMap.get(rideRequestId).getDropPointDistance() == 0){
							logger.trace("First Matched Drop Point for ride point Sequence:"+ridePoint.getSequence());
						}else {
							logger.trace("Drop Distance is smaller that previous smallest distance for ride point Sequence:"+ridePoint.getSequence());
						}
						logger.trace("Updating new Ride Drop Point");
						//This will overwrite the previous values, so that at the end we will have smallest distance ride point
						matchedTripInfoMap.get(rideRequestId).setRideDropPoint(ridePoint);
						matchedTripInfoMap.get(rideRequestId).setDropPointDistance(dropDistance);
					} else {
						logger.trace("Drop Distance is bigger that previous smallest distance");
					}					
				} else {
					logger.trace("Drop Distance out of range");
				}					
			} // End of loop of ride requests
			count++;
		} // End of loop for ride points
		return matchedTripInfoMap;
	}



	/*
	 * Purpose: Get all valid ride request Ids based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	private Set<Long> getValidRideRequests(Set<Long> rideRequestIds, int availableSeats, RideMode createdRideMode, 
			User driver, TrustNetwork trustNetwork){
		UserMapper userMapper = new UserMapper();
		UserEntity driverEntity = userMapper.getEntity(driver, false);
		
		Set<RideRequestEntity> validRideRequestEntities = rideRequestDAO.getValidRideRequests(rideRequestIds, availableSeats, createdRideMode, driverEntity);
		
		//This will get the first element of trust category and any ride / ride request can have only one trust category for the moment 
		//as we have removed the friend category, so logically it would be either All or Group 	
		TrustCategory trustCategory = trustNetwork.getTrustCategories().iterator().next();
		
		
		//Logic for matching Group
		//Get Group of Driver irrespective of his request trust category is groups or all
		//Case 1 - Check if Ride Request Trust Category is All & Ride Trust Category is All - then its valid
		//Case 2 - If Ride Request Trust category is Groups
		//Then check ride driver groups and see if there is any common groups
		//If there is common group, then its valid 
		//Case 3 - If Ride Trust category is Groups 
		//Then check ride driver groups and see if there is any common groups
		//If there is common group, then its valid 
		
		List<GroupDetail> driverGroups = null;
		//Get groups of Driver
		driverGroups = RESTClientUtil.getGroups(driver.getId());
		
		Set<Long> validRideRequestIds = new HashSet<>();
		for (RideRequestEntity rideRequestEntity : validRideRequestEntities) {
			TrustCategoryEntity rideRequestTrustCategoryEntity = rideRequestEntity.getTrustNetwork().getTrustCategories().iterator().next();
			//Case 2 - Ride Request Trust category is Groups OR Case 3 - Ride Trust category is Groups
			if (trustCategory.getName().equals(TrustCategoryName.Groups) || rideRequestTrustCategoryEntity.getName().equals(TrustCategoryName.Groups)) {
				List<GroupDetail> passengerGroups = RESTClientUtil.getGroups(rideRequestEntity.getPassenger().getId());
				//This will check if there is any common groups between driver and passenger
				if (driverGroups!=null && !Collections.disjoint(driverGroups, passengerGroups)) {
					validRideRequestIds.add(rideRequestEntity.getId());
				}
			}
			//Case 1 - Ride Request & Ride both trust category is Anonymous
			else {
				validRideRequestIds.add(rideRequestEntity.getId());
			}
		}

		return validRideRequestIds;
	}


	/*
	 * Purpose: Get polygons around the route to cover specific distance around the route
	 * Reference - http://google-maps-utility-library-v3.googlecode.com/svn/trunk/routeboxer/docs/examples.html
	 * High Level Logic -
	 * 
	 * - Get Rectangles based on RouteBoxer Logic
	 * - Create Polygon from the rectangle and add it to the multi-polygon
	 *
	 * Note - You can't just take top corners of each rectangle and keep traversing to get the top line 
	 * as rectangles can be any order, so the only option is to create individual polygon from each rectangle latlngs.
	 * 
	 */
	private MultiPolygon getPolygonAroundRouteUsingRouteBoxer(List<RidePoint> ridePoints, double distance){

		RouteBoxer routeBoxer = new RouteBoxer();
		List<Point> routePoints = new LinkedList<>();
		List<com.digitusrevolution.rideshare.common.util.external.LatLng> latLngs = new LinkedList<>();
		for (RidePoint ridePoint : ridePoints) {
			double lat = ridePoint.getPoint().getLatitude();
			double lng = ridePoint.getPoint().getLongitude();
			Point point = new Point(lng, lat);
			com.digitusrevolution.rideshare.common.util.external.LatLng latLng = new com.digitusrevolution.rideshare.common.util.external.LatLng(lat,lng);
			latLngs.add(latLng);
			routePoints.add(point);
		}

		List<LatLngBounds> latLngBounds = routeBoxer.box(latLngs, distance);
		MultiPolygon multiPolygon = new MultiPolygon();

		for (LatLngBounds latLngBound : latLngBounds) {
			double SWLat = latLngBound.getSouthWest().lat;
			double SWLng = latLngBound.getSouthWest().lng;
			double NELat = latLngBound.getNorthEast().lat;
			double NELng = latLngBound.getNorthEast().lng;
			double NWLat = NELat;
			double NWLng = SWLng;
			double SELat = SWLat;
			double SELng = NELng;
			Point SWPoint = new Point(SWLng, SWLat);
			Point SEPoint = new Point(SELng, SELat);
			Point NEPoint = new Point(NELng, NELat);
			Point NWPoint = new Point(NWLng, NWLat);

			List<Point> rectangleBox = new LinkedList<>();
			//Rectangle Box
			rectangleBox.add(NWPoint);
			rectangleBox.add(NEPoint);
			rectangleBox.add(SEPoint);
			rectangleBox.add(SWPoint);
			//This will complete the polygon by closing at start point
			rectangleBox.add(NWPoint);

			Polygon polygon = GeoJSONUtil.getPolygonFromPoints(rectangleBox);
			multiPolygon.add(polygon);

		}

		JSONUtil<MultiPolygon> jsonUtilMultiPolygon = new JSONUtil<>(MultiPolygon.class);
		logger.debug("Rectangle Box Count:" + latLngBounds.size());
		logger.trace("MultiPolygon:"+jsonUtilMultiPolygon.getJson(multiPolygon));

		LineString routeLineString = GeoJSONUtil.getLineStringFromPoints(routePoints);
		JSONUtil<LineString> jsonUtilLineString = new JSONUtil<>(LineString.class);
		logger.trace("Route line:"+jsonUtilLineString.getJson(routeLineString));

		GeometryCollection geometryCollection = new GeometryCollection();
		geometryCollection.add(routeLineString);
		geometryCollection.add(multiPolygon);
		JSONUtil<GeometryCollection> jsonUtilGeometryCollection = new JSONUtil<>(GeometryCollection.class);
		logger.trace("Geomtry:"+jsonUtilGeometryCollection.getJson(geometryCollection));
		return multiPolygon;
	}	

	public FeatureCollection getAllRideRequestPoints(){
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getAllRideRequestPoints();
	}

	public FeatureCollection getMatchingRideRequests(long rideId,double lastSearchDistance, int lastResultIndex){
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
	}

	public FeatureCollection getRideRequestPoints(long rideRequestId) {
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getRideRequestPoints(rideRequestId);
	}

	public List<Feature> getRideRequestGeoJSON(long rideRequestId) {
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getRideRequestGeoJSON(rideRequestId);
	}

	/*
	 * Purpose - Get ride request status
	 */
	public RideRequestStatus getStatus(long rideRequestId){
		return rideRequestDAO.getStatus(rideRequestId);
	}

	/*
	 * Purpose - Cancel the ride request which has been raised earlier
	 * 
	 * High level logic -
	 * 
	 * - Check if the ride is unfulfilled, if yes, then you can cancel
	 * - Update the status of ride request as cancelled
	 * - If its fulfilled, then check if the passenger has not been dropped (Allow user to cancel even he/she has been picked to avoid unnecessary blocking of their money and misuse from driver side)
	 * - If its not picked, then you can cancel, else it can't be cancelled
	 * - If not picked, then call Cancel Ride Request function of ride and Update the status of ride request as cancelled 
	 * 
	 * Note - Reason for returning void for consistency with other rides action method 
	 * as well as it will give flexibility to modify this function without worrying about updating both side references
	 * e.g ride should be updated with ride request and vice versa
	 * 
	 */
	public void cancelRideRequest(long rideRequestId){
		logger.info("Cancelling Ride Request:"+rideRequestId);
		rideRequest = getAllData(rideRequestId);
		RideRequestStatus rideRequestStatus = rideRequest.getStatus();
		if (rideRequestStatus.equals(RideRequestStatus.Unfulfilled)){
			rideRequest.setStatus(RideRequestStatus.Cancelled);
			update(rideRequest);
		} else {
			long rideId = rideRequest.getAcceptedRide().getId();
			RideDO rideDO = new RideDO();
			if (!rideRequest.getPassengerStatus().equals(PassengerStatus.Dropped)){
				//This will cancel the ride request from confirmed ride as well as update the cancellation status in ride request
				rideDO.cancelAcceptedRideRequest(rideId, rideRequestId, CancellationType.RideRequest);
				//This will just ensure that we do auto match for the Ride which has got affected because of this
				autoMatchRideRequest(rideId);
			} else {
				throw new NotAcceptableException("Ride request can't be cancelled as passenger has already been dropped"
						+ "Passenger current status:"+rideRequest.getPassengerStatus());
			}
		}
	}

	/*
	 * Purpose - Get current ride request
	 */
	public RideRequest getCurrentRideRequest(long passengerId){		
		User passenger = RESTClientUtil.getUser(passengerId);
		UserMapper userMapper = new UserMapper();
		//We don't need child object of User entity, just the basic user entity is fine as it primarily needs only PK
		UserEntity passengerEntity = userMapper.getEntity(passenger, false);
		RideRequestEntity rideRequestEntity = rideRequestDAO.getCurrentRideRequest(passengerEntity);
		if (rideRequestEntity!=null) {
			setRideRequestEntity(rideRequestEntity);
			fetchChild();
			return rideRequest;
		}
		else {
			return null;
		}
	}

	public List<RideRequest> getRideRequests(long passengerId, int page){
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount; 
		//Not required as we are fetching the result with max result set defined
		//int endIndex = (page+1)*itemsCount;

		User passenger = RESTClientUtil.getUser(passengerId);
		UserMapper userMapper = new UserMapper();
		//We don't need child object of User entity, just the basic user entity is fine as it primarily needs only PK
		UserEntity passengerEntity = userMapper.getEntity(passenger, false);
		List<RideRequestEntity> rideRequestEntities = rideRequestDAO.getRideRequests(passengerEntity, startIndex);
		List<RideRequest> rideRequests = new LinkedList<>();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			setRideRequestEntity(rideRequestEntity);
			//Don't fetch child objects be it from mySQL or MongoDB as it will become very resource intensive job
			rideRequests.add(rideRequest);
		}
		//This will sort the element as per the comparator written in Ride Request class
		Collections.sort(rideRequests);
		return rideRequests;
	}

	public MatchedTripInfo autoMatchRideRequest(long rideId) {		
		RideRequestSearchResult searchRideRequests = searchRideRequests(rideId, 0, 0);
		List<MatchedTripInfo> matchedTripInfos = searchRideRequests.getMatchedTripInfos();
		RideDO rideDO = new RideDO();
		Ride ride = rideDO.getAllData(rideId);
		//IMP - This will sort the matched list based on seats occupied, so that we fill the seats evenly
		matchedTripInfos = rideDO.getSortedMatchedList(matchedTripInfos);
		if (matchedTripInfos.size() > 0) {
			for (int i=0; i < matchedTripInfos.size(); i++) {
				MatchedTripInfo matchedTripInfo = matchedTripInfos.get(i);
				try {
					RideRequest rideRequest = getAllData(matchedTripInfo.getRideRequestId());
					rideDO.acceptRideRequest(ride, rideRequest, matchedTripInfo);
					logger.info("Found Matching Ride Request for Ride ID:"+rideId);	
					//IMP - This is good as in one shot i can match many ride request, so if a driver has offered 4 seats, then 4 ride request can be served
					//otherwise, we need to wait for 4 rides to match each request which is not good
					int seatOccupied = 0;
					for (RideRequest acceptedRideRequest : ride.getAcceptedRideRequests()) {
						seatOccupied += acceptedRideRequest.getSeatRequired();
					}
					int availableSeats = ride.getSeatOffered() - seatOccupied;					
					if (availableSeats!=0) {
						continue;
					} else {
						return matchedTripInfo;	
					}				
				} catch (Exception e) {
					if (e instanceof RideRequestUnavailableException || e instanceof InSufficientBalanceException) {
						continue;
					}
					//This will only come into effect when ride itself has become unavailable which can happen 
					//if we are trying to match the ride request with multiple ride at the same ride. This may be the scenario of future
					else {
						throw e;
					}
				}
			}
		} 
		logger.info("No Matching Ride Request Found for Ride ID:"+rideId);
		return null;
	}

	/*
	 * This function is not in use as in Android, we are calculating fare there itself and calling pending bills url to get pending bills seperately 
	 * 
	 */
	public PreBookingRideRequestResult getPreBookingInfo(RideRequest rideRequest) {

		BasicUser passenger = JsonObjectMapper.getMapper().convertValue(rideRequest.getPassenger(), BasicUser.class);
		List<Bill> pendingBills = RESTClientUtil.getPendingBills(passenger);
		RideDO rideDO = new RideDO();
		float price = rideDO.getPrice(rideRequest);	
		//Note - Reason for not sending googleDistance back to the client and resending 
		//along with confirmation request is to avoid unnecessary implication if the user 
		//changes the date/time then distance is no more valid so its better to take google distance 
		//time etc. post final confirmation in the backend
		PreBookingRideRequestResult preBookingRideRequestResult = new PreBookingRideRequestResult();
		preBookingRideRequestResult.setMaxFare(price);
		preBookingRideRequestResult.setPendingBills(pendingBills);
		return preBookingRideRequestResult;
	}
}










































