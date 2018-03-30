package com.digitusrevolution.rideshare.model.user.domain;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class Interest implements Comparable<Interest>{
	
	private long id;
	private String name;
	private Photo photo;
	private Collection<User> users = new HashSet<User>();
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Photo getPhoto() {
		return photo;
	}
	public void setPhoto(Photo photo) {
		this.photo = photo;
	}
	public Collection<User> getUsers() {
		return users;
	}
	public void setUsers(Collection<User> users) {
		this.users = users;
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
		if (!(obj instanceof Interest)) {
			return false;
		}
		Interest other = (Interest) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(Interest interest) {
		//Negative number is desc order, positive is asc order
		//This will return in assending order
		//Changing it to upper case so that we are comparing the group name properly by ignoring the case
		return this.name.toUpperCase().compareTo(interest.name.toUpperCase());
	}
	
	
}
