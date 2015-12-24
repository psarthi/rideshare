package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

	@Override
	public Response toResponse(Throwable exception) {
		
		ErrorMessage errorMessage = null;
		int errorCode = Status.INTERNAL_SERVER_ERROR.getStatusCode();
		if (exception.getCause()!=null){
			errorMessage = new ErrorMessage(errorCode, exception.getClass().getSimpleName(), 
											exception.getMessage(), exception.getCause().toString());
		} else {
			errorMessage = new ErrorMessage(errorCode, exception.getClass().getSimpleName(), exception.getMessage());			
		}
		Response response = Response.status(errorCode).entity(errorMessage).build();
		return response;
	}

}
