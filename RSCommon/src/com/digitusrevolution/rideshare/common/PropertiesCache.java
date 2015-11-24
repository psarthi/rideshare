package com.digitusrevolution.rideshare.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertiesCache {

	private final Properties configProp = new Properties();
	private static final Logger logger = LogManager.getLogger(PropertiesCache.class.getName());
	
	   private PropertiesCache()
	   {
	      //Private constructor to restrict new instances
	      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("/resources/config.properties");
	      logger.info("Read all properties from file");
	      try {
	          configProp.load(inputStream);
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	      logger.debug(getAllPropertyNames());
	   }
	 
	   //Bill Pugh Solution for singleton pattern
	   private static class LazyHolder
	   {
	      private static final PropertiesCache INSTANCE = new PropertiesCache();
	   }
	 
	   public static PropertiesCache getInstance()
	   {
	      return LazyHolder.INSTANCE;
	   }
	    
	   public String getProperty(String key){
	      return configProp.getProperty(key);
	   }
	    
	   public Set<String> getAllPropertyNames(){
	      return configProp.stringPropertyNames();
	   }
	    
	   public boolean containsKey(String key){
	      return configProp.containsKey(key);
	   }
}
