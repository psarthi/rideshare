package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;

public class RoleDO implements DomainObjectPKString<Role>{

	private Role role;
	private final GenericDAO<RoleEntity, RoleName> genericDAO;

	public RoleDO() {
		role = new Role();
		genericDAO = new GenericDAOImpl<>(RoleEntity.class);
	}

	public void setRole(Role role) {
		this.role = role;
	}
	
	public Role getRole() {
		return role;
	}

	@Override
	public String create(Role role) {
		//Converting RoleName Enum to String
		String name = genericDAO.create(role.getEntity()).toString();
		return name;
	}

	@Override
	public Role get(String name) {
		//Converting name to RoleName enum
		RoleEntity roleEntity = genericDAO.get(RoleName.valueOf(name));
		if (roleEntity == null){
			throw new NotFoundException("No Data found with id: "+name);
		}
		role.setEntity(roleEntity);
		return role;
	}

	@Override
	public List<Role> getAll() {
		List<Role> roles = new ArrayList<>();
		List<RoleEntity> roleEntities = genericDAO.getAll();
		for (RoleEntity roleEntity : roleEntities) {
			role.setEntity(roleEntity);
			roles.add(role);
		}
		return roles;
	}

	@Override
	public void update(Role role) {
		if (role.getName().toString().isEmpty()){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+role.getName());
		}
		genericDAO.update(role.getEntity());				
	}

	@Override
	public void delete(String name) {
		role = get(name);
		genericDAO.delete(role.getEntity());				
	}
}
