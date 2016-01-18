package com.digitusrevolution.rideshare.common.exception;

public class RideUnavailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RideUnavailableException(String message) {
		super(message);
	}
}
