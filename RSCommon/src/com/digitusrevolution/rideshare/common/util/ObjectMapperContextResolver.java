package com.digitusrevolution.rideshare.common.util;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.message.internal.FormProvider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 *
 * This is used by Jersey and no additional configuration required to detect this class
 * But for Jersey client, we need to register this class explicitly, else it will not know about any of the configuration defined here
 * 
 * So in nutshell, if you want to use this ObjectMapper anywhere then you need to register this, its discovered by jersey internally but for 
 * Jersey client you need to register it. In JSONUtil class, we are not using this ObjectMapper as we don't want dates to be represented as ISO format,
 * So in JSONUtil we are registering JSR310 module manually
 * 
 * Sample Code of Jersey Client from RESTClientImpl -
 * 
 * 	private final Client client = ClientBuilder.newClient(new ClientConfig().register(LoggingFilter.class)).register(JacksonJsonProvider.class)
 *		.register(ObjectMapperContextResolver.class);
 * 
 * 
 */
@Provider
public class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {  
	private final ObjectMapper MAPPER;

	public ObjectMapperContextResolver() {
		MAPPER = new ObjectMapper();
		//This would add JSR310 (Datetime) support while converting date to JSON using JAXRS service
		MAPPER.registerModule(new JavaTimeModule());
		//Below line would disable use of timestamps (numbers), 
		//and instead use a [ISO-8601 ]-compliant notation, which gets output as something like: "1970-01-01T00:00:00.000+0000".
		MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return MAPPER;
	}  
}
