package com.digitusrevolution.rideshare.user.data.inf;

import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;

public interface IUserDAO {

	void createUser(UserEntity userEntity);

	UserEntity getUserById(int userId);

	UserEntity getUserByEmail(String email);

	void updateUser(UserEntity userEntity);

	void deleteUser(int userId);

}