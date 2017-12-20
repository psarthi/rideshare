package com.digitusrevolution.rideshare.model.user.dto;

import java.util.List;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;

public class UserProfile {
	
	private BasicUser user;
	private int ridesTaken;
	private int offeredRides;
	private List<BasicUser> mutualFriends;
	private List<Group> commonGroups;
	
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
	public List<Group> getCommonGroups() {
		return commonGroups;
	}
	public void setCommonGroups(List<Group> commonGroups) {
		this.commonGroups = commonGroups;
	}
}
