package com.digitusrevolution.rideshare.common.db;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.ietf.jgss.Oid;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtil {
	
	private static MongoDatabase database;
	private static MongoClient mongoClient = null;
	private static final Logger logger = LogManager.getLogger(MongoDBUtil.class.getName());

	static {
        try {
        	//Set below property in JVM startup command
        	//-DMONGODB_CONNECTION_STRING=mongodb://localhost:27017 -DMONGO_DB_NAME=ridesharedb
        	String connectionString = System.getProperty("MONGODB_CONNECTION_STRING");
        	String dbName = System.getProperty("MONGODB_NAME");
        	//String connectionString = "mongodb://admin:<Password>@rideshare-test-shard-00-00-txmgo.mongodb.net:27017,rideshare-test-shard-00-01-txmgo.mongodb.net:27017,rideshare-test-shard-00-02-txmgo.mongodb.net:27017/test?ssl=true&replicaSet=rideshare-test-shard-0&authSource=admin";
        	int minPoolSize = Integer.parseInt(PropertyReader.getInstance().getProperty("MONGODB_MIN_POOL_SIZE"));
        	int maxPoolSize = Integer.parseInt(PropertyReader.getInstance().getProperty("MONGODB_MAX_POOL_SIZE"));
        	int maxConnectionIdleTime = Integer.parseInt(PropertyReader.getInstance().getProperty("MONGODB_MAX_CONNECTION_IDLE_TIME_IN_MILLIS"));
        	MongoClientOptions clientOptions = MongoClientOptions.builder()
        			.minConnectionsPerHost(minPoolSize)
        			.connectionsPerHost(maxPoolSize)
        			.maxConnectionIdleTime(maxConnectionIdleTime)
        			.build();
        	Builder builder = MongoClientOptions.builder(clientOptions);
        	MongoClientURI uri = new MongoClientURI(connectionString, builder);
        	mongoClient = new MongoClient(uri);
        	database = mongoClient.getDatabase(dbName);
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
