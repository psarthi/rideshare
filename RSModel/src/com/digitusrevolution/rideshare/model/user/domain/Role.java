package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.user.data.RoleEntity;

public class Role{
	
	private RoleEntity entity = new RoleEntity();
	private RoleName name;

	public RoleName getName() {
		name = entity.getName();
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
		entity.setName(name);
	}

	public RoleEntity getEntity() {
		return entity;
	}

	public void setEntity(RoleEntity entity) {
		this.entity = entity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		if (!(obj instanceof Role)) {
			return false;
		}
		Role other = (Role) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}


}
