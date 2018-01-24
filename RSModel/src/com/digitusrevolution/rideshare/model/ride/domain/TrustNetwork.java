package com.digitusrevolution.rideshare.model.ride.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.core.Group;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetwork {
	
	private long id;
	private Collection<TrustCategory> trustCategories = new HashSet<TrustCategory>();
	private Collection<User> friends = new HashSet<User>();
	private Collection<Group> groups = new HashSet<Group>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TrustNetwork)) {
			return false;
		}
		TrustNetwork other = (TrustNetwork) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
