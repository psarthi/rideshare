package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;

public class RoleDO implements DomainObject{

	private Role role;
	private RoleEntity roleEntity;

	public RoleDO() {
		role = new Role();
		roleEntity = new RoleEntity();
	}

	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
		mapDomainModelToDataModel();
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}


	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
		mapDataModelToDomainModel();
	}


	@Override
	public void mapDomainModelToDataModel() {
		roleEntity.setName(role.getName());

	}

	@Override
	public void mapDataModelToDomainModel() {
		role.setName(roleEntity.getName());

	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub

	}
}
