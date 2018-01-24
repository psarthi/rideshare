package com.digitusrevolution.rideshare.common.inf;

public interface AuthServiceInf {

	public String getToken(long userId);
	public boolean verifyToken(String token);
	
}
