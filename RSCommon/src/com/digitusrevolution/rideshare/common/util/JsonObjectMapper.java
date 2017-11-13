package com.digitusrevolution.rideshare.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/*
 * This is used internally by the application.
 * Note - I have not disabled/changed the datetime format and default timestamps (numbers) would be stored instead of textual
 */
public class JsonObjectMapper {
	
	private static ObjectMapper mapper;

	public static ObjectMapper getMapper() {
		//This is required to register JSR310 datatype module to support JDK Date and Time API
		mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}	
}
