package com.digitusrevolution.rideshare.user.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.HibernateUtil;
import com.digitusrevolution.rideshare.model.common.ErrorMessage;

@Provider
public class EmailExistExceptionMapper implements ExceptionMapper<EmailExistException>{

	private static final Logger logger = LogManager.getLogger(HibernateUtil.class.getName());
	
	public EmailExistExceptionMapper() {
		logger.debug("EmailExistMapper Invoked");
	}
	@Override
	public Response toResponse(EmailExistException exception) {
		logger.debug("toResponse invoked");
		ErrorMessage errorMessage = new ErrorMessage(1, "EMAIL_ID_ALREADY_EXIST", exception.getMessage());
		Response response = Response.status(Response.Status.CONFLICT).entity(errorMessage).build();
		return response;
	}

}
