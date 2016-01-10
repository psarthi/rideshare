package com.digitusrevolution.rideshare.ride.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
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


	public void getAllMatchingRideRequestPointNearGivenPoint(List<RidePoint> ridePoints){

		double maxRideRequestDistanceVariation = Double.parseDouble(PropertyReader.getInstance().getProperty("MAX_DISTANCE_VARIATION_FROM_RIDE_REQUEST_POINT"));
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_SEARCH_MIN_DISTANCE"));
		logger.debug("Total RidePoints:"+ridePoints.size());

		Set<RideRequestSearchPoint> rideRequestSearchPointsSet = new HashSet<>();
		Set<Integer> rideRequestIdsSet = new HashSet<>();
		
		logger.debug("Start - Searching Ride Request Points");
		for (RidePoint ridePoint : ridePoints) {
			logger.trace("Ride Point:"+ridePoint.getPoint().toString());	
			Point point = ridePoint.getPoint();
			JSONUtil<Point> jsonUtilPoint = new JSONUtil<>(Point.class);
			String pointJson = jsonUtilPoint.getJson(point);
			logger.trace(pointJson);

			Document geoNear = new Document("$geoNear",new Document("spherical",true)
					.append("limit", PropertyReader.getInstance().getProperty("RIDE_REQUEST_SEARCH_RESULT_LIMIT"))
					.append("maxDistance", maxRideRequestDistanceVariation)
					.append("minDistance", minDistance)
					.append("near", new Document(Document.parse(pointJson)))
					.append("distanceField", "distance"));


			logger.trace(geoNear.toJson());

			List<Document> pipeline = new ArrayList<>();
			pipeline.add(geoNear);
			
			MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();
		
			JSONUtil<RideRequestSearchPoint> jsonUtilRideRequestSearchPoint = new JSONUtil<>(RideRequestSearchPoint.class);
			int count =0;
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
					count++;
				}
			} finally{
				cursor.close();
			}
			logger.trace("Total Count" + count);	
		}
		logger.debug("End - Searching Ride Request Points");
		logger.debug("Matching Unique Ride Request Points Ids:" + rideRequestIdsSet);
	}

}
