package com.digitusrevolution.rideshare.common.mapper.user.core;

import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class UserMapper {

	
	public UserEntity getUserEntity(User user){
		UserEntity userEntity = new UserEntity();
		userEntity.setId(user.getId());
		userEntity.setFirstName(user.getFirstName());
		userEntity.setLastName(user.getLastName());
		userEntity.setSex(user.getSex());
		userEntity.setMobileNumber(user.getMobileNumber());
		userEntity.setEmail(user.getEmail());
		userEntity.setPassword(user.getPassword());

		CityMapper cityMapper = new CityMapper();
		City city = user.getCity();
		userEntity.setCity(cityMapper.getCityEntity(city));
		return userEntity;				
	}

	
	public User getUser(UserEntity userEntity){
		User user = new User();
		user.setId(userEntity.getId());
		user.setFirstName(userEntity.getFirstName());
		user.setLastName(userEntity.getLastName());
		user.setSex(userEntity.getSex());
		user.setMobileNumber(userEntity.getMobileNumber());
		user.setEmail(userEntity.getEmail());
		user.setPassword(userEntity.getPassword());

		CityMapper cityMapper = new CityMapper();
		CityEntity cityEntity = userEntity.getCity();
		user.setCity(cityMapper.getCity(cityEntity));
		return user;
	}

}
