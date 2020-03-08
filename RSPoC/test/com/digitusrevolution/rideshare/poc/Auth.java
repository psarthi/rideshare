package com.digitusrevolution.rideshare.poc;

import com.digitusrevolution.rideshare.common.auth.AuthService;

public class Auth {
	
	public static void main(String[] args) {
		AuthService authService = new AuthService();
		System.out.println("Token is:  "+authService.getToken(-1));
		
	}
	
}
