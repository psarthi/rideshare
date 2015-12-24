package com.digitusrevolution.rideshare.common.util;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * This is used by Jersey for JSON conversion for Web services
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
