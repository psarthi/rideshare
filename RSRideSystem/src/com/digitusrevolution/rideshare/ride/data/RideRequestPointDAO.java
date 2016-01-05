package com.digitusrevolution.rideshare.ride.data;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.db.MongoDBUtil;
import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.RideRequestPoint;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class RideRequestPointDAO{
	
	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("rideRequest_point");
	private final JSONUtil<RideRequestPoint> jsonUtil = new JSONUtil<>(RideRequestPoint.class);

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
	
	public List<RideRequestPoint> getRideRequestPointsForRideRequest(int rideRequestId) {
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

}
