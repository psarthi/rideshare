package com.digitusrevolution.rideshare.model.user.data;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

@Entity
@Table(name="interest")
public class InterestEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String name;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;
	@ManyToMany(mappedBy="interests")
	private Collection<UserEntity> users = new HashSet<UserEntity>();
	
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
		if (!(obj instanceof InterestEntity)) {
			return false;
		}
		InterestEntity other = (InterestEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	
}
