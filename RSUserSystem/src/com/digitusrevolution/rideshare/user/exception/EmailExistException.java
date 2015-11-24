package com.digitusrevolution.rideshare.user.exception;

public class EmailExistException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailExistException(String message) {
		super(message);
	}

}
