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
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.digitusrevolution.rideshare.ride.dto.RidePointDTO;
import com.digitusrevolution.rideshare.ride.dto.RideSearchPoint;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.geojson.Geometry;

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
		logger.debug(json);
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
		logger.debug(json);
		RidePoint ridePoint = jsonUtil.getModel(json);
		return ridePoint;
	}

	public RidePoint getRidePointOfRide(String _id, int rideId) {
		Document document = collection.find(eq("_id", _id)).first();
		String json = document.toJson();
		logger.debug(json);
		RidePoint ridePoint = jsonUtil.getModel(json);
		ridePoint = getSpecificRidePoint(ridePoint, rideId);
		return ridePoint;
	}

	private RidePoint getSpecificRidePoint(RidePoint ridePoint, int rideId){
		List<RideBasicInfo> ridesBasicInfo = ridePoint.getRidesBasicInfo();
		Iterator<RideBasicInfo> iterator = ridesBasicInfo.iterator();
		while(iterator.hasNext()){
			int id = iterator.next().getId();
			if (id != rideId){
				logger.debug("Removing Ride Id:" + id);
				iterator.remove();
			} else {
				logger.debug("Matched Ride Id, so not removing:" + id);
			}			
		}
		logger.debug("Final Ride:"+ridePoint.toString());
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

	public void delete(String _id) {
		collection.deleteOne(eq("_id", _id));
	}

	public List<RidePoint> getAll() {
		MongoCursor<Document> cursor = collection.find().iterator();
		return getAllRidePointFromDocuments(cursor);
	}

	public List<RidePoint> getAllRidePointsOfRide(int rideId) {
		MongoCursor<Document> cursor = collection.find(eq("rides.id", rideId)).iterator();
		return getAllSpecificRidePointFromDocuments(cursor, rideId);
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
	private Map<Integer, RidePointDTO> getAllMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint, int rideId){

		logger.debug("Ride Request Point:"+rideRequestPoint.getPoint().toString());	
		long variationInSeconds = DateTimeUtil.getSeconds(rideRequestPoint.getTimeVariation());
		logger.debug("Time Variation in Seconds:" + variationInSeconds);
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_SEARCH_MIN_DISTANCE"));
		Document query;

		//***This is important, as depending on the input of rideId, it will either get all matching rides or specific ride
		if (rideId==-1){
			query = new Document("rides.dateTime", new Document("$gte", rideRequestPoint.getDateTime().minusSeconds(variationInSeconds).toEpochSecond())
					.append("$lte", rideRequestPoint.getDateTime().plusSeconds(variationInSeconds).toEpochSecond()));			
		} else {
			query = new Document("rides.dateTime", new Document("$gte", rideRequestPoint.getDateTime().minusSeconds(variationInSeconds).toEpochSecond())
					.append("$lte", rideRequestPoint.getDateTime().plusSeconds(variationInSeconds).toEpochSecond())).append("rides.id", rideId);			
		}

		Point point = rideRequestPoint.getPoint();
		JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
		String pointJson = jsonUtilPoint.getJson(point);
		logger.debug(pointJson);

		Document geoNear = new Document("$geoNear",new Document("spherical",true)
				.append("limit", PropertyReader.getInstance().getProperty("RIDE_SEARCH_RESULT_LIMIT"))
				.append("maxDistance", rideRequestPoint.getDistanceVariation())
				.append("minDistance", minDistance)
				.append("query", query)
				.append("near", new Document(Document.parse(pointJson)))
				.append("distanceField", "distance"));

		Document group = new Document("$group", new Document("_id","$rides.id")
				.append("rideSearchPoint", new Document("$first", "$$CURRENT")));

		//Document sort = new Document("$sort", new Document("ridepoint.distance", 1));

		Document unwind = new Document("$unwind", "$rides");

		Document match = new Document("$match",query);

		logger.debug(query.toJson());
		logger.debug(geoNear.toJson());
		logger.debug(unwind.toJson());
		logger.debug(match.toJson());
		logger.debug(group.toJson());
		//logger.debug(sort.toJson());

		List<Document> pipeline = new ArrayList<>();
		pipeline.add(geoNear);
		pipeline.add(unwind);
		pipeline.add(match);
		pipeline.add(group);
		//pipeline.add(sort);

		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		Map<Integer, RideSearchPoint> rideSearchPointMap = new HashMap<>();
		JSONUtil<RideSearchPoint> jsonUtilRideSearchPoint = new JSONUtil<>(RideSearchPoint.class);
		int count =0;
		try {
			while (cursor.hasNext()){
				RideSearchPoint rideSearchPoint = new RideSearchPoint();
				Document document = cursor.next();
				Integer matchedRideId = document.getInteger("_id");
				Document searchPoint = (Document) document.get("rideSearchPoint");
				String json = searchPoint.toJson();
				logger.debug("rideId:" + matchedRideId);
				logger.debug("searchPoint:" + searchPoint.toJson());
				rideSearchPoint = jsonUtilRideSearchPoint.getModel(json);
				rideSearchPointMap.put(matchedRideId, rideSearchPoint); 
				count++;
			}
		} finally{
			cursor.close();
		}
		logger.debug("Total Count" + count);		
		return getRidePointDTOMap(rideSearchPointMap);
	}

	public Map<Integer, RidePointDTO> getAllMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint){
		return getAllMatchingRidePointNearGivenPoint(rideRequestPoint,-1);
	}
	
	public RidePointDTO getMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint, int rideId){
		Map<Integer, RidePointDTO> ridePointDTOMap = getAllMatchingRidePointNearGivenPoint(rideRequestPoint,rideId);	
		return ridePointDTOMap.get(rideId);
	}

	
	private Map<Integer, RidePointDTO> getRidePointDTOMap(Map<Integer, RideSearchPoint> rideSearchPointMap){
		Map<Integer, RidePointDTO> ridePointDTOMap = new HashMap<>();

		for (Map.Entry<Integer, RideSearchPoint> entry: rideSearchPointMap.entrySet() ) {
			RideSearchPoint rideSearchPoint = entry.getValue();
			Integer rideId = entry.getKey();
			RidePoint ridePoint = getRidePoint(rideSearchPoint);
			RidePointDTO ridePointDTO = new RidePointDTO();
			ridePointDTO.setRidePoint(ridePoint);
			ridePointDTO.setDistance(rideSearchPoint.getDistance());
			ridePointDTOMap.put(rideId, ridePointDTO);
			logger.debug(ridePointDTO.toString());
		}	
		return ridePointDTOMap;
	}

	private RidePoint getRidePoint(RideSearchPoint rideSearchPoint) {
		RidePoint ridePoint = new RidePoint();
		ridePoint.set_id(rideSearchPoint.get_id());
		ridePoint.setPoint(rideSearchPoint.getPoint());
		RideBasicInfo ridesBasicInfo = rideSearchPoint.getRideBasicInfo();
		ridePoint.getRidesBasicInfo().add(ridesBasicInfo);
		ridePoint.setSequence(rideSearchPoint.getSequence());
		return ridePoint;
	}

	private List<RidePoint> getAllRidePointFromDocuments(MongoCursor<Document> cursor){
		List<RidePoint> ridePoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.debug(json);
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}

	private List<RidePoint> getAllSpecificRidePointFromDocuments(MongoCursor<Document> cursor, int rideId){
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
