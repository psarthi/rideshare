package com.digitusrevolution.rideshare.model.user.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

@Entity
@Table(name="group_detail")
public class GroupEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	@OneToOne
	private PhotoEntity photo;
	@ManyToMany
	@JoinTable(name="group_user")
	private Collection<UserEntity> users = new HashSet<UserEntity>();

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

	public PhotoEntity getPhoto() {
		return photo;
	}

	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}

	public Collection<UserEntity> getUsers() {
		return users;
	}

	public void setUsers(Collection<UserEntity> users) {
		this.users = users;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof GroupEntity)) {
			return false;
		}
		GroupEntity other = (GroupEntity) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}

	
}
