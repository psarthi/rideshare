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
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
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
	private UserEntity userEntity;
	private static final Logger logger = LogManager.getLogger(UserDO.class.getName());
	private UserMapper userMapper;
	private final UserDAO userDAO;

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
		userMapper = new UserMapper();
		userDAO = new UserDAO();
	}

	public void setUser(User user) {
		this.user = user;
		userEntity = userMapper.getEntity(user,true);
	}

	private void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		user = userMapper.getDomainModel(userEntity,false);
	}
	
	@Override
	public int create(User user){
		setUser(user);
		int id = userDAO.create(userEntity);
		return id;
	}

	@Override
	public User get(int id){
		userEntity = userDAO.get(id);
		if (userEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setUserEntity(userEntity);
		return user;
	}
	
	@Override
	public User getChild(int id){		
		get(id);
		fetchChild();
		return user;
	}
	
	@Override
	public List<User> getAll(){
		List<User> users = new ArrayList<>();
		List<UserEntity> userEntities = userDAO.getAll();
		for (UserEntity userEntity : userEntities) {
			setUserEntity(userEntity);
			users.add(user);
		}
		return users;
	}
	
	@Override
	public void update(User user){
		if (user.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+user.getId());
		}
		setUser(user);
		userDAO.update(userEntity);
	}
	
	@Override
	public void delete(int id){
		user = get(id);
		setUser(user);
		userDAO.delete(userEntity);
	}
	
	@Override
	public void fetchChild(){
		user = userMapper.getDomainModelChild(user, userEntity);
	}


	
	public boolean isExist(String userEmail){
		if (userDAO.getUserByEmail(userEmail)==null){
			return false;			
		}
		return true;
	}
	
	public Collection<Role> getRoles(int id){
		getChild(id);
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
		User user = getChild(userId);
		user.setAccount(account);
		update(user);		
	}
	
}





































