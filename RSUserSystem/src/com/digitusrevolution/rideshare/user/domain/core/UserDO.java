package com.digitusrevolution.rideshare.user.domain.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;
import com.digitusrevolution.rideshare.user.data.UserDAO;
import com.digitusrevolution.rideshare.user.domain.RoleDO;
import com.digitusrevolution.rideshare.user.exception.EmailExistException;

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		userEntity = userMapper.getEntity(user);
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		user = userMapper.getDomainModel(userEntity);
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
		setUser(user);
		userDAO.update(userEntity);
	}
	
	@Override
	public void delete(User user){
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
			Role role = roleDO.get("Driver");
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
	
}
