package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class RideRequestUnavailableExceptionMapper implements ExceptionMapper<RideRequestUnavailableException>{

	private static final Logger logger = LogManager.getLogger(RideRequestUnavailableExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(RideRequestUnavailableException exception) {
		String errorType = "RIDE_REQUEST_UNAVAILABLE";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, exception.getMessage());
		Response response = Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
