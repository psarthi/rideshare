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
		return getAllRidePointFromBSONDocuments(cursor);
	}

	public List<RidePoint> getAllRidePointsOfRide(int rideId) {
		MongoCursor<Document> cursor = collection.find(eq("rides.id", rideId)).iterator();
		return getAllSpecificRidePointFromBSONDocuments(cursor, rideId);
	}
	
	public List<RidePoint> getAllRidePointWithinGivenGeometry(Geometry geometry){
		MongoCursor<Document> cursor = collection.find(geoWithin("point", geometry)).iterator();
		return getAllRidePointFromBSONDocuments(cursor);
	}
	
	public List<RidePoint> getAllRidePointNearGivenPoint(Point point, double maxDistance, double minDistance){
		logger.debug("Given Point:"+point.toString());
		Position coordinate = new Position(point.getCoordinates());
		com.mongodb.client.model.geojson.Point givenPoint = new com.mongodb.client.model.geojson.Point(coordinate);
		MongoCursor<Document> cursor = collection.find(nearSphere("point", givenPoint, maxDistance, minDistance)).iterator();
		return getAllRidePointFromBSONDocuments(cursor);
	}
	
	private List<RidePoint> getAllRidePointFromBSONDocuments(MongoCursor<Document> cursor){
		List<RidePoint> ridePoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				logger.trace(json);
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}
	
	private List<RidePoint> getAllSpecificRidePointFromBSONDocuments(MongoCursor<Document> cursor, int rideId){
		List<RidePoint> ridePoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
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
