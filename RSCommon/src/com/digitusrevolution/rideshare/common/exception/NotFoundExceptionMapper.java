package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException>{

	@Override
	public Response toResponse(NotFoundException exception) {
		ErrorMessage errorMessage = new ErrorMessage(404, "NOT_FOUND_EXCEPTION", exception.getMessage());
		Response response = Response.status(Response.Status.NOT_FOUND).entity(errorMessage).build();
		return response;
	}

}
