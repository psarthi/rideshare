package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;;

public class UserDO {
	
	private User user;
	private UserEntity userEntity;

	public UserDO(){
		user = new User();
		userEntity = new UserEntity();
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
		mapDomainModelToDataEntity();
	}

	public UserEntity getUserEntity() {
		return userEntity;
	}

	public void setUserEntity(UserEntity userEntity) {
		this.userEntity = userEntity;
		mapDataEntityToDomainModel();
	}

	public void mapDomainModelToDataEntity(){	
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setEmail(user.getEmail());
	}
	
	public void mapDataEntityToDomainModel(){	
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setEmail(userEntity.getEmail());		
	}
	public void addVehicle(Vehicle vehicle){
		VehicleService vehicleService = new VehicleService();
		vehicleService.createVehicle(vehicle);	
	}
}
