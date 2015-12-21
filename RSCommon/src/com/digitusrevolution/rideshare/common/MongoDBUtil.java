package com.digitusrevolution.rideshare.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
	
	private static MongoDatabase database;
	private static MongoClient mongoClient = null;
	private static final Logger logger = LogManager.getLogger(MongoDBUtil.class.getName());

	static {
        try {
    		mongoClient = new MongoClient( "localhost" , 27017 ); 
        } catch (Throwable ex) {
            logger.error("Database connection failed: " + ex);
            mongoClient.close();
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static MongoDatabase getDatabase() {
 		database = mongoClient.getDatabase("ridesharedb");
        return database;
    }
}
