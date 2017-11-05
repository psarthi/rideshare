package com.digitusrevolution.rideshare.common.inf;

public interface AuthService {

	public String getToken(int userId);
	public boolean verifyToken(String token);
	
}
