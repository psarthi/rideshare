package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.ArrayList;
import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;

public class RoleMapper implements Mapper<Role, RoleEntity>{
	
	@Override
	public Role getDomainModelWithOnlyPK(RoleEntity roleEntity) {
		Role role = new Role();
		role.setName(roleEntity.getName());
		return role;
	}
	
	@Override
	public Role getDomainModel(RoleEntity roleEntity) {
		Role role = new Role();
		role = getDomainModelWithOnlyPK(roleEntity);
		return role;
	}

	@Override
	public RoleEntity getEntityWithOnlyPK(Role role) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity.setName(role.getName());
		return roleEntity;
	}

	@Override
	public RoleEntity getEntity(Role role) {
		RoleEntity roleEntity = new RoleEntity();
		roleEntity = getEntityWithOnlyPK(role);
		return roleEntity;
	}
	
	@Override
	public Collection<RoleEntity> getEntities(Collection<Role> roles){
		Collection<RoleEntity> roleEntities = new ArrayList<>();
		for (Role role : roles) {
			roleEntities.add(getEntity(role));
		}
		return roleEntities;
	}

	@Override
	public Collection<RoleEntity> getEntitiesWithOnlyPK(Collection<Role> roles) {
		Collection<RoleEntity> roleEntities = new ArrayList<>();
		for (Role role : roles) {
			roleEntities.add(getEntityWithOnlyPK(role));
		}
		return roleEntities;
	}

	public Collection<Role> getDomainModels(Collection<RoleEntity> roleEntities){
		Collection<Role> roles = new ArrayList<>();
		Role role = new Role();
		for (RoleEntity roleEntity : roleEntities) {
			role = getDomainModel(roleEntity);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public Collection<Role> getDomainModelsWithOnlyPK(Collection<RoleEntity> roleEntities) {
		Collection<Role> roles = new ArrayList<>();
		Role role = new Role();
		for (RoleEntity roleEntity : roleEntities) {
			role = getDomainModelWithOnlyPK(roleEntity);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public RoleEntity getEntityChild(Role model, RoleEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Role getDomainModelChild(Role model, RoleEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

}
