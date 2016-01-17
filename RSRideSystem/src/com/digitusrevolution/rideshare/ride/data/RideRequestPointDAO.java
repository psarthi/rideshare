package com.digitusrevolution.rideshare.ride.data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.geojson.MultiPolygon;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.ride.dto.RideRequestSearchPoint;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;

public class RideRequestPointDAO{

	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("rideRequest_point");
	private final JSONUtil<RideRequestPoint> jsonUtil = new JSONUtil<>(RideRequestPoint.class);
	private static final Logger logger = LogManager.getLogger(RideRequestPointDAO.class.getName());

	public String create(RideRequestPoint rideRequestPoint) {
		ObjectId _id = new ObjectId();
		rideRequestPoint.set_id(_id.toString());
		String json = jsonUtil.getJson(rideRequestPoint);
		collection.insertOne(Document.parse(json));
		return rideRequestPoint.get_id();
	}

	public RideRequestPoint get(String _id) {
		Document document = collection.find(Filters.eq("_id", _id)).first();
		String json = document.toJson();
		RideRequestPoint rideRequestPoint = jsonUtil.getModel(json);
		return rideRequestPoint;
	}

	public void update(RideRequestPoint rideRequestPoint) {
		String json = jsonUtil.getJson(rideRequestPoint);
		String _id = rideRequestPoint.get_id();
		/*
		 * Don't use updateOne as it seems there is some bug in the code
		 * which throws _id field not valid in BSON, so use replaceOne
		 */
		collection.replaceOne(Filters.eq("_id", _id), Document.parse(json));
	}

	public void delete(String _id) {
		collection.deleteOne(Filters.eq("_id", _id));
	}

	public void deletePointsOfRideRequest(int rideRequestId) {
		Document query = new Document("rideRequestId",rideRequestId);
		DeleteResult result = collection.deleteMany(query);
		logger.debug("Total points deleted for Ride Request:"+rideRequestId+" is: "+result.getDeletedCount());
	}

