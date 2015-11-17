package com.digitusrevolution.rideshare.user.data.inf;

import com.digitusrevolution.rideshare.user.data.entity.core.UserEntity;

public interface UserDAO {

	void createUser(UserEntity userEntity);

	UserEntity getUserById(int userId);

	UserEntity getUserByEmail(String email);

	void updateUser(UserEntity userEntity);

	void deleteUser(int userId);

}