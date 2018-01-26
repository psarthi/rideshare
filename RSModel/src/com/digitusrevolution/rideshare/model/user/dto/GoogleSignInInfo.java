package com.digitusrevolution.rideshare.model.user.dto;

public class GoogleSignInInfo {

	private String email;
	private String signInToken;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSignInToken() {
		return signInToken;
	}

	public void setSignInToken(String signInToken) {
		this.signInToken = signInToken;
	}
	
	
}
