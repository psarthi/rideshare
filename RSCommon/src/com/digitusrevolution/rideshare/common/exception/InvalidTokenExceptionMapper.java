package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException>{

	@Override
	public Response toResponse(InvalidTokenException exception) {
		String AUTHENTICATION_SCHEME = "Bearer";
		String errorType = "INVALID_TOKEN";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		String errorMsg = PropertyReader.getInstance().getProperty("INVALID_TOKEN_ERROR_MESSAGE");
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, errorMsg);
		Response response = Response.status(Response.Status.UNAUTHORIZED)
				.header(HttpHeaders.WWW_AUTHENTICATE, AUTHENTICATION_SCHEME)
				.entity(errorMessage).build();
		return response;
	}

}
