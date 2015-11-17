package com.digitusrevolution.rideshare.model.user.domain.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class Group {

	private int id;
	private String name;
	private Photo photo;
	private List<User> users;

	public int getId() {
		return id;
	}

	public void setId(int id) {
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

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	
}
