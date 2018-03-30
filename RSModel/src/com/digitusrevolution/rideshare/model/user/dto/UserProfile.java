package com.digitusrevolution.rideshare.model.user.dto;

import java.util.List;

public class UserProfile {
	
	private BasicUser user;
	private int requestedRides;
	private int offeredRides;
	private List<GroupDetail> commonGroups;
	private List<BasicInterest> commonInterests;
	
	public BasicUser getUser() {
		return user;
	}
	public void setUser(BasicUser user) {
		this.user = user;
	}
	public int getRequestedRides() {
		return requestedRides;
	}
	public void setRequestedRides(int requestedRides) {
		this.requestedRides = requestedRides;
	}
	public int getOfferedRides() {
		return offeredRides;
	}
	public void setOfferedRides(int offeredRides) {
		this.offeredRides = offeredRides;
	}
	public List<GroupDetail> getCommonGroups() {
		return commonGroups;
	}
	public void setCommonGroups(List<GroupDetail> commonGroups) {
		this.commonGroups = commonGroups;
	}
	public List<BasicInterest> getCommonInterests() {
		return commonInterests;
	}
	public void setCommonInterests(List<BasicInterest> commonInterests) {
		this.commonInterests = commonInterests;
	}
	
}
