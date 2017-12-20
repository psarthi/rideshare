package com.digitusrevolution.rideshare.model.common;

public class ResponseMessage {
 
	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	public enum Code{
		OK, FAILED
	}

}
