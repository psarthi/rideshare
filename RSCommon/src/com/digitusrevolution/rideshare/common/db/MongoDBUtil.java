package com.digitusrevolution.rideshare.common.db;

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
    		mongoClient = new MongoClient("localhost",27017);
    		database = mongoClient.getDatabase("ridesharedb");
        } catch (Throwable ex) {
            logger.error("Database connection failed: " + ex);
            if (mongoClient!=null) mongoClient.close();
            throw new ExceptionInInitializerError(ex);
        }
    }
  
    public static MongoDatabase getDatabase() {
        return database;
    }
    
    public static void closeConnection(){
    	mongoClient.close();
    }
}
