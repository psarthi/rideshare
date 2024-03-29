package com.digitusrevolution.rideshare.ride.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePointProperty;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.dto.RidePointInfo;
import com.digitusrevolution.rideshare.model.ride.dto.RideSearchPoint;
import com.digitusrevolution.rideshare.ride.domain.core.RideRequestDO;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.result.DeleteResult;

public class RidePointDAO{

	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("ride_point");
	private final JSONUtil<RidePoint> jsonUtil = new JSONUtil<>(RidePoint.class);
	private static final Logger logger = LogManager.getLogger(RidePointDAO.class.getName());

	public String create(RidePoint ridePoint) {
		ObjectId _id = new ObjectId();
		ridePoint.set_id(_id.toString());
		String json = jsonUtil.getJson(ridePoint);
		collection.insertOne(Document.parse(json));
		logger.trace(json);
		return ridePoint.get_id();
	}

	public void createBulk(Collection<RidePoint> ridePoints) {
		List<Document> documents = new ArrayList<>();
		for (RidePoint ridePoint : ridePoints) {
			String json = jsonUtil.getJson(ridePoint);
			documents.add(Document.parse(json));
			logger.trace(json);
		}
		if (documents.isEmpty()){
			throw new IllegalArgumentException("Empty documents is not allowed");
		} else {
			collection.insertMany(documents);
		}
	}

	public RidePoint get(String _id) {
		Document document = collection.find(eq("_id", _id)).first();
		String json = document.toJson();
		logger.trace(json);
		RidePoint ridePoint = jsonUtil.getModel(json);
		return ridePoint;
	}

	public RidePoint getRidePointOfRide(String _id, long rideId) {
		Document document = collection.find(eq("_id", _id)).first();
		String json = document.toJson();
		logger.trace(json);
		RidePoint ridePoint = jsonUtil.getModel(json);
		ridePoint = getSpecificRidePoint(ridePoint, rideId);
		return ridePoint;
	}

	private RidePoint getSpecificRidePoint(RidePoint ridePoint, long rideId){
		List<RidePointProperty> ridePointProperties = ridePoint.getRidePointProperties();
		Iterator<RidePointProperty> iterator = ridePointProperties.iterator();
		while(iterator.hasNext()){
			long id = iterator.next().getId();
			if (id != rideId){
				logger.trace("Removing Ride Id:" + id);
				iterator.remove();
			} else {
				logger.trace("Matched Ride Id, so not removing:" + id);
			}			
		}
		logger.trace("Final Ride:"+ridePoint.toString());
		return ridePoint;
	}

	public void update(RidePoint ridePoint) {
		String json = jsonUtil.getJson(ridePoint);
		String _id = ridePoint.get_id();
		/*
		 * Don't use updateOne as it seems there is some bug in the code
		 * which throws _id field not valid in BSON, so use replaceOne
		 */
		collection.replaceOne(eq("_id", _id), Document.parse(json));
	}
	
	public void updateAll(Collection<RidePoint> ridePoints) {
		for (RidePoint ridePoint : ridePoints) {
			String json = jsonUtil.getJson(ridePoint);
			String _id = ridePoint.get_id();
			//Not using updateMany as we need to update each points with different data
			collection.replaceOne(eq("_id", _id), Document.parse(json));
			logger.trace(json);
		}
		
	}

	public void delete(String _id) {
		collection.deleteOne(eq("_id", _id));
	}
	
	public void deleteAllRidePointsOfRide(long rideId) {
		Document query = new Document("rides.id",rideId);
		DeleteResult result = collection.deleteMany(query);
		logger.debug("Total ride points deleted for ride:"+rideId+" is: "+result.getDeletedCount());
	}


