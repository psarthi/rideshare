package com.digitusrevolution.rideshare.model.user.dto;

import java.util.List;

public class UserProfile {
	
	private BasicUser user;
	private int ridesTaken;
	private int offeredRides;
	private List<BasicUser> mutualFriends;
	private List<BasicGroup> commonGroups;
	
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
	public List<BasicUser> getMutualFriends() {
		return mutualFriends;
	}
	public void setMutualFriends(List<BasicUser> mutualFriends) {
		this.mutualFriends = mutualFriends;
	}
	public List<BasicGroup> getCommonGroups() {
		return commonGroups;
	}
	public void setCommonGroups(List<BasicGroup> commonGroups) {
		this.commonGroups = commonGroups;
	}
}
