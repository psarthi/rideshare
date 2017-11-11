package com.digitusrevolution.rideshare.user;

import com.digitusrevolution.rideshare.common.auth.AuthService;

public class TestPoC {

	public static void main(String args[]) {
		System.out.println(AuthService.getInstance().getVerificationCode());
	}
}
