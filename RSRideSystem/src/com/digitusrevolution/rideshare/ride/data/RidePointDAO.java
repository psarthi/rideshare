package com.digitusrevolution.rideshare.ride.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.JSONUtil;
import com.digitusrevolution.rideshare.common.MongoDBUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class RidePointDAO{
	
	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("ride_point");
	private final JSONUtil<RidePoint> jsonUtil = new JSONUtil<>(RidePoint.class);

	public String create(RidePoint ridePoint) {
		ObjectId _id = new ObjectId();
		ridePoint.set_id(_id.toString());
		String json = jsonUtil.getJson(ridePoint);
		collection.insertOne(Document.parse(json));
		return ridePoint.get_id();
	}

	public RidePoint get(String _id) {
		Document document = collection.find(Filters.eq("_id", _id)).first();
		String json = document.toJson();
		RidePoint ridePoint = jsonUtil.getModel(json);
		return ridePoint;
	}

	public void update(RidePoint ridePoint) {
		String json = jsonUtil.getJson(ridePoint);
		String _id = ridePoint.get_id();
		/*
		 * Don't use updateOne as it seems there is some bug in the code
		 * which throws _id field not valid in BSON, so use replaceOne
		 */
		collection.replaceOne(Filters.eq("_id", _id), Document.parse(json));
	}

	public void delete(String _id) {
		collection.deleteOne(Filters.eq("_id", _id));
	}
	
	public List<RidePoint> getAll() {
		List<RidePoint> ridePoints = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}

	public List<RidePoint> getAllRidePointsOfRide(int rideId) {
		List<RidePoint> ridePoints = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find(Filters.eq("rideId", rideId)).iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				RidePoint ridePoint = jsonUtil.getModel(json);
				ridePoints.add(ridePoint);
			}
		} finally{
			cursor.close();
		}
		return ridePoints;
	}
	

}
