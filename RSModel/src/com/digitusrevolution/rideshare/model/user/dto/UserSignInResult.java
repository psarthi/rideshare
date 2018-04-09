package com.digitusrevolution.rideshare.model.user.dto;

import com.digitusrevolution.rideshare.model.ride.dto.FullRide;
import com.digitusrevolution.rideshare.model.ride.dto.FullRideRequest;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Company;

public class UserSignInResult {

	private String token;
	private BasicUser user;
	private FullRide currentRide;
	private FullRideRequest currentRideRequest;
	private boolean groupMember;
	private Company company;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public BasicUser getUser() {
		return user;
	}
	public void setUser(BasicUser user) {
		this.user = user;
	}
	public FullRide getCurrentRide() {
		return currentRide;
	}
	public void setCurrentRide(FullRide currentRide) {
		this.currentRide = currentRide;
	}
	public FullRideRequest getCurrentRideRequest() {
		return currentRideRequest;
	}
	public void setCurrentRideRequest(FullRideRequest currentRideRequest) {
		this.currentRideRequest = currentRideRequest;
	}
	public boolean isGroupMember() {
		return groupMember;
	}
	public void setGroupMember(boolean groupMember) {
		this.groupMember = groupMember;
	}
	public Company getCompany() {
		return company;
	}
	public void setCompany(Company company) {
		this.company = company;
	}

}
