package com.digitusrevolution.rideshare.common.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class InSufficientBalanceExceptionMapper implements ExceptionMapper<InSufficientBalanceException>{

	@Override
	public Response toResponse(InSufficientBalanceException exception) {
		String errorType = "ACCOUNT_INSUFFICIENT_BALANCE";
		int errorCode = Integer.parseInt(PropertyReader.getInstance().getProperty(errorType));
		ErrorMessage errorMessage = new ErrorMessage(errorCode, errorType, exception.getMessage());
		Response response = Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
		return response;
	}

}
