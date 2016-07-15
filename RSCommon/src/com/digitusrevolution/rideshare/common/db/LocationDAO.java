package com.digitusrevolution.rideshare.common.db;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.common.util.JSONUtil;
import com.digitusrevolution.rideshare.model.common.Location;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

/*
 * 
 * This class is for testing purpose only
 * 
 */
public class LocationDAO{
	
	private MongoDatabase db = MongoDBUtil.getDatabase();
	private MongoCollection<Document> collection = db.getCollection("location");
	private final JSONUtil<Location> jsonUtil = new JSONUtil<>(Location.class);

	public String create(Location location) {
		ObjectId _id = new ObjectId();
		location.set_id(_id.toString());
		String json = jsonUtil.getJson(location);
		collection.insertOne(Document.parse(json));
		return location.get_id();
	}

	public Location get(String _id) {
		Document document = collection.find(Filters.eq("_id", _id)).first();
		String json = document.toJson();
		Location location = jsonUtil.getModel(json);
		return location;
	}

	public void update(Location location) {
		String json = jsonUtil.getJson(location);
		String _id = location.get_id();
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
		List<Location> locations = new ArrayList<>();
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()){
				String json = cursor.next().toJson();
				Location location = jsonUtil.getModel(json);
				locations.add(location);
			}
		} finally{
			cursor.close();
		}
		return locations;
	}
}
