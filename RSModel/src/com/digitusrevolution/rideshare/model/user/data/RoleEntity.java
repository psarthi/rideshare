package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.domain.RoleName;

@Entity
@Table(name="role")
public class RoleEntity {

	@Id
	@Column
	@Enumerated(EnumType.STRING)
	private RoleName name;

	public RoleName getName() {
		return name;
	}

	public void setName(RoleName name) {
		this.name = name;
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
		if (!(obj instanceof RoleEntity)) {
			return false;
		}
		RoleEntity other = (RoleEntity) obj;
		if (name != other.name) {
			return false;
		}
		return true;
	}
}
