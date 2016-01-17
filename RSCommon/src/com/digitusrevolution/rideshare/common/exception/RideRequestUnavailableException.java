package com.digitusrevolution.rideshare.common.exception;

public class RideRequestUnavailableException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public RideRequestUnavailableException(String message) {
		super(message);
	}
}
