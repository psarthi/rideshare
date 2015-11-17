package com.digitusrevolution.rideshare.user.data.entity.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class GroupEntity {

	private int id;
	private String name;
	private Photo photo;
	private List<UserEntity> users;

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

	public List<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(List<UserEntity> users) {
		this.users = users;
	}

	
}
