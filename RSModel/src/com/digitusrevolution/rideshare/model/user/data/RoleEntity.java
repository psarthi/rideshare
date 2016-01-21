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
}
