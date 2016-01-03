package com.digitusrevolution.rideshare.ride.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RideBasicInfo;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.*;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Position;

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
	
	public List<RidePoint> getAllMatchingRidePointNearGivenPoint(RideRequestPoint rideRequestPoint, double maxDistance, double minDistance){
		logger.debug("Ride Request Point:"+rideRequestPoint.getPoint().toString());
		
		Document query = new Document("rides.dateTime", new Document("$gte", rideRequestPoint.getDateTime().minusMinutes(30).toEpochSecond())
									.append("$lte", rideRequestPoint.getDateTime().plusMinutes(30).toEpochSecond()));
		
		Point point = rideRequestPoint.getPoint();
		JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
		String pointJson = jsonUtilPoint.getJson(point);
		logger.debug(pointJson);
		
		Document geoNear = new Document("$geoNear",new Document("spherical",true)
										.append("limit", 100000)
										.append("maxDistance", maxDistance)
										.append("minDistance", minDistance)
										.append("query", query)
										.append("near", new Document(Document.parse(pointJson)))
										.append("distanceField", "distance"));
		
		Document group = new Document("$group", new Document("_id","$rides.id")
									.append("ridepoint", new Document("$first", "$$CURRENT")));
		
		Document sort = new Document("$sort", new Document("ridepoint.distance", 1));
		
		logger.debug(geoNear.toJson());
		logger.debug(group.toJson());
		logger.debug(sort.toJson());
		
		List<Document> pipeline = new ArrayList<>();
		pipeline.add(geoNear);
		pipeline.add(group);
		pipeline.add(sort);
		
		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		int count =0;
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.debug(json);
				count++;
			}
		} finally{
			cursor.close();
		}
		logger.debug("Total Count" + count);
		
		return null;
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
