package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.math.google.LatLng;
import com.digitusrevolution.rideshare.common.math.google.SphericalUtil;
import com.digitusrevolution.rideshare.common.util.GeoJSONUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.external.LatLngBounds;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.ride.domain.core.PassengerStatus;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequestStatus;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.domain.TrustNetworkDO;
import com.digitusrevolution.rideshare.ride.domain.core.comp.RideRequestGeoJSON;
import com.digitusrevolution.rideshare.ride.dto.RideMatchInfo;
import com.digitusrevolution.rideshare.ride.dto.RideRequestSearchResult;


public class RideRequestDO implements DomainObjectPKInteger<RideRequest>{

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
		setRideRequestPoint(rideRequest);
	}

	@Override
	public void fetchChild() {
		rideRequest = rideRequestMapper.getDomainModelChild(rideRequest, rideRequestEntity);

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
	public void delete(int id) {
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
	public int create(RideRequest rideRequest) {
		logger.entry();
		setRideRequest(rideRequest);
		int id = rideRequestDAO.create(rideRequestEntity);
		logger.exit();
		return id;
	}

	@Override
	public RideRequest get(int id) {
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
	private void setRideRequestPoint(RideRequest rideRequest) {
		RideRequestPoint pickupPoint = rideRequestPointDAO.get(rideRequest.getPickupPoint().get_id());
		RideRequestPoint dropPoint = rideRequestPointDAO.get(rideRequest.getDropPoint().get_id());
		rideRequest.setPickupPoint(pickupPoint);
		rideRequest.setDropPoint(dropPoint);
	}

	@Override
	public RideRequest getAllData(int id) {
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
	public int requestRide(RideRequest rideRequest){
		ZonedDateTime pickupTimeUTC = rideRequest.getPickupTime().withZoneSameInstant(ZoneOffset.UTC);
		//Storing dateTime in UTC
		rideRequest.setPickupTime(pickupTimeUTC);
		rideRequest.setStatus(RideRequestStatus.Unfulfilled);
		
		//**IMP Problem - Trustnetwork gets created while creating the ride but we don't have its id and without id it will 
		//recreate the trust network while updating the ride at later part of the this function as trust network id is the primary key
		//for trust network entity. 
		//Solution - Create trust network first and set that to ride ride request. Remove cascade property from ride for trust network, 
		//so that ride request creation can happen properly, else it will throw error if it tries to recreated the same trustnetwork
		TrustNetwork trustNetwork = rideRequest.getTrustNetwork();
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		int trustNetworkId = trustNetworkDO.create(trustNetwork);
		TrustNetwork trustNetworkWithId = trustNetworkDO.get(trustNetworkId);
		rideRequest.setTrustNetwork(trustNetworkWithId);

		int id = create(rideRequest);
		rideRequest.setId(id);

		//No need to get update Ride request as return type as in java its pass by reference, so data would be updated in the original ride request
		setRideRequestPointProperties(rideRequest);

		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.debug("Ride Request has been created with id:" + id);
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
		long dropTimeBuffer = Long.parseLong(PropertyReader.getInstance().getProperty("DROP_TIME_BUFFER"));
		rideRequest.getDropPoint().setTimeVariation(rideRequest.getPickupTimeVariation().plusSeconds(dropTimeBuffer));
	}


	public List<RideRequestPoint> getPointsOfRideRequest(int rideRequestId) {
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
	 * - Get min and max distance variation for that ride as it varies from ride to ride and based on total travel distance of ride
	 * 	 Note - All matching ride request would fall within that max distance
	 * - Get the slice count and expected result count, which would be used to compare the result count with final valid ride requests 
	 *   as well as incrementing the distance for search in case result is less than expected
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
	public RideRequestSearchResult searchRideRequests(int rideId, double lastSearchDistance, int lastResultIndex){
		logger.debug("[Searching Rides Requests for Ride Id]:"+ rideId);
		RideDO rideDO = new RideDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(rideId);
		Ride ride = rideDO.get(rideId);
		double minDistancePercent = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_MIN_DISTANCE_VARIATION"));
		double maxDistancePercent = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_MAX_DISTANCE_VARIATION"));
		double minDistance = ride.getTravelDistance() * minDistancePercent;
		double maxDistance = ride.getTravelDistance() * maxDistancePercent;
		int sliceCount = Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_REQUEST_DISTANCE_VARIATION_SLICE_COUNT"));
		//Slice the max distance by slice count, so that we can increment by each slice e.g. 10% for every loop 
		//Our main objective is to increment distance by specific percentage for each iteration and that's what is incremental distance
		double incrementalDistance = maxDistance / sliceCount;
		//Distance from route
		double distance = 0;
		//If its first time search, lastSearchDistance = 0, for additional search this should be the last searched distance
		if (lastSearchDistance!=0){
			logger.debug("Additional result set request and last time search distance was at:"+lastSearchDistance);
			//This is the search request for additional result set, so it will start from last searched distance instead of starting from scratch again
			minDistance = lastSearchDistance;
		}
		int resultSetLimit = Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_REQUEST_RESULT_LIMIT"));
		//This will set the expected result set count to initial limit e.g. 10
		int expectedResultCount = resultSetLimit;
		//This is the start index of the result set
		int resultStartIndex = 0;
		//If last result Index is not equal to 0, that means its a request for additional result set
		//If its 0, then its the first result set request
		if (lastResultIndex!=0){
			logger.debug("Additional result set request and last result index was:"+lastResultIndex);
			expectedResultCount=expectedResultCount+lastResultIndex;
			//LastresultIndex value is based on starting index 0
			//But resultStartIndex/EndIndex is based on index 0
			//So if the lastResultIndex is 9, then starting index would be 10, that's why we need to increase the count by 1
			resultStartIndex = lastResultIndex + 1;
		}
		int rideRequestResultValidCount = 0;
		//Not initializing this as its getting initialized later on, so not required here
		int rideRequestResultCountByDistance;

		//This will hold the ride requests which is inside multi polygon
		Map<Integer, List<RideRequestPoint>> rideRequestsMap = new HashMap<>();
		//This will hold the ride requests Ids from the previous ride search based on distance, which would be used to get incremental ids only
		Set<Integer> rideRequestsIdsFromPreviousResult = new HashSet<>();
		//This will hold the final valid ride requests
		List<RideMatchInfo> rideMatchInfoFinalResultSet = new LinkedList<>();
		
		//This will define if the ride search has been completed at max distance
		boolean rideRequestSearchCompleted = false;
		MultiPolygon polygonAroundRoute = new MultiPolygon();

		//Reason for having value 0 so that first distance is the min distance only 
		int counter = 0;
		//Run this loop till the time we have got valid request more than or equal expected result count or search is completed at max distance
		//In case search is not completed at max distance and valid result count has not exceeded expected count, then fetch more result and process it
		logger.debug("Min, Max, Incremental Distance:" + minDistance+","+maxDistance +","+incrementalDistance);
		while (rideRequestResultValidCount <= expectedResultCount && !rideRequestSearchCompleted){
			//Resetting this value to 0 so that after last iteration at max distance, it should not try to process data 
			rideRequestResultCountByDistance = 0;
			//Run this loop till the time we have reached max distance or result set is more than expected result count
			//***Getting Ride Requests based on Distance
			while (rideRequestResultCountByDistance <= expectedResultCount && !rideRequestSearchCompleted){
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
					rideRequestSearchCompleted = true;
				}
				logger.debug("Ride Id and Distance:" + rideId+","+distance);
				polygonAroundRoute = getPolygonAroundRouteUsingRouteBoxer(ridePoints, distance);
				rideRequestsMap = rideRequestPointDAO.getAllMatchingRideRequestWithinMultiPolygonOfRide(ride,polygonAroundRoute);
				rideRequestResultCountByDistance = rideRequestsMap.size();
				logger.debug("Ride Request Result Count Based on Distance:"+rideRequestResultCountByDistance);
				//This will set the search status completed so that outer loop would exit, 
				//else it would go in infinite loop in case you never get expected count e.g. final valid count = 2 < would always be less than 10
				counter++;
			}
			//Process the data only when there is any ride request which is inside multi polygon
			if (rideRequestResultCountByDistance > 0){
				
				logger.debug("Ride Request Ids from Previous Result based on Distance:"+rideRequestsIdsFromPreviousResult);
				//*** This is important - It will remove all the old ride requests so that only new ones get processed again
				rideRequestsMap.keySet().removeAll(rideRequestsIdsFromPreviousResult);
				logger.debug("New Ride Request Ids based on Distance for Processing:"+rideRequestsMap.keySet());
				//This will store the previous result ride request Ids, which would be used to get only new results so that we process only new ride requests
				//Don't just copy map keyset to this set here, as that would be copy by reference and removing from any set would effect both of them
				//So, below method would ensure that we get seperate copy of data
				for (Integer rideRequestId: rideRequestsMap.keySet()){
					rideRequestsIdsFromPreviousResult.add(rideRequestId);
				}

				//This will get ridepoint which is having shortest distance from pickup and drop point of each ride requests
				//Final result would be stored into RideMatchInfo Map
				//***Processing all ride request for each ride points
				Map<Integer, RideMatchInfo> rideMatchInfoMap = processMatchedRideRequests(rideId, ridePoints,
						rideRequestsMap);

				//This will get all valid ride requests based on ride pickup and drop point availability as well as sequence of ride pickup and drop point
				//*** Validating ride requests based on ride direction as well as pickup and drop point availability 
				rideMatchInfoMap = validateProcessedRideRequests(ride, rideMatchInfoMap);
				
				//*** Adding valid points to the final result set
				rideMatchInfoFinalResultSet.addAll(rideMatchInfoMap.values());
				rideRequestResultValidCount = rideMatchInfoFinalResultSet.size();
				logger.trace("Ride Request Final Result Count:"+rideRequestResultValidCount);
				int index =0;
				for (RideMatchInfo rideMatchInfo : rideMatchInfoFinalResultSet) {
					logger.debug("Final Ride Request ["+index+"]:"+rideMatchInfo.getRideRequestId());
					index++;
				}

			}
		}//End of loop for (rideRequestResultValidCount < expectedResultCount)
		
		//This will update the ride request travel distance which can be used for sorting
		for (RideMatchInfo rideMatchInfo : rideMatchInfoFinalResultSet) {
			double rideRequestTravelDistance = get(rideMatchInfo.getRideRequestId()).getTravelDistance();
			rideMatchInfo.setRideRequestTravelDistance(rideRequestTravelDistance);
		}	
		//Preparing Search result
		//Need to decide on Pagination - TBD
		RideRequestSearchResult rideRequestSearchResult = new RideRequestSearchResult();
		//This will get result from last index value (would be 0 in case of first time search) to result set limit 
		int resultEndIndex = resultStartIndex + resultSetLimit;
		//This will check if the total valid result set is more than result set limit, else change the end index to actual value
		//So that we don't get index out of bound exception while fetching the result which is not even there
		if (rideRequestResultValidCount < resultEndIndex){
			resultEndIndex = rideRequestResultValidCount;
		}
		logger.debug("[Search Completed Rides Requests for Ride Id]:"+ rideId);
		logger.debug("StartIndex,EndIndex:"+resultStartIndex+","+resultEndIndex);
		logger.debug("Result count:"+(resultEndIndex - resultStartIndex));
		rideRequestSearchResult.setRideMatchInfos(rideMatchInfoFinalResultSet.subList(resultStartIndex, resultEndIndex));
		rideRequestSearchResult.setSearchDistance(distance);
		rideRequestSearchResult.setResultLastIndex(resultEndIndex);
		//rideRequestSearchResult.setMultiPolygon(polygonAroundRoute);
		return rideRequestSearchResult;
	}

	/*
	 * Purpose - Validate all the ride requests for specific ride and get all valid rides at the end
	 * 
	 * Parameter -
	 * 
	 * @ride = Ride for which we need to validate ride requests
	 * @rideMatchInfoMap = Contains shortest ride pickup/drop points details along with ride request details
	 * 
	 * Return - 
	 * 
	 * @rideMatchInfoMap = Contains valid ride pickup/drop points details along with ride request details
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
	private Map<Integer, RideMatchInfo> validateProcessedRideRequests(Ride ride, Map<Integer, RideMatchInfo> rideMatchInfoMap) {
		//Use iterator instead of using for loop with entrySet as you can't remove an entry while iterating on the same Map
		Iterator<Map.Entry<Integer, RideMatchInfo>> iterator = rideMatchInfoMap.entrySet().iterator();

		//This will get all valid ride requests based on ride pickup and drop point availability as well as sequence of ride pickup and drop point
		//*** Validating ride requests based on ride direction as well as pickup and drop point availability 
		while(iterator.hasNext()){
			Entry<Integer, RideMatchInfo> entry = iterator.next();
			RideMatchInfo rideMatchInfo = entry.getValue();
			//Validate if Ride pickup and Ride drop both exist
			//If there is any valid Ride pickup or Ride drop point then value would not be null
			//i.e. both point exist
			if (rideMatchInfo.getRidePickupPoint()!=null && rideMatchInfo.getRideDropPoint()!=null){
				//Validate if Ride pickup is before Ride drop
				//Ride pickup sequence number should be smaller than drop sequence number, then we can say pickup point is before drop point
				if (rideMatchInfo.getRidePickupPoint().getSequence() < rideMatchInfo.getRideDropPoint().getSequence()){
					logger.debug("Phase 1 - Valid Ride Request Id:"+entry.getKey());
					logger.debug("Ride Pickup and Drop Sequence number:"+rideMatchInfo.getRidePickupPoint().getSequence()+","
							+rideMatchInfo.getRideDropPoint().getSequence());
				} else {
					//Remove the invalid ride request ids entry from the map
					logger.debug("Phase 1 - InValid Ride Request Id as its going in opp direction:"+entry.getKey());
					logger.debug("Ride Pickup and Drop Sequence number:"+rideMatchInfo.getRidePickupPoint().getSequence()+","
							+rideMatchInfo.getRideDropPoint().getSequence());
					iterator.remove();
				}
			}
			else {
				//Remove the invalid ride request ids entry from the map
				logger.debug("Phase 1 - InValid Ride Request Id as there is no matching ride pickup and drop point :"+entry.getKey());
				iterator.remove();
			}
		}
		logger.debug("Phase 1 - Valid Ride Request Ids of Ride Id["+ride.getId()+"]:"+rideMatchInfoMap.keySet());

		//*** Validating ride requests based on business criteria
		if (!rideMatchInfoMap.keySet().isEmpty()){
			//Getting valid ride request Ids based on all business criteria
			Set<Integer> validRideRequestIds = getValidRideRequests(rideMatchInfoMap.keySet());
			//Removing all the invalid ride request Ids
			rideMatchInfoMap.keySet().retainAll(validRideRequestIds);
			logger.debug("Phase 2 - Valid Ride Request Ids based on all business criteria of Ride Id["+ride.getId()+"]:"+rideMatchInfoMap.keySet());
		}
		return rideMatchInfoMap;
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
	 * @rideMatchInfoMap = Contains shortest ride pickup/drop points details along with ride request details
	 * 
	 * 
	 * High level logic - 
	 * 
	 * - For each ride points, calculate the ride pickup and drop distance
	 * - Compare those distance with earlier distances and ensure you store the shortest distance for pickup and drop point and its associated ride points
	 * - Once all ride request points are processed against each ride points, you will have shortest ride pickup and drop points against each ride request
	 * 
	 */
	private Map<Integer, RideMatchInfo> processMatchedRideRequests(int rideId, List<RidePoint> ridePoints,
			Map<Integer, List<RideRequestPoint>> rideRequestsMap) {
		//Its important to have this as map else you can't get ridematch info for each ride request ids
		Map<Integer, RideMatchInfo> rideMatchInfoMap = new HashMap<>();
		int count=0;
		//This will get ridepoint which is having shortest distance from pickup and drop point of each ride requests
		//Final result would be stored into RideMatchInfo Map
		//***Processing all ride request for each ride points
		for (RidePoint ridePoint : ridePoints) {
			LatLng from = new LatLng(ridePoint.getPoint().getLatitude(), ridePoint.getPoint().getLongitude());

			for (Map.Entry<Integer, List<RideRequestPoint>> entry: rideRequestsMap.entrySet()) {
				//Pickup and drop point would always be in the same order which is taken care by rideRequestPointDAO
				RideRequestPoint pickupPoint = entry.getValue().get(0);
				RideRequestPoint dropPoint = entry.getValue().get(1);
				Integer rideRequestId = entry.getKey();
				LatLng pickupPointCordinates = new LatLng(pickupPoint.getPoint().getLatitude(), pickupPoint.getPoint().getLongitude());
				double pickupDistance = SphericalUtil.computeDistanceBetween(from, pickupPointCordinates);
				LatLng dropPointCordinates = new LatLng(dropPoint.getPoint().getLatitude(), dropPoint.getPoint().getLongitude());
				double dropDistance = SphericalUtil.computeDistanceBetween(from, dropPointCordinates);

				//Don't use ridePoints.getIndex(0).equals(ridepoint) or ridePoints.lastIndexOf(ridePoint)==0
				//as ride points equal method has been overridden to compare only by ride Id
				//So in that case, all ride points would match 
				if (count==0){
					//This will add an entry to the map while going through first ride point only
					RideMatchInfo rideMatchInfo = new RideMatchInfo();	
					//Ensure all the fields of RideMatch Info is filled up properly, so that it can be utilized later
					//We are not updating travel distance as for that we need to make db call, so this will be updated for only valid ride request Ids
					rideMatchInfo.setRideRequestId(rideRequestId);
					rideMatchInfo.setRideId(rideId);
					rideMatchInfoMap.put(rideRequestId, rideMatchInfo);
				} 				
				logger.trace("[Ride RequestId]:"+rideRequestId);
				logger.trace("[Pickup Distance, Variation]:"+pickupDistance+","+pickupPoint.getDistanceVariation());
				//This will validate if pickupDistance is within the pickup variation range
				//*** Processing pickup points
				if (pickupDistance <= pickupPoint.getDistanceVariation()){
					logger.trace("Pickup Distance is within range");
					logger.trace("[Previous Distance, Current Distance]:"+pickupDistance+","+rideMatchInfoMap.get(rideRequestId).getPickupPointDistance());
					//This will validate if pickupDistance from current ridePoint is smallest as of this iteration
					if (pickupDistance < rideMatchInfoMap.get(rideRequestId).getPickupPointDistance() || rideMatchInfoMap.get(rideRequestId).getPickupPointDistance() == 0){

						if (rideMatchInfoMap.get(rideRequestId).getPickupPointDistance() == 0){
							logger.trace("First Matched Pickup Point for ride point Sequence:"+ridePoint.getSequence());
						}else {
							logger.trace("Pickup Distance is smaller that previous smallest distance for ride point Sequence:"+ridePoint.getSequence());
						}
						logger.trace("Updating new Ride Pickup Point");
						//This will overwrite the previous values, so that at the end we will have smallest distance ride point
						rideMatchInfoMap.get(rideRequestId).setRidePickupPoint(ridePoint);
						rideMatchInfoMap.get(rideRequestId).setPickupPointDistance(pickupDistance);
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
					logger.trace("[Previous Distance, Current Distance]:"+dropDistance+","+rideMatchInfoMap.get(rideRequestId).getDropPointDistance());
					//This will validate if dropDistance from current ridePoint is smallest as of this iteration
					if (dropDistance < rideMatchInfoMap.get(rideRequestId).getDropPointDistance() || rideMatchInfoMap.get(rideRequestId).getDropPointDistance() == 0){

						if (rideMatchInfoMap.get(rideRequestId).getDropPointDistance() == 0){
							logger.trace("First Matched Drop Point for ride point Sequence:"+ridePoint.getSequence());
						}else {
							logger.trace("Drop Distance is smaller that previous smallest distance for ride point Sequence:"+ridePoint.getSequence());
						}
						logger.trace("Updating new Ride Drop Point");
						//This will overwrite the previous values, so that at the end we will have smallest distance ride point
						rideMatchInfoMap.get(rideRequestId).setRideDropPoint(ridePoint);
						rideMatchInfoMap.get(rideRequestId).setDropPointDistance(dropDistance);
					} else {
						logger.trace("Drop Distance is bigger that previous smallest distance");
					}					
				} else {
					logger.trace("Drop Distance out of range");
				}					
			} // End of loop of ride requests
			count++;
		} // End of loop for ride points
		return rideMatchInfoMap;
	}
	


	/*
	 * Purpose: Get all valid ride request Ids based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	private Set<Integer> getValidRideRequests(Set<Integer> rideRequestIds){		
		Set<RideRequestEntity> validRideRequestEntities = rideRequestDAO.getValidRideRequests(rideRequestIds);
		Set<Integer> validRideRequestIds = new HashSet<>();
		for (RideRequestEntity rideRequestEntity : validRideRequestEntities) {
			validRideRequestIds.add(rideRequestEntity.getId());
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
	
	public FeatureCollection getMatchingRideRequests(int rideId,double lastSearchDistance, int lastResultIndex){
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getMatchingRideRequests(rideId, lastSearchDistance, lastResultIndex);
	}
	
	public FeatureCollection getRideRequestPoints(int rideRequestId) {
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getRideRequestPoints(rideRequestId);
	}
	
	public List<Feature> getRideRequestGeoJSON(int rideRequestId) {
		RideRequestGeoJSON rideRequestGeoJSON = new RideRequestGeoJSON(this);
		return rideRequestGeoJSON.getRideRequestGeoJSON(rideRequestId);
	}
	
	/*
	 * Purpose - Get ride request status
	 */
	public RideRequestStatus getStatus(int rideRequestId){
		return rideRequestDAO.getStatus(rideRequestId);
	}
	
	/*
	 * Purpose - Cancel the ride request which has been raised earlier
	 * 
	 * High level logic -
	 * 
	 * - Check if the ride is unfulfilled, if yes, then you can cancel
	 * - Update the status of ride request as cancelled
	 * - If its fulfilled, then check if the passenger has been picked
	 * - If its not picked, then you can cancel, else it can't be cancelled
	 * - If not picked, then call Cancel Ride Request function of ride 
	 * - Update the status of ride request as cancelled 
	 * 
	 */
	public void cancelRideRequest(int rideRequestId){
		rideRequest = getAllData(rideRequestId);
		RideRequestStatus rideRequestStatus = rideRequest.getStatus();
		if (rideRequestStatus.equals(RideRequestStatus.Unfulfilled)){
			rideRequest.setStatus(RideRequestStatus.Cancelled);
			update(rideRequest);
		} else {
			//Reason for getting child of ride as ride Request has basic ride object which doesn't have passenger list
			int rideId = rideRequest.getAcceptedRide().getId();
			RideDO rideDO = new RideDO();
			Ride ride = rideDO.getAllData(rideId);
			RidePassenger ridePassenger = ride.getRidePassenger(rideRequest.getPassenger().getId());
			if (ridePassenger.getStatus().equals(PassengerStatus.Confirmed)){
				//This will cancel the ride request from confirmed ride
				rideDO.cancelRideRequest(ride.getId(), rideRequestId);
				//Once its cancelled from ride front, then we can cancel ride request
				rideRequest.setStatus(RideRequestStatus.Cancelled);
				update(rideRequest);
			} else {
				throw new NotAcceptableException("Ride request can't be cancelled as its already picked up. "
						+ "Passenger current status:"+ridePassenger.getStatus());
			}
		}
	}
}










































