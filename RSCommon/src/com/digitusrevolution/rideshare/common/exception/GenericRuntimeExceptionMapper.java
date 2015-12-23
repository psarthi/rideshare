package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class GenericRuntimeExceptionMapper implements ExceptionMapper<RuntimeException> {

	@Override
	public Response toResponse(RuntimeException exception) {
				
		ErrorMessage errorMessage = new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), exception.getClass().getSimpleName(), exception.getCause().toString());
		Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		return response;

	}

}
