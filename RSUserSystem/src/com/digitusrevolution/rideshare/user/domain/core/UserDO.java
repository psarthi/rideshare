package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.exception.EmailExistException;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.RoleName;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;

public class UserDO implements DomainObjectPKInteger<User>{

	private User user;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());
	private final UserDAO userDAO;

	public UserDO(){
		user = new User();
		userDAO = new UserDAO();
	}

	public void setUser(User user) {
		this.user = user;
	}

	public User getUser() {
		return user;
	}

	@Override
	public int create(User user){
		int id = userDAO.create(user.getEntity());
		return id;
	}

	@Override
	public User get(int id){
		UserEntity userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		user.setEntity(userEntity);
		return user;
	}
		
	@Override
	public List<User> getAll(){
		List<User> users = new ArrayList<>();
		List<UserEntity> userEntities = userDAO.getAll();
		for (UserEntity userEntity : userEntities) {
			user.setEntity(userEntity);
			users.add(user);
		}
		return users;
	}
	
	@Override
	public void update(User user){
		if (user.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+user.getId());
		}
		userDAO.update(user.getEntity());
	}
	
	@Override
	public void delete(int id){
		user = get(id);
		userDAO.delete(user.getEntity());
	}
	
	public boolean isExist(String userEmail){
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}
	
	public Collection<Role> getRoles(int id){
		user = get(id);
		logger.debug("Role size: "+user.getRoles().size());
		return user.getRoles();
	}
	
	public void addVehicle(Vehicle vehicle){		
		if (user.getVehicles().size()==0){
			RoleDO roleDO = new RoleDO();
			Role role = roleDO.get(RoleName.Driver.toString());
			user.getRoles().add(role);
		}
		user.getVehicles().add(vehicle);
		update(user);
	}
	
	public int registerUser(User user){
		int id = 0;
		boolean status = isExist(user.getEmail());
		if (status){
			throw new EmailExistException("Email id already exist :"+user.getEmail());					
		} else {
			id = create(user);
		}
		return id;
	}
	
	/*
	 * Purpose - Add account to the user
	 * 
	 */
	public void addAccount(int userId, Account account){
		//Always use getChild instead of get whenever you are trying to update, so that you don't miss any fields where relationship is owned by this entity
		//Otherwise while updating, that field relationship would be deleted
		user = get(userId);
		user.getAccounts().add(account);
		update(user);		
	}
	
}





































