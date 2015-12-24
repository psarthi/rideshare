package com.digitusrevolution.rideshare.model.common;

public class ErrorMessage {

	private int errorCode;
	private String errorType;
	private String errorMessage;
	private String errorCause;

	public ErrorMessage(){

	}
	
	public ErrorMessage(int errorCode, String errorType, String errorMessage, String errorCause) {
		this.errorCode = errorCode;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
		this.errorCause = errorCause;
	}
	
	public ErrorMessage(int errorCode, String errorType, String errorMessage) {
		this.errorCode = errorCode;
		this.errorType = errorType;
		this.errorMessage = errorMessage;
		this.errorCause = "NA";
	}


	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

}
