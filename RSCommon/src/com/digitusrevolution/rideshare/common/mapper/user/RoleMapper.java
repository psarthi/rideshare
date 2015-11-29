package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;

public class RoleMapper {
	
	public Role getRole(RoleEntity roleEntity) {
		Role role = new Role();
		role.setName(roleEntity.getName());
		return role;
	}

	public RoleEntity getRoleEntity(Role role) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setName(role.getName());
		return roleEntity;
	}
	
	public Collection<RoleEntity> getRoleEntities(Collection<Role> roles){

		Collection<RoleEntity> roleEntities = new ArrayList<>();
		for (Role role : roles) {
			roleEntities.add(getRoleEntity(role));
		}
		return roleEntities;
	}

	public Collection<Role> getRoles(Collection<RoleEntity> roleEntities){

		Collection<Role> roles = new ArrayList<>();
		for (RoleEntity roleEntity : roleEntities) {
			roles.add(getRole(roleEntity));
		}
		return roles;
	}


}
