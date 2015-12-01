package com.digitusrevolution.rideshare.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PropertyReader {

	private static final Properties properties = new Properties();
	private static final PropertyReader PROPERTY_READER = new PropertyReader();
	private static final Logger logger = LogManager.getLogger(PropertyReader.class.getName());
	
	static
	{
		try {
			InputStream inputStream = PropertyReader.class.getClassLoader().getResourceAsStream("resources/config.properties");
			properties.load(inputStream);
			logger.info("Property file loaded");
		} catch (IOException ex) {
			logger.error("Porperty loading failed: " + ex);
			throw new ExceptionInInitializerError(ex);
		}
	}

	public static PropertyReader getInstance() {
		return PROPERTY_READER;
	}

	public String getProperty(String key){
		return properties.getProperty(key);
	}

	public Set<String> getAllPropertyNames(){
		return properties.stringPropertyNames();
	}

	public boolean containsKey(String key){
		return properties.containsKey(key);
	}
}
