package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetwork {
	
	private int id;
	private Collection<TrustCategory> trustCategories = new HashSet<TrustCategory>();
	private Collection<User> friends = new HashSet<User>();
	private Collection<Group> groups = new HashSet<Group>();
	
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
