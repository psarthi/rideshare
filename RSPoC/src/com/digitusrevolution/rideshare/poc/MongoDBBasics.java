package com.digitusrevolution.rideshare.poc;

import java.io.IOException;
import java.text.Format;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.WebApplicationException;

import org.bson.BsonDocumentReader;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.digitusrevolution.rideshare.model.ride.domain.RidePoint;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.Block;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Indexes;
import com.mongodb.client.model.geojson.CoordinateReferenceSystem;
import com.mongodb.client.model.geojson.GeoJsonObjectType;
import com.mongodb.client.model.geojson.Geometry;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Polygon;
import com.mongodb.client.model.geojson.Position;
import com.mongodb.util.JSON;

import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Sorts.*;

public class MongoDBBasics {

	public static void main(String[] args) {

		MongoClient mongoClient = new MongoClient( "localhost" , 27017 );
		MongoDatabase database = mongoClient.getDatabase("mydb");
		MongoCollection<Document> collection = database.getCollection("test");
		MongoCollection<Document> collectionPoint = database.getCollection("Point");

		ObjectId _id = new ObjectId();
		
		Document document = new Document("name","MongoDB")
				.append("type","database")
				.append("count", 1)
				.append("info", new Document("x", 100).append("y", 102))
				.append("_id", _id);
		
		System.out.println(document);
		
		// collection.insertOne(document);

		document = collection.find().first();
		System.out.println(document.toJson());
		System.out.println(collection.count());

		List<Document> documents = new ArrayList<Document>();
		for (int i = 0; i < 100; i++) {
			documents.add(new Document("i", i));
		}
		//		collection.insertMany(documents);

		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()){
				System.out.println(cursor.next().toJson());
			}
		} finally{
			cursor.close();
		}


		document = collection.find(eq("i",7)).first();
		System.out.println(document.toJson());

		Block<Document> block = new Block<Document>() {

			@Override
			public void apply(Document document) {
				System.out.println(document.toJson());

			}
		};

		collection.find(gt("i", 50)).forEach(block);
		
		document = collection.find(gt("i", 75)).sort(ascending("i")).first();
		System.out.println(document.toJson());
		
		collection.createIndex(Indexes.ascending("i"));
		
		for (final Document index : collection.listIndexes()) {
		    System.out.println(index.toJson());
		}
		
		//Point Insertion
		
		
		RidePoint ridePoint = new RidePoint();
		ridePoint.getPoint().setLatitude(12.12);
		ridePoint.getPoint().setLongitude(13.12);
		ridePoint.setSequence(1);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(ridePoint);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("RidePoint" + json);
		
		Document documentRidePoint = new Document();
		documentRidePoint.append("point", JSON.parse(json));
		System.out.println("-----------------");
		collectionPoint.insertOne(documentRidePoint);
		
	
		
		Position position = new Position(12.1212, 23.232);
		Point point = new Point(position);
		
		System.out.println(point.toJson());
		collectionPoint.insertOne(Document.parse(point.toJson()));
		
		cursor = collectionPoint.find().iterator();
		try {
			while (cursor.hasNext()){
				System.out.println(cursor.next().toJson());
			}
		} finally{
			cursor.close();
		}
		
		System.out.println("Restaurant Example-----------------");
		
		database = mongoClient.getDatabase("test");
		MongoCollection<Document> collectionRestaurant = database.getCollection("restaurants");
		MongoCollection<Document> collectionNeighborhoods = database.getCollection("neighborhoods");

		for (final Document index : collectionRestaurant.listIndexes()) {
		    System.out.println(index.toJson());
		}

		for (final Document index : collectionNeighborhoods.listIndexes()) {
		    System.out.println(index.toJson());
		}
		
		document = collectionRestaurant.find().first();
		System.out.println(document.toJson());
		document = collectionNeighborhoods.find().first();
		System.out.println(document.toJson());
		
		document = collectionNeighborhoods.find(Filters.geoIntersects("geometry", new Point(new Position(-73.93414657, 40.82302903)))).first();
		System.out.println(document.toJson());
		
		System.out.println(document.get("geometry").toString());
		System.out.println(document.get("geometry").getClass().getName());

		Document geometry = (Document) document.get("geometry");
		
		cursor = collectionRestaurant.find(Filters.geoWithin("location", geometry)).iterator();
//		try {
//			while (cursor.hasNext()){
//				System.out.println(cursor.next().toJson());
//			}
//		} finally{
//			cursor.close();
//		}

		//geoWithinCenterSphere will get unsorted list
//		cursor = collectionRestaurant.find(Filters.geoWithinCenterSphere("location", -73.93414657, 40.82302903, 5 / 3963.2)).iterator();

		//nearSphere will get sorted list
		cursor = collectionRestaurant.find(Filters.nearSphere("location", new Point(new Position(-73.93414657, 40.82302903)), 8046.5,0.0)).iterator();
		System.out.println("----");
		int i=0;
		try {
			while (cursor.hasNext()){
				System.out.println(cursor.next().toJson());
				i++;
			}
		} finally{
			System.out.println("Count" + i);
			cursor.close();
		}
		
		mongoClient.close();

	}
}
