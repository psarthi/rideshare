package com.digitusrevolution.rideshare.common.exception;

public class SignInFailedException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public SignInFailedException(String message) {
		super(message);
	}
	
}
