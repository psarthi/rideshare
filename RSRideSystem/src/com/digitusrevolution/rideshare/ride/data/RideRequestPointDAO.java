package com.digitusrevolution.rideshare.ride.data;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.geojson.MultiPolygon;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.common.util.MathUtil;
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
	 * Purpose - Get all ride request point by going through each ride points and find the ride request point around it
	 */
	public void getAllMatchingRideRequestPointNearGivenPoint(List<RidePoint> ridePoints){

		double maxRideRequestDistanceVariation = Double.parseDouble(PropertyReader.getInstance().getProperty("MAX_DISTANCE_VARIATION_FROM_RIDE_REQUEST_POINT"));
		double minDistance = Double.parseDouble(PropertyReader.getInstance().getProperty("RIDE_REQUEST_SEARCH_MIN_DISTANCE"));
		logger.debug("Total RidePoints:"+ridePoints.size());

		Set<RideRequestSearchPoint> rideRequestSearchPointsSet = new HashSet<>();
		Set<Integer> rideRequestIdsSet = new HashSet<>();

		logger.debug("Start - Searching Ride Request Points");
		for (RidePoint ridePoint : ridePoints) {
			logger.debug("Ride Point:"+ridePoint.getPoint().toString());	
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

	public List<RideRequestPoint> getAllMatchingRideRequestWithinMultiPolygonOfRide(Ride ride, MultiPolygon multiPolygon){

		JSONUtil<MultiPolygon> jsonUtilMultiPolygon = new JSONUtil<>(MultiPolygon.class);
		String jsonMultipPolygon = jsonUtilMultiPolygon.getJson(multiPolygon);
		long maxTimeVariation = Long.parseLong(PropertyReader.getInstance().getProperty("MAX_PICKUP_TIME_VARIATION"));
		long dropTimeBuffer = Long.parseLong(PropertyReader.getInstance().getProperty("DROP_TIME_BUFFER"));
		ZonedDateTime endTime = ride.getEndPoint().getRidesBasicInfo().get(0).getDateTime();

		Document geoWithinQuery = new Document("point", new Document("$geoWithin", new Document("$geometry", new Document(Document.parse(jsonMultipPolygon)))));
		Document dateTimeFilter = new Document("dateTime", new Document("$gte", ride.getStartTime().minusSeconds(maxTimeVariation).toEpochSecond())
											  .append("$lte", endTime.plusSeconds(maxTimeVariation).plusSeconds(dropTimeBuffer).toEpochSecond()));

		Document matchGeoWithin = new Document("$match",geoWithinQuery);
		Document matchDateTimeFilter = new Document("$match",dateTimeFilter);
		
		Document group = new Document("$group", new Document("_id","$rideRequestId")
									 .append("rideRequestSearchPoint", new Document("$push", "$$CURRENT")));

		Document project = new Document("$project", new Document("rideRequestSearchPoint", 1)
								.append("count", new Document("$size", "$rideRequestSearchPoint")));
		
		Document rideRequestPointCountFilter = new Document("count", new Document("$eq", 2));
		Document matchrideRequestPointCountFilter = new Document("$match",rideRequestPointCountFilter);
		
		logger.trace(matchGeoWithin.toJson());
		logger.trace(matchDateTimeFilter.toJson());
		logger.trace(group.toJson());
		logger.trace(project.toJson());
		logger.trace(matchrideRequestPointCountFilter.toJson());

		List<Document> pipeline = new ArrayList<>();
		pipeline.add(matchGeoWithin);
		pipeline.add(matchDateTimeFilter);
		pipeline.add(group);
		pipeline.add(project);
		pipeline.add(matchrideRequestPointCountFilter);

		MongoCursor<Document> cursor = collection.aggregate(pipeline).iterator();

		List<RideRequestPoint> rideRequestPoints = getAllRideRequestPointFromDocuments(cursor);
		logger.debug("Total Ride Request Point Found:"+rideRequestPoints.size());
		List<Integer> rideIds = new ArrayList<>();
		for (RideRequestPoint rideRequestPoint : rideRequestPoints) {
			rideIds.add(rideRequestPoint.getRideRequestId());
		}		
		logger.debug("Ride Request Ids are:"+rideIds);
		return rideRequestPoints;
	}

	private List<RideRequestPoint> getAllRideRequestPointFromDocuments(MongoCursor<Document> cursor){
		List<RideRequestPoint> rideRequestPoints = new ArrayList<>();
		try {
			while (cursor.hasNext()){
				Document document = cursor.next();
				String json = document.toJson();
				logger.trace("Document:"+json);
//				RideRequestPoint rideRequestPoint = jsonUtil.getModel(json);				
//				rideRequestPoints.add(rideRequestPoint);
			}
		} finally{
			cursor.close();
		}
		return rideRequestPoints;
	}

}








































