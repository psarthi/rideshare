package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.RoleMapper;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;

public class RoleDO implements DomainObject{

	private Role role;
	private RoleEntity roleEntity;
	private RoleMapper roleMapper;

	public RoleDO() {
		role = new Role();
		roleEntity = new RoleEntity();
		roleMapper = new RoleMapper();
	}

	public Role getRole() {
		return role;
	}


	public void setRole(Role role) {
		this.role = role;
		roleEntity = roleMapper.getEntity(role);
	}

	public RoleEntity getRoleEntity() {
		return roleEntity;
	}


	public void setRoleEntity(RoleEntity roleEntity) {
		this.roleEntity = roleEntity;
		role = roleMapper.getDomainModel(roleEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}
}
