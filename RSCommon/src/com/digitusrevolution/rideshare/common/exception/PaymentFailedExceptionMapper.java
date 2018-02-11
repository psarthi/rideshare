package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class PaymentFailedExceptionMapper implements ExceptionMapper<PaymentFailedException>{

	private static final Logger logger = LogManager.getLogger(PaymentFailedExceptionMapper.class.getName());
	
	@Override
	public Response toResponse(PaymentFailedException exception) {
		String errorType = "PAYMENT_FAILED";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, exception.getMessage());
		Response response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorMessage).build();
		logger.error("Error Msg:"+errorMessage.toString());
		return response;
	}

}
