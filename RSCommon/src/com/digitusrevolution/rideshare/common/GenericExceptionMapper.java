package com.digitusrevolution.rideshare.common;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<WebApplicationException> {

	@Override
	public Response toResponse(WebApplicationException exception) {
		ErrorMessage errorMessage = new ErrorMessage(500, "INTERNAL_SERVER_ERROR", exception.getMessage());
		Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		return response;

	}

}
