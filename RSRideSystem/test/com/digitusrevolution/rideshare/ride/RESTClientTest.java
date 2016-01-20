package com.digitusrevolution.rideshare.ride;

import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class RESTClientTest {
	
	public static void main(String[] args) {
		User user = RESTClientUtil.getUser(2);
		System.out.println(user.getFirstName());
		
	}
}
