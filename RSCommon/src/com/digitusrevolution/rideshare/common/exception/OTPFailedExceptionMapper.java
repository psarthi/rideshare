package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class OTPFailedExceptionMapper implements ExceptionMapper<OTPFailedException> {

	private static final Logger logger = LogManager.getLogger(OTPFailedExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(OTPFailedException exception) {
		String errorType = "OTP_FAILED";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, exception.getMessage());
		Response response = Response.status(Response.Status.UNAUTHORIZED).entity(errorMessage).build();
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
