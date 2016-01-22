package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;

public class RoleMapper implements Mapper<Role, RoleEntity>{

	@Override
	public Role getDomainModel(RoleEntity roleEntity, boolean fetchChild) {
		Role role = new Role();
		role.setName(roleEntity.getName());
		return role;
	}

	@Override
	public RoleEntity getEntity(Role role, boolean fetchChild) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setName(role.getName());
		return roleEntity;
	}
	
	@Override
	public Collection<RoleEntity> getEntities(Collection<RoleEntity> roleEntities, Collection<Role> roles, boolean fetchChild){
		for (Role role : roles) {
			roleEntities.add(getEntity(role, fetchChild));
		}
		return roleEntities;
	}
	
	@Override
	public Collection<Role> getDomainModels(Collection<Role> roles, Collection<RoleEntity> roleEntities, boolean fetchChild){
		for (RoleEntity roleEntity : roleEntities) {
			Role role = new Role();
			role = getDomainModel(roleEntity, fetchChild);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public RoleEntity getEntityChild(Role role, RoleEntity roleEntity) {
		return roleEntity;
	}

	@Override
	public Role getDomainModelChild(Role role, RoleEntity roleEntity) {
		return role;
	}

}
