package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class EmailExistExceptionMapper implements ExceptionMapper<EmailExistException>{

	private static final Logger logger = LogManager.getLogger(EmailExistExceptionMapper.class.getName());
	
	public EmailExistExceptionMapper() {
		logger.debug("EmailExistMapper Invoked");
	}
	@Override
	public Response toResponse(EmailExistException exception) {
		logger.error("toResponse invoked");
		String errorType = "EMAIL_ID_ALREADY_EXIST";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, exception.getMessage());
		Response response = Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
