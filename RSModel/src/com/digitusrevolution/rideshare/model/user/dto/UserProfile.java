package com.digitusrevolution.rideshare.model.user.dto;

import java.util.List;

public class UserProfile {
	
	private BasicUser user;
	private int ridesTaken;
	private int offeredRides;
	private List<GroupDetail> commonGroups;
	
	public BasicUser getUser() {
		return user;
	}
	public void setUser(BasicUser user) {
		this.user = user;
	}
	public int getRidesTaken() {
		return ridesTaken;
	}
	public void setRidesTaken(int ridesTaken) {
		this.ridesTaken = ridesTaken;
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
	
}
