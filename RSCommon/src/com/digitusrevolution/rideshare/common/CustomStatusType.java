package com.digitusrevolution.rideshare.common;

import javax.ws.rs.core.Response.Status.Family;
import javax.ws.rs.core.Response.StatusType;

public class CustomStatusType implements StatusType{

	private final Family family;
	private final int statusCode;
	private final String reasonPhrase;

	public CustomStatusType(final int statusCode, final String reasonPhrase) {
		
		this.family = Family.OTHER;
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
	}

	@Override
	public Family getFamily() { 
		return family;
	}

	@Override
	public String getReasonPhrase() { 
		return reasonPhrase;
	}

	@Override
	public int getStatusCode() {
		return statusCode; 
	}


}
