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
import com.digitusrevolution.rideshare.common.util.MathUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.external.LatLngBounds;
import com.digitusrevolution.rideshare.common.util.external.RouteBoxer;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;
import com.digitusrevolution.rideshare.ride.data.RideRequestPointDAO;
import com.digitusrevolution.rideshare.ride.dto.RideMatchInfo;


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
		rideRequestEntity = rideRequestMapper.getEntity(rideRequest);
	}

	private void setRideRequestEntity(RideRequestEntity rideRequestEntity) {
		this.rideRequestEntity = rideRequestEntity;
		rideRequest = rideRequestMapper.getDomainModel(rideRequestEntity);
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
			getRideRequestPoint();
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
		getRideRequestPoint();
		return rideRequest;
	}

	/*
	 * This method doesn't return but set ride request points in the ride request object itself 
	 */
	private void getRideRequestPoint() {
		RideRequestPoint pickupPoint = rideRequestPointDAO.get(rideRequest.getPickupPoint().get_id());
		RideRequestPoint dropPoint = rideRequestPointDAO.get(rideRequest.getDropPoint().get_id());
		rideRequest.setPickupPoint(pickupPoint);
		rideRequest.setDropPoint(dropPoint);;
	}

	@Override
	public RideRequest getChild(int id) {
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
		rideRequest.setStatus("unfulfilled");
		int id = create(rideRequest);
		rideRequest.setId(id);

		//No need to get update Ride request as return type as in java its pass by reference, so data would be updated in the original ride request
		setRideRequestPoint(rideRequest);

		String pickupPointId = rideRequestPointDAO.create(rideRequest.getPickupPoint());
		String dropPointId = rideRequestPointDAO.create(rideRequest.getDropPoint());
		rideRequest.getPickupPoint().set_id(pickupPointId);
		rideRequest.getDropPoint().set_id(dropPointId);
		rideRequest.setId(id);
		update(rideRequest);
		logger.debug("Ride Request has been created with id:" + id);
		return id;
	}

	private void setRideRequestPoint(RideRequest rideRequest) {
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

	public FeatureCollection getAllRideRequestPoints(){
		FeatureCollection featureCollection = new FeatureCollection();

		List<RideRequest> rideRequests = getAll();
		for (RideRequest rideRequest : rideRequests) {
			List<RideRequestPoint> requestPoints = getPointsOfRideRequest(rideRequest.getId());			
			for (RideRequestPoint rideRequestPoint : requestPoints) {
				if (rideRequestPoint.get_id().equals(rideRequest.getPickupPoint().get_id())){
					Point pickupPoint = rideRequestPoint.getPoint();
					Map<String, Object> properties = getRideRequestPointProperties(rideRequestPoint, "pickuppoint");
					org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(pickupPoint);
					Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
					featureCollection.add(feature);
				} else {					
					Point dropPoint = rideRequestPoint.getPoint();
					Map<String, Object> properties = getRideRequestPointProperties(rideRequestPoint, "droppoint");
					org.geojson.Point geoJsonPoint = GeoJSONUtil.getGeoJsonPointFromPoint(dropPoint);
					Feature feature = GeoJSONUtil.getFeatureFromGeometry(geoJsonPoint, properties);
					featureCollection.add(feature);
				}
			}
		}	

		return featureCollection;
	}

	private Map<String, Object> getRideRequestPointProperties(RideRequestPoint rideRequestPoint, String pointType) {
		Map<String, Object> properties = new HashMap<>();
		properties.put("type", pointType);
		properties.put("RideRequestId", rideRequestPoint.getRideRequestId());
		properties.put("DateTimeUTC", rideRequestPoint.getDateTime());
		properties.put("DistanceVariation", rideRequestPoint.getDistanceVariation());
		return properties;
	}

	public List<RideRequestPoint> getPointsOfRideRequest(int rideRequestId) {
		return rideRequestPointDAO.getPointsOfRideRequest(rideRequestId);
	}

	/*
	 * Purpose - Get all Matching ride requests for specific ride
	 * 
	 * High Level Logic -
	 * 
	 * - Get min and max distance variation for that ride as it varies from ride to ride and based on total travel distance of ride
	 * 	 Note - All matching ride request would fall within that max distance
	 * - Get the slice count and expected result count, which would be used to compare the result count with final valid ride requests 
	 *   as well as incrementing the distance for search in case result is less than expected
	 * - Get the multi polygon for minimum distance from the ride [Multi polygon is created around route using routeboxer, 
	 * 	 so that minimum distance is covered and in general its around 3x the required distance
	 * - Pass the multi polygon to mongoDB and get all the valid ride requests within the polygon
	 * - Check if the result set is less than required result count
	 * - If its less, then check if its below 50% of the required count and if the answer is yes, then increment the distance at higher proportion
	 * 	 Else, increment at normal rate e.g. 10% [Proportional rate is 10%, 30%, 50%, 70%, 90%, 100%] and [Normal is 10, 20, 30 ... 100]
	 * - Increase the polygon size based on incremental distance and get the ride requests for the same
	 * - Repeat this, till you get the result set greater than equal to expected count or you have searched at max distance
	 * - Once you get the result set more than expected count, then process all the ride requests
	 *   Note - For incremental processing, ensure only new ride requests are getting processed
	 * - First remove the old ones which has already been processed, so that we process only new ones
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
	 * - Once all ride request is processed then save the valid ride request into final list
	 * 
	 * 
	 * Key Strategy -
	 * 
	 * - Get all the points at min. distance and then increment the distance till we find certain number of matching ride requests.
	 * - Don't get all the points first with max distance else number of points may be too high and complexity of finding the matching
	 * 	 rides would be MxN i.e. for 1 million ride request point in a 400 ride points of ride would cost 400 Millions times Big O time complexity
	 * 
	 */
	public void searchRideRequests(int rideId){
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
		int expectedResultCount = Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_REQUEST_RESULT_COUNT"));
		//Not initializing this as its getting initialized later on, so not required here
		int rideRequestResultCountByDistance;
		int rideRequestResultValidCount = 0;

		//This will hold the ride requests which is inside multi polygon
		Map<Integer, List<RideRequestPoint>> rideRequestsMap = new HashMap<>();
		//This will hold the ride requests Ids from the previous ride search based on distance, which would be used to get incremental ids only
		Set<Integer> rideRequestsIdsFromPreviousResult = new HashSet<>();
		//This will hold the final valid ride requests
		Set<RideMatchInfo> rideMatchInfoFinalResultSet = new HashSet<>();
		//This will define if the ride search has been completed at max distance
		boolean rideRequestSearchCompleted = false;

		//Reason for having value 0 so that first distance is the min distance only 
		int counter = 0;
		//Distance from route
		double distance = 0;
		//Run this loop till the time we have got valid request more than or equal expected result count or search is completed at max distance
		//In case search is not completed at max distance and valid result count has not exceeded expected count, then fetch more result and process it
		logger.debug("Min, Max, Incremental Distance:" + minDistance+","+maxDistance +","+incrementalDistance);
		while (rideRequestResultValidCount <= expectedResultCount && !rideRequestSearchCompleted){
			//Resetting this value to 0 so that after last iteration at max distance, it should not try to process data 
			rideRequestResultCountByDistance = 0;
			//Run this loop till the time we have reached max distance or result set is more than expected result count
			//***Getting Ride Requests based on Distance
			while (rideRequestResultCountByDistance <= expectedResultCount && distance <= maxDistance){
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
					rideRequestSearchCompleted = true;
				}
				logger.debug("Ride Id and Distance:" + rideId+","+distance);
				MultiPolygon polygonAroundRoute = getPolygonAroundRouteUsingRouteBoxer(ridePoints, distance);
				rideRequestsMap = rideRequestPointDAO.getAllMatchingRideRequestWithinMultiPolygonOfRide(ride,polygonAroundRoute);
				rideRequestResultCountByDistance = rideRequestsMap.size();
				logger.debug("Ride Request Result Count Based on Distance:"+rideRequestResultCountByDistance);
				//This will set the search status completed so that outer loop would exit, 
				//else it would go in infinite loop in case you never get expected count e.g. final valid count = 2 < would always be less than 10
				counter++;
			}
			//Process the data only when there is any ride request which is inside multi polygon
			if (rideRequestResultCountByDistance > 0){
				
				//*** This is important - It will remove all the old ride requests so that only new ones get processed again
				rideRequestsMap.keySet().removeAll(rideRequestsIdsFromPreviousResult);
				logger.debug("Ride Request Ids based on Distance for Processing:"+rideRequestsMap.keySet());
				//This will store the previous result ride request Ids, which would be used to get only new results so that we process only new ride requests
				//Don't just copy map keyset to this set here, as that would be copy by reference and removing from any set would effect both of them
				//So, below method would ensure that we get seperate copy of data
				for (Integer rideRequestId: rideRequestsMap.keySet()){
					rideRequestsIdsFromPreviousResult.add(rideRequestId);
				}
				logger.debug("Ride Request Ids from Previous Result based on Distance:"+rideRequestsIdsFromPreviousResult);

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
					logger.debug("Valid Request Ids based on all business criteria:"+validRideRequestIds);
					//Removing all the invalid ride request Ids
					rideMatchInfoMap.keySet().retainAll(validRideRequestIds);
					logger.debug("Phase 2 - Valid Ride Request Ids of Ride Id["+ride.getId()+"]:"+rideMatchInfoMap.keySet());
				}
				//*** Adding valid points to the final result set
				rideMatchInfoFinalResultSet.addAll(rideMatchInfoMap.values());
				logger.debug("Final Ride Request Result Ids:");
				for (RideMatchInfo rideMatchInfo : rideMatchInfoFinalResultSet) {
					logger.debug("[Id]:"+rideMatchInfo.getRideRequestId());	
				}
				rideRequestResultValidCount = rideMatchInfoFinalResultSet.size();
				logger.trace("Ride Request Result Valid Count:"+rideRequestResultValidCount);
			}
		}//End of loop for (rideRequestResultValidCount < expectedResultCount)
	}


	/*
	 * Purpose: Get all valid ride request Ids based on multiple business criteria
	 * e.g. user rating, preference, trust category etc.
	 * 
	 */
	public Set<Integer> getValidRideRequests(Set<Integer> rideRequestIds){		
		Set<RideRequestEntity> validRideRequestEntities = rideRequestDAO.getValidRideRequests(rideRequestIds);
		Set<Integer> validRideRequestIds = new HashSet<>();
		for (RideRequestEntity rideRequestEntity : validRideRequestEntities) {
			validRideRequestIds.add(rideRequestEntity.getId());
		}
		return validRideRequestIds;
	}



	/*
	 * State: ****Not ready and should not be used. Instead use RouteBoxer mechanism to get polygons
	 * Purpose: Get polygon around route at a specific distance
	 * Current Status - Its getting all the polygon points but unable to generate outer polygon based on Convex hull algorithm
	 * 
	 * Issue - 
	 * 
	 * 1. Convex Hull algorithm complexity is too high and there is no ready made available algorithm code available, 
	 *    so implementing this is a huge task and on top of it not sure how it can adversely affect the system performance
	 * 2. Even though MongoDB support multiple polygon, but generated polygons is intersecting with each other which is 
	 * 	  against the geoJSON specification, so multiple polygon is not working
	 * 
	 * High Level Logic -
	 * 
	 * - Generate left/right points at a distance on the line perpendicular to the current line and previous line
	 * - Generate left/right points at a distance on intersection line generated by intersection the angle between lines equally
	 * - For first and last point, generate extension points and add left/right points, no intersection points required
	 * - Generate outer polygon from set of all points (This step is pending)
	 * 
	 */
	private MultiPolygon getPolygonAroundRoute(int rideId){

		logger.debug("Entry CreatePolyLine");

		RideDO rideDO = new RideDO();
		List<RidePoint> ridePoints = rideDO.getAllRidePointsOfRide(rideId);
		List<Point> centerPoints = new LinkedList<>();
		for (RidePoint ridePoint : ridePoints) {
			centerPoints.add(ridePoint.getPoint());
		}
		List<Point> leftPoints = new LinkedList<>();
		List<Point> rightPoints = new LinkedList<>();
		List<Point> centerReferencePoints = new LinkedList<>();
		LatLng from;
		LatLng to;
		double currentLineHeading;
		double previousLineHeading = 0;
		//distance is in Km and 5 is the dummy value
		double distance = 5;
		from = GeoJSONUtil.getLatLng(centerPoints.get(0));

		int i = 0;
		int size = centerPoints.size();
		double SouthHeading = 180;
		double NorthHeading = 0;
		logger.trace("[size]:"+size);
		//This will hold all the polygons around the route
		MultiPolygon multiPolygon = new MultiPolygon();

		for (Point point : centerPoints) {
			to = GeoJSONUtil.getLatLng(point);
			logger.trace("[i]:"+i+" :[from,to]:"+"["+from+"],["+to+"]");
			// This will give heading in the range of (-180 to 180) degrees
			// -ve bearing is counterclockwise from North pole and +ve bearing is clocking from north pole 
			currentLineHeading = SphericalUtil.computeHeading(from, to);
			//This will convert -ve heading to +ve when θ = -129.60, then calculation would be (-129.60 + 360) % 360 = 230.39  
			currentLineHeading = MathUtil.convertToCompassBearing(currentLineHeading);
			logger.trace("currentLineHeading, previousLineHeading:"+currentLineHeading+","+previousLineHeading);
			if (i==0){
				//Will not add any point as we have only one point and even though we can add first point, 
				//then the sequence would get effected, we need to first add first point extension and then first point
				i++;
				continue;
			}
			if (i==1){
				addFirstPointAndExtension(leftPoints, rightPoints, centerReferencePoints, from, currentLineHeading,
						distance, NorthHeading,multiPolygon);	

				from = to;
				previousLineHeading = currentLineHeading;
				i++;				
				continue;
			}

			addPointsAtPerpendicularFromLine(leftPoints, rightPoints, centerReferencePoints, from,
					previousLineHeading, distance,multiPolygon);


			addPointsAtIntersectionAngle(previousLineHeading, currentLineHeading, from, distance, 
					leftPoints, rightPoints, centerReferencePoints,multiPolygon);

			addPointsAtPerpendicularFromLine(leftPoints, rightPoints, centerReferencePoints, from,
					currentLineHeading, distance,multiPolygon);

			//This is to get points around last point as well as extension point
			if (i == (size-1)){
				addLastPointAndExtension(leftPoints, rightPoints, centerReferencePoints, to, currentLineHeading,
						distance, SouthHeading,multiPolygon);
			}
			previousLineHeading = currentLineHeading;
			from = to;
			i++;
		}

		logger.trace("leftPoint size:"+leftPoints.size());
		createGeoJSONGeometry(centerReferencePoints,multiPolygon);
		logger.debug("Exit CreatePolyLine");
		return multiPolygon;
	}

	private void addQuadrilateral(List<Point> leftPoints, List<Point> rightPoints, MultiPolygon multiPolygon){
		List<Point> rectangleBox = new LinkedList<>();
		int currentLineIndex = leftPoints.size()-1;

		if (currentLineIndex > 0){
			int previousLineIndex = currentLineIndex - 1;		
			Point NWPoint = leftPoints.get(previousLineIndex);
			Point NEPoint = leftPoints.get(currentLineIndex);
			Point SEPoint = rightPoints.get(currentLineIndex);
			Point SWPoint = rightPoints.get(previousLineIndex);
			//Rectangle Box
			rectangleBox.add(NWPoint);
			rectangleBox.add(NEPoint);
			rectangleBox.add(SEPoint);
			rectangleBox.add(SWPoint);
			//This will complete the polygon by closing at start point
			rectangleBox.add(NWPoint);	
			Polygon polygon = GeoJSONUtil.getPolygonFromPoints(rectangleBox);
			logger.trace("Adding Quadrilateral of line having index (m,n):" + currentLineIndex + ","+previousLineIndex);
			multiPolygon.add(polygon);
		} else {
			logger.trace("There is only one line exist, so quadrilateral can not be created");
		}
	}

	private void addPointsAtIntersectionAngle(double previousLineHeading, double currentLineHeading, LatLng from, double distance, 
			List<Point> leftPoints, List<Point> rightPoints,List<Point> centerReferencePoints,MultiPolygon multiPolygon){
		//This will get angle between two lines (Current and Previous line)
		double angleBetweenLines = 180 + previousLineHeading - currentLineHeading;
		logger.trace("[angleBetweenLines]:"+angleBetweenLines);
		angleBetweenLines = Math.abs(angleBetweenLines);
		logger.trace("[Math.abs(angleBetweenLines)]:"+angleBetweenLines);
		angleBetweenLines = MathUtil.getMod360(angleBetweenLines);
		logger.trace("[getMod360(angleBetweenLines)]:"+angleBetweenLines);
		double intersectionHeading = currentLineHeading + (angleBetweenLines / 2);
		logger.trace("[angleBetweenLines, intersectionHeading]:"+angleBetweenLines+","+intersectionHeading);
		//Adding 180 so that we get Left point towards North
		intersectionHeading = intersectionHeading + 180;
		logger.trace("[intersectionHeading+180]:"+intersectionHeading);
		intersectionHeading = MathUtil.getMod360(intersectionHeading);
		logger.trace("[Mod360(intersectionHeading)]:"+intersectionHeading);
		logger.trace("[Final - angleBetweenLines, intersectionHeading]:"+angleBetweenLines+","+intersectionHeading);
		logger.trace("Adding Angle Intersection Points");
		//This will add left/right points at intersection angle between lines on the point
		addLeftRightPoints(from, distance, intersectionHeading, leftPoints, rightPoints, centerReferencePoints,multiPolygon);

	}

	private void addPointsAtPerpendicularFromLine(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng from, double currentLineHeading, double distance,MultiPolygon multiPolygon) {
		double perpendicularLeftHeading;
		//This will add left/right perpendicular to the line on the point
		//Adding 270 so that we get left point towards North
		perpendicularLeftHeading = currentLineHeading + 270;
		logger.trace("perpendicularLeftHeading = currenHeading + 270: "+perpendicularLeftHeading);
		perpendicularLeftHeading = MathUtil.getMod360(perpendicularLeftHeading);
		logger.trace("Mod360(perpendicularLeftHeading)"+perpendicularLeftHeading);
		logger.trace("Adding Perpendicular Points");
		addLeftRightPoints(from, distance, perpendicularLeftHeading, leftPoints, rightPoints, centerReferencePoints,multiPolygon);
	}

	private void addLastPointAndExtension(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng to, double currentLineHeading, double distance,
			double NorthHeading,MultiPolygon multiPolygon) {
		//This will get left/right point from last point
		logger.trace("Adding Last Point");
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(to, distance, NorthHeading, leftPoints, rightPoints, centerReferencePoints,multiPolygon);

		logger.trace("Adding Last Point Extension");
		//This will get extension point with the same heading as last point
		LatLng lastExtensionPoint = SphericalUtil.computeOffset(to, distance, currentLineHeading);
		//This will get left/right point from last extension point
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(lastExtensionPoint, distance, NorthHeading, leftPoints, rightPoints,centerReferencePoints,multiPolygon);
	}

	private void addFirstPointAndExtension(List<Point> leftPoints, List<Point> rightPoints,
			List<Point> centerReferencePoints, LatLng from, double currentLineHeading, double distance,
			double NorthHeading, MultiPolygon multiPolygon) {
		logger.trace("Adding First Point Extension");
		//This is to get first extension point based on first two points
		//Adding 180 to get heading in opposite direction on the same line, so that we can find a point on that line before first point
		double oppsiteDirectionHeading = currentLineHeading + 180;
		oppsiteDirectionHeading = MathUtil.getMod360(oppsiteDirectionHeading);
		logger.trace("oppsiteDirectionHeading:"+oppsiteDirectionHeading);
		//This will get extension point from first point with the heading from second point to first point
		//i.e. on the same line
		LatLng firstExtensionPoint = SphericalUtil.computeOffset(from, distance, oppsiteDirectionHeading);
		//This will get left/right point from first extension point. 
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(firstExtensionPoint, distance, NorthHeading, leftPoints, rightPoints,centerReferencePoints,multiPolygon);
		//This is important as from here, all the points require previous heading details 
		//as well as from point needs to be changed to current point
		logger.trace("Adding First Point");
		//This will add left points towards North i.e. Vertical line parallel to North/South pole
		addLeftRightPoints(from, distance, NorthHeading, leftPoints, rightPoints, centerReferencePoints,multiPolygon);
	}

	private void createGeoJSONGeometry(List<Point> centerReferencePoints, MultiPolygon multiPolygon) {

		JSONUtil<MultiPolygon> jsonUtilMultiPolygon = new JSONUtil<>(MultiPolygon.class);
		String multiPolygonGeoJson = jsonUtilMultiPolygon.getJson(multiPolygon);
		logger.debug("Multi Polygon:"+multiPolygonGeoJson);

		LineString centerLineString = GeoJSONUtil.getLineStringFromPoints(centerReferencePoints);
		String geoJsonCenterLineString = GeoJSONUtil.getGeoJsonString(centerLineString);
		logger.trace("Route:"+geoJsonCenterLineString);

		GeometryCollection geometryCollection = new GeometryCollection();
		geometryCollection.add(centerLineString);
		geometryCollection.add(multiPolygon);
		JSONUtil<GeometryCollection> jsonUtil = new JSONUtil<>(GeometryCollection.class);
		logger.debug("Geometry Collection:"+jsonUtil.getJson(geometryCollection));
	}

	private void addLeftRightPoints(LatLng from, double distance, double leftHeading, 
			List<Point> leftPoints, List<Point> rightPoints, List<Point> centerReferencePoints, MultiPolygon multiPolygon){
		double rightHeading = leftHeading + 180;
		rightHeading = MathUtil.getMod360(rightHeading);
		logger.trace("leftHeading,rightHeading:"+leftHeading+","+rightHeading);
		LatLng left = SphericalUtil.computeOffset(from, distance, leftHeading);
		LatLng right = SphericalUtil.computeOffset(from, distance, rightHeading);
		leftPoints.add(GeoJSONUtil.getPoint(left));
		rightPoints.add(GeoJSONUtil.getPoint(right));
		centerReferencePoints.add(GeoJSONUtil.getPoint(from));
		logger.trace("[Count]:[center,left,right]:"+leftPoints.size()+":["+from+","+left+","+right+"]");
		//This will add polygon of current line & previous line from Left/Right points
		addQuadrilateral(leftPoints, rightPoints, multiPolygon);
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
	public MultiPolygon getPolygonAroundRouteUsingRouteBoxer(List<RidePoint> ridePoints, double distance){

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
		logger.debug("MultiPolygon:"+jsonUtilMultiPolygon.getJson(multiPolygon));

		LineString routeLineString = GeoJSONUtil.getLineStringFromPoints(routePoints);
		JSONUtil<LineString> jsonUtilLineString = new JSONUtil<>(LineString.class);
		logger.trace("Route line:"+jsonUtilLineString.getJson(routeLineString));

		GeometryCollection geometryCollection = new GeometryCollection();
		geometryCollection.add(routeLineString);
		geometryCollection.add(multiPolygon);
		JSONUtil<GeometryCollection> jsonUtilGeometryCollection = new JSONUtil<>(GeometryCollection.class);
		logger.debug("Geomtry:"+jsonUtilGeometryCollection.getJson(geometryCollection));
		return multiPolygon;
	}	
}










































