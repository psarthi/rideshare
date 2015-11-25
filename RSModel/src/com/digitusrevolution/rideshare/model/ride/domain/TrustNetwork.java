package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetwork {
	
	private int id;
	private Collection<TrustCategory> trustCategories = new ArrayList<TrustCategory>();
	private Collection<User> friends = new ArrayList<User>();
	private Collection<Group> groups = new ArrayList<Group>();
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Collection<TrustCategory> getTrustCategories() {
		return trustCategories;
	}
	public void setTrustCategories(Collection<TrustCategory> trustCategories) {
		this.trustCategories = trustCategories;
	}
	public Collection<User> getFriends() {
		return friends;
	}
	public void setFriends(Collection<User> friends) {
		this.friends = friends;
	}
	public Collection<Group> getGroups() {
		return groups;
	}
	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}

}
