package com.digitusrevolution.rideshare.common.exception;

public class InSufficientBalanceException extends RuntimeException{

	private static final long serialVersionUID = 1L;

	public InSufficientBalanceException(String message) {
		super(message);
	}
}