	public List<RidePoint> getAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		return getAllRidePointFromDocuments(cursor);
	}

	public List<RidePoint> getAllRidePointsOfRide(long rideId) {
		logger.entry();
		MongoCursor<Document> cursor = collection.find(eq("rides.id", rideId)).iterator();
		logger.exit();
		return getAllSpecificRidePointFromDocuments(cursor, rideId);
	}
	
	public List<RidePoint> getAllRidePointsWithRecurringRides(long parentRideId) {
		logger.entry();
		MongoCursor<Document> cursor = collection.find(eq("rides.id", parentRideId)).iterator();
		logger.exit();
		return getAllRidePointFromDocuments(cursor);
	}


	public List<RidePoint> getAllRidePointWithinGivenGeometry(Geometry geometry){
		MongoCursor<Document> cursor = collection.find(geoWithin("point", geometry)).iterator();
		return getAllRidePointFromDocuments(cursor);
	}

	/*
	 * 
	 * Purpose: Get all matching ride which is near the pickup/ride point within specified time limit as well. 
	 * This function should work for all rides or for specific ride as well.  
	 * 
	 * High level logic -
	 *
	 * - Get all/specific ride points based on time and distance near a given point using GeoNear
	 * - Unwind all the matched points, so that we can get exact matched ride instead of all recurring rides which is stored for each point
	 * - Run the query again to get all matching ride points based on Just time
	 * - Group all the matching ride points by Ride ID and get the first one as its a sorted list and first one is the closest from the given point
	 * 	 This is primarily applicable to those cases where you have multiple ride points matched within pickup/drop zone and you need to find out 
	 *   the nearest one
	 * - Convert the result into the map of Ride id, ridepoint with calculated distance as well
	 * 
	 * Key Note - 
	 * 
	 * 1. Reason behind using $geoNear command instead of nearSphere as later command is not supported on replica set or in sharding environment
	 *
	 * Note -
	 *  
	 * 1. Reason behind this function in this class instead of riderequestDAO as collection used is ride_point and not rideRequest_point 
	 */
	private Map<Long, RidePointInfo> getAllMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint, long rideId, boolean flexible){

		logger.trace("Ride Request Point:"+rideRequestPoint.getPoint().toString());	
		long variationInSeconds = DateTimeUtil.getSeconds(rideRequestPoint.getTimeVariation());
		logger.trace("Time Variation in Seconds:" + variationInSeconds);
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_SEARCH_MIN_DISTANCE"));
		int maxDistance = rideRequestPoint.getDistanceVariation();
		//IMP - This will take care of finding options for flexible time and distance
		long flexiblePickupTimeVariationInSeconds = Long.parseLong(PropertyReader.getInstance().getProperty("FLEXIBLE_PICKUP_TIME_VARIATION"));
		double flexibleDistanceVariationPercentage = Double.parseDouble(PropertyReader.getInstance().getProperty("FLEXIBLE_DISTANCE_VARIATION"));
		RideRequestDO rideRequestDO = new RideRequestDO();
		int flexibleDistance = (int) (rideRequestDO.get(rideRequestPoint.getRideRequestId()).getTravelDistance() * flexibleDistanceVariationPercentage);
		
		if (flexible) {
			//IMP - This will ensure before and after flexible variation has been applied
			//i.e. if pickup time was 9 then it will search between 7 to 11 if flexible time is 2 Hrs.
			variationInSeconds = flexiblePickupTimeVariationInSeconds;
			if (flexibleDistance > maxDistance)maxDistance = flexibleDistance;
		}

		Document query;
		long earliestTimeEpocSecond = rideRequestPoint.getDateTime().minusSeconds(variationInSeconds).toEpochSecond();
		long latestTimeEpochSecond = rideRequestPoint.getDateTime().plusSeconds(variationInSeconds).toEpochSecond();
		long currentTimeEpochSecond = DateTimeUtil.getCurrentTimeInUTC().toEpochSecond();
		//This will set the earliest time to current time which include variation, so that we don't match for the past time
		//No need to do this for latestTime as while validation it validates for gt and le and if one of them is updated to current time, then other condition is already taken care
		//e.g. if condition is - time > 9:15 and < 9:00. So if 9:15 is matched to current time, then it doesn't matter what the other condition is as until both condition is valid
		//result would not come up
		if (currentTimeEpochSecond >= earliestTimeEpocSecond) {
			earliestTimeEpocSecond = currentTimeEpochSecond;
		}
		
		

		//***This is important, as depending on the input of rideId, it will either get all matching rides or specific ride
		//This will get all ride points based on ride request point date time and its variation
		//Note - we don't have to specifically add drop buffer as its already included while creating the ride request
		if (rideId==-1){
			query = new Document("rides.dateTime", new Document("$gte", earliestTimeEpocSecond)
					.append("$lte", latestTimeEpochSecond));			
		} else {
			query = new Document("rides.dateTime", new Document("$gte", earliestTimeEpocSecond)
					.append("$lte", latestTimeEpochSecond)).append("rides.id", rideId);			
		}

		Point point = rideRequestPoint.getPoint();
		JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
		String pointJson = jsonUtilPoint.getJson(point);
		logger.trace(pointJson);

		//This will get all the ride points based on max distance from ride request point 
		//i.e circle with center as ride request point and radius as max distance 
		Document geoNear = new Document("$geoNear",new Document("spherical",true)
				.append("maxDistance", maxDistance)
				.append("minDistance", minDistance)
				.append("query", query)
				.append("near", Document.parse(pointJson))
				.append("distanceField", "distance"));
		
		Document limit = new Document("$limit", Integer.parseInt(PropertyReader.getInstance().getProperty("RIDE_POINT_SEARCH_RESULT_LIMIT")));

		//This will create individual ride points by ride id
		//Unwind command, create multiple document for each array item i.e. array item would be different but rest of the property would be same
		Document unwind = new Document("$unwind", "$rides");

		//This will filter all the ride points by date and time again post the unwind command
		Document match = new Document("$match",query);
		
		//This will group by ride ids, and get the first point which is at the shortest distance
		//Result from geoNear would be sorted and that's why first point is the nearest
		//This is applicable for those cases where you have many matching ride points of the same ride within pickup/drop zone
		//so you need the nearest one as ride pickup point
		Document group = new Document("$group", new Document("_id","$rides.id")
				.append("rideSearchPoint", new Document("$first", "$$CURRENT")));

		//Nor required, its here for just for reference
		//Document sort = new Document("$sort", new Document("ridepoint.distance", 1));


		logger.trace(query.toJson());
		logger.trace(geoNear.toJson());
		logger.trace(limit.toJson());
		logger.trace(unwind.toJson());
		logger.trace(match.toJson());
		logger.trace(group.toJson());
		//logger.trace(sort.toJson());

		List<Document> pipeline = new ArrayList<>();
		pipeline.add(geoNear);
		pipeline.add(limit);
		pipeline.add(unwind);
		pipeline.add(match);
		pipeline.add(group);
		//pipeline.add(sort);

		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		Map<Long, RideSearchPoint> rideSearchPointMap = new HashMap<>();
		JSONUtil<RideSearchPoint> jsonUtilRideSearchPoint = new JSONUtil<>(RideSearchPoint.class);
		int count =0;
		try {
			while (cursor.hasNext()){
				RideSearchPoint rideSearchPoint = new RideSearchPoint();
				Document document = cursor.next();
				//TODO - By default mongodb insert the data as integer, and we need to find a way to store as Long
				//once we are able to store as long, then getLong would work otherwise, it will throw cast exception
				Long matchedRideId = Integer.toUnsignedLong(document.getInteger("_id"));
				Document searchPoint = (Document) document.get("rideSearchPoint");
				String json = searchPoint.toJson();
				logger.trace("rideId:" + matchedRideId);
				logger.trace("searchPoint:" + searchPoint.toJson());
				rideSearchPoint = jsonUtilRideSearchPoint.getModel(json);
				rideSearchPointMap.put(matchedRideId, rideSearchPoint); 
				count++;
			}
		} finally{
			cursor.close();
		}
		logger.trace("Total Count" + count);		
		return getRidePointInfoMap(rideSearchPointMap);
	}

	public Map<Long, RidePointInfo> getAllMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint, boolean flexible){
		return getAllMatchingRidePointNearGivenPoint(rideRequestPoint,-1, flexible);
	}
	
	public RidePointInfo getRidePointOfSpecificRideNearGivenPoint(RideRequestPoint rideRequestPoint, long rideId){
		Map<Long, RidePointInfo> ridePointInfoMap = getAllMatchingRidePointNearGivenPoint(rideRequestPoint,rideId, false);	
		return ridePointInfoMap.get(rideId);
	}

	
	private Map<Long, RidePointInfo> getRidePointInfoMap(Map<Long, RideSearchPoint> rideSearchPointMap){
		Map<Long, RidePointInfo> ridePointInfoMap = new HashMap<>();

		for (Map.Entry<Long, RideSearchPoint> entry: rideSearchPointMap.entrySet() ) {
			RideSearchPoint rideSearchPoint = entry.getValue();
			Long rideId = entry.getKey();
			RidePoint ridePoint = getRidePoint(rideSearchPoint);
			RidePointInfo ridePointInfo = new RidePointInfo();
			ridePointInfo.setRidePoint(ridePoint);
			ridePointInfo.setDistance(rideSearchPoint.getDistance());
			ridePointInfoMap.put(rideId, ridePointInfo);
			logger.trace(ridePointInfo.toString());
		}	
		return ridePointInfoMap;
	}

	private RidePoint getRidePoint(RideSearchPoint rideSearchPoint) {
		RidePoint ridePoint = new RidePoint();
		ridePoint.set_id(rideSearchPoint.get_id());
		ridePoint.setPoint(rideSearchPoint.getPoint());
		RidePointProperty ridePointProperty = rideSearchPoint.getRidePointProperty();
		ridePoint.getRidePointProperties().add(ridePointProperty);
		ridePoint.setSequence(rideSearchPoint.getSequence());
		return ridePoint;
	}

	private List<RidePoint> getAllRidePointFromDocuments(MongoCursor<Document> cursor){
		List<RidePoint> ridePoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.trace(json);
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}

	private List<RidePoint> getAllSpecificRidePointFromDocuments(MongoCursor<Document> cursor, long rideId){
		List<RidePoint> ridePoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.trace(json);
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoint = getSpecificRidePoint(ridePoint, rideId);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}
}
