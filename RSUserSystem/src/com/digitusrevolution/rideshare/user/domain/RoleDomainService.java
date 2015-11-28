package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.model.user.data.RoleEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.user.data.RoleDAO;

public class RoleDomainService {
	
	private static final Logger logger = LogManager.getLogger(RoleDomainService.class.getName());
	
	public String create(Role role){
		logger.entry();
		RoleDO roleDO = new RoleDO();
		RoleDAO roleDAO = new RoleDAO();
		roleDO.setRole(role);
		String id = roleDAO.create(roleDO.getRoleEntity());
		logger.exit();
		return id;
	}

	public Role get(String id){
		RoleDO roleDO = new RoleDO();
		RoleDAO roleDAO = new RoleDAO();
		RoleEntity roleEntity = new RoleEntity();
		roleEntity = roleDAO.get(id);
		if (roleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		roleDO.setRoleEntity(roleEntity);
		return roleDO.getRole();
	}
	
	public Role getChild(String id){
		
	 // Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch 

		RoleDO roleDO = new RoleDO();
		RoleDAO roleDAO = new RoleDAO();
		RoleEntity roleEntity = new RoleEntity();
		roleEntity = roleDAO.get(id);
		if (roleEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		roleDO.setRoleEntity(roleEntity);
		roleDO.mapChildDataModelToDomainModel();
		return roleDO.getRole();
	}
	
	public List<Role> getAll(){
		RoleDAO roleDAO = new RoleDAO();
		List<RoleEntity> roleEntities = new ArrayList<>();
		List<Role> roles = new ArrayList<>();
		roleEntities = roleDAO.getAll();
		for (RoleEntity roleEntity : roleEntities) {
			RoleDO roleDO = new RoleDO();
			roleDO.setRoleEntity(roleEntity);
			roles.add(roleDO.getRole());
		}
		return roles;
	}
	
	public void update(Role role){
		RoleDO roleDO = new RoleDO();
		RoleDAO roleDAO = new RoleDAO();
		roleDO.setRole(role);
		roleDAO.update(roleDO.getRoleEntity());
	}
	
	public void delete(Role role){
		RoleDO roleDO = new RoleDO();
		RoleDAO roleDAO = new RoleDAO();
		roleDO.setRole(role);
		roleDAO.delete(roleDO.getRoleEntity());
	}


}