	public List<RideRequestPoint> getAll() {
		List<RideRequestPoint> rideRequestPoints = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				RideRequestPoint rideRequestPoint = jsonUtil.getModel(json);
				rideRequestPoints.add(rideRequestPoint);
			}
		} finally{
			cursor.close();
		}
		return rideRequestPoints;
	}

	public List<RideRequestPoint> getPointsOfRideRequest(int rideRequestId) {
		List<RideRequestPoint> rideRequestPoints = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find(Filters.eq("rideRequestId", rideRequestId)).iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				RideRequestPoint rideRequestPoint = jsonUtil.getModel(json);
				rideRequestPoints.add(rideRequestPoint);
			}
		} finally{
			cursor.close();
		}
		return rideRequestPoints;
	}

	/*
	 * Purpose - Get all valid ride request point around any ride point
	 * 
	 */
	public void getAllMatchingRideRequestPointNearGivenPoint(RidePoint ridePoint, double minDistance, double maxDistance){
		
		Set<RideRequestSearchPoint> rideRequestSearchPointsSet = new HashSet<>();
		Set<Integer> rideRequestIdsSet = new HashSet<>();
		logger.debug("Start - Searching for Ride Point:"+ridePoint.getPoint().toString());	
		Point point = ridePoint.getPoint();
		JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
		String pointJson = jsonUtilPoint.getJson(point);
		logger.trace(pointJson);

		Document geoNear = new Document("$geoNear",new Document("spherical",true)
				.append("limit", PropertyReader.getInstance().getProperty("RIDE_REQUEST_POINT_SEARCH_RESULT_LIMIT"))
				.append("maxDistance", maxDistance)
				.append("minDistance", minDistance)
				.append("near", new Document(Document.parse(pointJson)))
				.append("distanceField", "distance"));

		logger.trace(geoNear.toJson());
		
		List<Document> pipeline = new ArrayList<>();
		pipeline.add(geoNear);

		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		JSONUtil<RideRequestSearchPoint> jsonUtilRideRequestSearchPoint = new JSONUtil<>(RideRequestSearchPoint.class);
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				logger.trace("document:"+document.toJson());
				RideRequestSearchPoint rideRequestSearchPoint = jsonUtilRideRequestSearchPoint.getModel(document.toJson());
				long timeVariation = DateTimeUtil.getSeconds(rideRequestSearchPoint.getTimeVariation());
				long rideDateTime = ridePoint.getRidesBasicInfo().get(0).getDateTime().toEpochSecond();
				boolean dateTimeCondition = (rideDateTime >= rideRequestSearchPoint.getDateTime().minusSeconds(timeVariation).toEpochSecond() && 
						rideDateTime <= rideRequestSearchPoint.getDateTime().plusSeconds(timeVariation).toEpochSecond());
				boolean distanceCondition = (rideRequestSearchPoint.getDistance() <= rideRequestSearchPoint.getDistanceVariation());
				logger.trace("RideRequest Search Point:"+jsonUtilRideRequestSearchPoint.getJson(rideRequestSearchPoint));
				if (distanceCondition && dateTimeCondition){
					logger.trace("Datetime or Distance condition passsed, so adding rideRequest Id:" + rideRequestSearchPoint.getRideRequestId());
					rideRequestSearchPointsSet.add(rideRequestSearchPoint);
					rideRequestIdsSet.add(rideRequestSearchPoint.getRideRequestId());
				} else {
					logger.trace("Datetime or Distance condition failed, so invalid rideRequest Id:" + rideRequestSearchPoint.getRideRequestId());
				}
			}
		} finally{
			cursor.close();
		}
		logger.debug("End - Searching Ride Request Points");
		logger.debug("Matching Unique Ride Request Points Ids:" + rideRequestIdsSet);
	}

	/*
	 * Purpose - Get all valid ride request points which is inside multiple polygons created around the route
	 * Return - It will return a map of Ride Request id and value as ride request points, 
	 * 			***where first point in the list would be pickup and 2nd one is drop point
	 * 
	 * High Level Logic -
	 * 
	 * - Get all ride request points which is inside multi polygon
	 * - Filter all the points which is outside the date time range of ride start and end point with variation and drop buffer added
	 * - Group by the ride request Ids and then calculate the number of ride request points in the array of ride ids
	 * - Filter all those ride request ids where count is not equal to 2 i.e. it doesn't have both pickup and drop point within the polygon
	 * - So final result would get all the valid ride request points
	 * - Create a map with key as ride request id and value as ride request points 
	 * 	 (Store as a list, where 1st item in the list is the pickup point and 2nd as drop point)    
	 * 
	 */
	public Map<Integer, List<RideRequestPoint>> getAllMatchingRideRequestWithinMultiPolygonOfRide(Ride ride, MultiPolygon multiPolygon){

		JSONUtil<MultiPolygon> jsonUtilMultiPolygon = new JSONUtil<>(MultiPolygon.class);
		String jsonMultipPolygon = jsonUtilMultiPolygon.getJson(multiPolygon);
		long maxTimeVariation = Long.parseLong(PropertyReader.getInstance().getProperty("MAX_PICKUP_TIME_VARIATION"));
		long dropTimeBuffer = Long.parseLong(PropertyReader.getInstance().getProperty("DROP_TIME_BUFFER"));
		ZonedDateTime endTime = ride.getEndPoint().getRidesBasicInfo().get(0).getDateTime();

		//This will get ride request point inside the polygons
		Document geoWithinQuery = new Document("point", new Document("$geoWithin", new Document("$geometry", new Document(Document.parse(jsonMultipPolygon)))));
		//This will filter ride request point based on ride start and end time by adding max pickup variation and drop buffer as well
		//for e.g. If a ride is starting at 8 AM and ending at 9 AM, so with max variation added (2 Hrs) and (30Mins drop buffer to cover up route variation) 
		//valid ride request points would be >=6 AM and <=11:30AM, so if someone has requested for pickup at 5:30 AM, so max pickup time would be 7:30AM
		//Based on that, 8AM ride would not be valid
		Document dateTimeFilter = new Document("dateTime", new Document("$gte", ride.getStartTime().minusSeconds(maxTimeVariation).toEpochSecond())
				.append("$lte", endTime.plusSeconds(maxTimeVariation).plusSeconds(dropTimeBuffer).toEpochSecond()));

		Document matchGeoWithin = new Document("$match",geoWithinQuery);
		Document matchDateTimeFilter = new Document("$match",dateTimeFilter);

		//This will group ride request points by its id
		Document groupByRideRequestId = new Document("$group", new Document("_id","$rideRequestId")
				.append("rideRequestSearchPoint", new Document("$push", "$$CURRENT")));

		//This will filter those ride requests which doesn't have pickup and drop both available
		Document rideRequestPointCountFilter = new Document("rideRequestSearchPoint", new Document("$size", 2));
		Document matchrideRequestPointCountFilter = new Document("$match",rideRequestPointCountFilter);

		/* Below code block is just for reference purpose, which will achieve the same result as matchrideRequestPointCountFilter above statement-
		//This will add additional count field which shows the number of ride request points available for a particular ride request Id
		Document projectRideRequestPointCount = new Document("$project", new Document("rideRequestSearchPoint", 1)
															  .append("count", new Document("$size", "$rideRequestSearchPoint")));

		//This will filter those ride requests which doesn't have pickup and drop both available
		Document rideRequestPointCountFilter = new Document("count", new Document("$eq", 2));
		Document matchrideRequestPointCountFilter = new Document("$match",rideRequestPointCountFilter);
		 */

		Document projectResult = new Document("$project", new Document("rideRequestSearchPoint", 1));


		logger.trace(matchGeoWithin.toJson());
		logger.trace(matchDateTimeFilter.toJson());
		logger.trace(groupByRideRequestId.toJson());
		logger.trace(matchrideRequestPointCountFilter.toJson());
		logger.trace(projectResult.toJson());

		List<Document> pipeline = new ArrayList<>();
		pipeline.add(matchGeoWithin);
		pipeline.add(matchDateTimeFilter);
		pipeline.add(groupByRideRequestId);
		pipeline.add(matchrideRequestPointCountFilter);
		pipeline.add(projectResult);

		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		Map<Integer, List<RideRequestPoint>> rideRequestMap = new HashMap<>();

		logger.trace("Ride Id:"+ride.getId());

		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.trace("Document:"+json);
				Integer rideRequestId = document.getInteger("_id");
				//This will get only the ride request point in the original form
				@SuppressWarnings("unchecked")
				List<Document> rideRequestPointsBSON = (List<Document>) document.get("rideRequestSearchPoint");
				JSONUtil<RideRequestPoint> jsonUtilRideRequestPoint = new JSONUtil<>(RideRequestPoint.class);
				List<RideRequestPoint> rideRequestPoints = new LinkedList<>();
				RideRequestPoint rideRequestPoint1 = jsonUtilRideRequestPoint.getModel(rideRequestPointsBSON.get(0).toJson());
				RideRequestPoint rideRequestPoint2 = jsonUtilRideRequestPoint.getModel(rideRequestPointsBSON.get(1).toJson());
				//Checking if point 1 is the pickup point or not. If point 1 is pickup point, then its datetime would be less than point 2
				//We need to add pickup point first and then drop point just to maintain the sequence and consistency
				if (rideRequestPoint1.getDateTime().toEpochSecond() < rideRequestPoint2.getDateTime().toEpochSecond()){
					logger.trace("Point 1 is the pickup and Point 2 is the drop");
					//Point 1 is the pickup and Point 2 is the drop
					rideRequestPoints.add(rideRequestPoint1);
					rideRequestPoints.add(rideRequestPoint2);
				} else {
					logger.trace("Point 2 is the pickup and Point 1 is the drop");
					//Point 2 is the pickup and Point 1 is the drop
					rideRequestPoints.add(rideRequestPoint2);
					rideRequestPoints.add(rideRequestPoint1);
				}
				logger.trace("Adding Ride Request points in the map for ride request Id:"+rideRequestId);
				//Adding ride request points by ride request Id
				rideRequestMap.put(rideRequestId, rideRequestPoints);
			}
		} finally{
			cursor.close();
		}

		logger.debug("Ride Request Ids inside multi polygon of Ride Id["+ride.getId()+"]:"+rideRequestMap.keySet());

		return rideRequestMap;
	}
}








































