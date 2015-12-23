package com.digitusrevolution.rideshare.common.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.ride.domain.Location;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

public class LocationDAO{
	
	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("location");
	private final JSONUtil<Location> jsonUtil = new JSONUtil<>(Location.class);

	public String create(Location rideRequestPoint) {
		ObjectId _id = new ObjectId();
		rideRequestPoint.set_id(_id.toString());
		String json = jsonUtil.getJson(rideRequestPoint);
		collection.insertOne(Document.parse(json));
		return rideRequestPoint.get_id();
	}

	public Location get(String _id) {
		Document document = collection.find(Filters.eq("_id", _id)).first();
		String json = document.toJson();
		Location rideRequestPoint = jsonUtil.getModel(json);
		return rideRequestPoint;
	}

	public void update(Location rideRequestPoint) {
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
	
	public List<Location> getAll() {
		List<Location> rideRequestPoints = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				Location rideRequestPoint = jsonUtil.getModel(json);
				rideRequestPoints.add(rideRequestPoint);
			}
		} finally{
			cursor.close();
		}
		return rideRequestPoints;
	}
}
