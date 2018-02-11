package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

public class InvalidTokenExceptionMapper implements ExceptionMapper<InvalidTokenException>{
	
	private static final Logger logger = LogManager.getLogger(InvalidTokenExceptionMapper.class.getName());

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
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
