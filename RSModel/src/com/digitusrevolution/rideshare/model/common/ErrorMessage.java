package com.digitusrevolution.rideshare.model.common;

public class ErrorMessage {

	private int errorCode;
	private String errorReason;
	private String errorDetail;

	public ErrorMessage(){

	}
	
	public ErrorMessage(int errorCode, String errorReason, String errorDetail) {
		this.errorCode = errorCode;
		this.errorReason = errorReason;
		this.setErrorDetail(errorDetail);
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorReason() {
		return errorReason;
	}

	public void setErrorReason(String errorReason) {
		this.errorReason = errorReason;
	}

	public String getErrorDetail() {
		return errorDetail;
	}

	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}


}
