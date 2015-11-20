package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.model.user.domain.core.Vehicle;;

public class UserDO {
	
	private User user;
	private UserEntity userEntity;

	public UserDO(){
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
	}

	public void mapDomainModelToDataEntity(){	
		userEntity.setId(user.getId());
		userEntity.setEmail(user.getEmail());
		setUserEntity(userEntity);
	}
	
	public void addVehicle(Vehicle vehicle){
		VehicleService vehicleService = new VehicleService();
		vehicleService.createVehicle(vehicle);	
	}

}
