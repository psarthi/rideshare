package com.digitusrevolution.rideshare.common.mapper.user;

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
	public Collection<RoleEntity> getEntities(Collection<RoleEntity> roleEntities, Collection<Role> roles){
		for (Role role : roles) {
			roleEntities.add(getEntity(role));
		}
		return roleEntities;
	}

	@Override
	public Collection<RoleEntity> getEntitiesWithOnlyPK(Collection<RoleEntity> roleEntities, Collection<Role> roles) {
		for (Role role : roles) {
			roleEntities.add(getEntityWithOnlyPK(role));
		}
		return roleEntities;
	}

	@Override
	public Collection<Role> getDomainModels(Collection<Role> roles, Collection<RoleEntity> roleEntities){
		for (RoleEntity roleEntity : roleEntities) {
			Role role = new Role();
			role = getDomainModel(roleEntity);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public Collection<Role> getDomainModelsWithOnlyPK(Collection<Role> roles, Collection<RoleEntity> roleEntities) {
		for (RoleEntity roleEntity : roleEntities) {
			Role role = new Role();
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
