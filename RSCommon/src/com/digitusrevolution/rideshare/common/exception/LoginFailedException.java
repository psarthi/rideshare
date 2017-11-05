package com.digitusrevolution.rideshare.common.exception;

public class LoginFailedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public LoginFailedException(String message) {
		super(message);
	}
	
}
