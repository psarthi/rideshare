package com.digitusrevolution.rideshare.common.inf;

public interface AuthServiceInf {

	public String getToken(int userId);
	public boolean verifyToken(String token);
	
}
