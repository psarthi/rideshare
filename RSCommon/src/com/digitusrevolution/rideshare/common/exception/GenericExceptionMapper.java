package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	private static final Logger logger = LogManager.getLogger(GenericExceptionMapper.class.getName());
	
	public GenericExceptionMapper() {
		logger.debug("GenericExceptionMapper Invoked");
	}

	@Override
	public Response toResponse(Throwable exception) {
		
		logger.error("Caught in GenericExceptionMapper");
		ErrorMessage errorMessage = null;
		int errorCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		if (exception.getCause()!=null){
			exception.printStackTrace();
			errorMessage = new ErrorMessage(errorCode, exception.getClass().getSimpleName(), 
											exception.getMessage(), exception.getCause().toString());
		} else {
			exception.printStackTrace();
			errorMessage = new ErrorMessage(errorCode, exception.getClass().getSimpleName(), exception.getMessage());			
		}
		logger.error("Error Msg:"+errorMessage.toString());
		Response response = Response.status(errorCode).entity(errorMessage).build();
		return response;
	}

}
