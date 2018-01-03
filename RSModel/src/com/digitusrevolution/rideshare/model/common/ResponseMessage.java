package com.digitusrevolution.rideshare.model.common;

public class ResponseMessage {
 
	private String result;
	private Code status;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public Code getStatus() {
		return status;
	}

	public void setStatus(Code status) {
		this.status = status;
	}

	public enum Code{
		OK, FAILED
	}
}
