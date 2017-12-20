package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideRequestMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.data.UserFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.domain.UserFeedback;

public class UserFeedbackMapper implements Mapper<UserFeedback, UserFeedbackEntity>{

	@Override
	public UserFeedbackEntity getEntity(UserFeedback userFeedback, boolean fetchChild) {
		UserFeedbackEntity userFeedbackEntity = new UserFeedbackEntity();
		userFeedbackEntity.setId(userFeedback.getId());
		userFeedbackEntity.setRating(userFeedback.getRating());
		UserMapper userMapper = new UserMapper();
		userFeedbackEntity.setForUser(userMapper.getEntity(userFeedback.getForUser(), false));
		userFeedbackEntity.setGivenByUser(userMapper.getEntity(userFeedback.getGivenByUser(), false));
		RideMapper rideMapper = new RideMapper();
		userFeedbackEntity.setRide(rideMapper.getEntity(userFeedback.getRide(), false));
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		userFeedbackEntity.setRideRequest(rideRequestMapper.getEntity(userFeedback.getRideRequest(), false));
		return userFeedbackEntity;
	}

	@Override
	public UserFeedbackEntity getEntityChild(UserFeedback userFeedback, UserFeedbackEntity userFeedbackEntity) {
		return userFeedbackEntity;
	}

	@Override
	public UserFeedback getDomainModel(UserFeedbackEntity userFeedbackEntity, boolean fetchChild) {
		UserFeedback userFeedback = new UserFeedback();
		userFeedback.setId(userFeedbackEntity.getId());
		userFeedback.setRating(userFeedbackEntity.getRating());
		UserMapper userMapper = new UserMapper();
		userFeedback.setForUser(userMapper.getDomainModel(userFeedbackEntity.getForUser(), false));
		userFeedback.setGivenByUser(userMapper.getDomainModel(userFeedbackEntity.getGivenByUser(), false));
		RideMapper rideMapper = new RideMapper();
		userFeedback.setRide(rideMapper.getDomainModel(userFeedbackEntity.getRide(), false));
		RideRequestMapper rideRequestMapper = new RideRequestMapper();
		userFeedback.setRideRequest(rideRequestMapper.getDomainModel(userFeedbackEntity.getRideRequest(), false));
		return userFeedback;
	}

	@Override
	public UserFeedback getDomainModelChild(UserFeedback userFeedback, UserFeedbackEntity userFeedbackEntity) {
		return userFeedback;
	}

	@Override
	public Collection<UserFeedback> getDomainModels(Collection<UserFeedback> userFeedbacks,
			Collection<UserFeedbackEntity> userFeedbackEntities, boolean fetchChild) {
		for (UserFeedbackEntity userFeedbackEntity : userFeedbackEntities) {
			UserFeedback userFeedback = new UserFeedback();
			userFeedback = getDomainModel(userFeedbackEntity, fetchChild);
			userFeedbacks.add(userFeedback);
		}
		return userFeedbacks;
	}

	@Override
	public Collection<UserFeedbackEntity> getEntities(Collection<UserFeedbackEntity> userFeedbackEntities,
			Collection<UserFeedback> userFeedbacks, boolean fetchChild) {
		for (UserFeedback userFeedback : userFeedbacks) {
			UserFeedbackEntity userFeedbackEntity = new UserFeedbackEntity();
			userFeedbackEntity = getEntity(userFeedback, fetchChild);
			userFeedbackEntities.add(userFeedbackEntity);
		}
		return userFeedbackEntities;
	}

}























