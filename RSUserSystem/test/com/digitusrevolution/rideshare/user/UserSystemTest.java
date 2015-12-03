package com.digitusrevolution.rideshare.user;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserSystemTest {

	private static final Logger logger = LogManager.getLogger(UserSystemTest.class.getName());
	
	public static void main(String args[]){
		
		logger.info("Logger Testing");
		
		UserDataLoader dataLoader = new UserDataLoader();
		dataLoader.load();
		
		
	}
}
