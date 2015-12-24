package com.digitusrevolution.rideshare.common.util;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JSONUtil<T> {
	
	private final Class<T> modelClass;

	public JSONUtil(Class<T> modelClass){
		this.modelClass = modelClass;
	}
	
	public String getJson(T model){
		ObjectMapper mapper = new ObjectMapper();
		//This is required to register JSR310 datatype module to support JDK Date and Time API
		mapper.registerModule(new JavaTimeModule());
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		String json = null;
		try {
			json = mapper.writeValueAsString(model);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to convert to JSON from Model",e);
		}
		return json;
	}
	
	public T getModel(String json){
		ObjectMapper mapper = new ObjectMapper();
		//This is required to register JSR310 datatype module to support JDK Date and Time API
		mapper.registerModule(new JavaTimeModule());
		T model;
		try {
			model = mapper.readValue(json, modelClass);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to convert to Model from JSON",e);
		}
		return model;
		
	}

}
