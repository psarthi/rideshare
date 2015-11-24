package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException exception) {
				
		ErrorMessage errorMessage = new ErrorMessage(exception.getResponse().getStatus(), exception.getClass().getSimpleName(), exception.getMessage());
		Response response = Response.status(exception.getResponse().getStatus()).entity(errorMessage).build();
		return response;

	}

}
