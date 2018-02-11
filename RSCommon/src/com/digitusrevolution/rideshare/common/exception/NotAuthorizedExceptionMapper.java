package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class NotAuthorizedExceptionMapper implements ExceptionMapper<NotAuthorizedException>{
	
	private static final Logger logger = LogManager.getLogger(NotAuthorizedExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(NotAuthorizedException exception) {
		String errorType = "UNAUTHORIZED";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		String errorMsg = PropertyReader.getInstance().getProperty("UNAUTHORIZED_ERROR_MESSAGE");
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, errorMsg);
		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
