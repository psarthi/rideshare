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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((friends == null) ? 0 : friends.hashCode());
		result = prime * result + ((groups == null) ? 0 : groups.hashCode());
		result = prime * result + id;
		result = prime * result + ((trustCategories == null) ? 0 : trustCategories.hashCode());
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
		if (friends == null) {
			if (other.friends != null) {
				return false;
			}
		} else if (!friends.equals(other.friends)) {
			return false;
		}
		if (groups == null) {
			if (other.groups != null) {
				return false;
			}
		} else if (!groups.equals(other.groups)) {
			return false;
		}
		if (id != other.id) {
			return false;
		}
		if (trustCategories == null) {
			if (other.trustCategories != null) {
				return false;
			}
		} else if (!trustCategories.equals(other.trustCategories)) {
			return false;
		}
		return true;
	}

}
