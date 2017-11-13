package com.digitusrevolution.rideshare.common.util;

import java.io.IOException;

import javax.ws.rs.WebApplicationException;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * This is used internally by the application.
 * Note - I have not disabled/changed the datetime format and default timestamps (numbers) would be stored instead of textual
 * Extracted ObjectMapper creation into seperate file JsonObjectMapper so that we don't have to duplicate and can be used across
 * the application and in particular convertvalue function for mapping one object to another
 */
public class JSONUtil<T> {
	
	private final Class<T> modelClass;

	public JSONUtil(Class<T> modelClass){
		this.modelClass = modelClass;
	}
	
	public String getJson(T model){
		ObjectMapper mapper = JsonObjectMapper.getMapper();
		String json = null;
		try {
			json = mapper.writeValueAsString(model);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to convert to JSON from Model:"+model.toString(),e);
		}
		return json;
	}
	
	public T getModel(String json){
		ObjectMapper mapper = JsonObjectMapper.getMapper();
		T model;
		try {
			model = mapper.readValue(json, modelClass);
		} catch (IOException e) {
			throw new WebApplicationException("Unable to convert to Model from JSON:"+json,e);
		}
		return model;
		
	}

}
