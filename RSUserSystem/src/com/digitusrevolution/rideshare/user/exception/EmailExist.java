package com.digitusrevolution.rideshare.user.exception;

public class EmailExist extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public EmailExist(String message) {
		super(message);
	}

}
