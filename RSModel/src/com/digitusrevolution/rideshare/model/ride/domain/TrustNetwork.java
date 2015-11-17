package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.List;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetwork {
	
	private int id;
	private List<TrustCategory> trustCategories;
	private List<User> friends;
	private List<Group> groups;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public List<TrustCategory> getTrustCategories() {
		return trustCategories;
	}
	public void setTrustCategories(List<TrustCategory> trustCategories) {
		this.trustCategories = trustCategories;
	}
	public List<User> getFriends() {
		return friends;
	}
	public void setFriends(List<User> friends) {
		this.friends = friends;
	}
	public List<Group> getGroups() {
		return groups;
	}
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

}
