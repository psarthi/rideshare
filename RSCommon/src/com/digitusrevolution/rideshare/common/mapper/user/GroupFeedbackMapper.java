package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.user.data.GroupFeedbackEntity;
import com.digitusrevolution.rideshare.model.user.domain.GroupFeedback;

public class GroupFeedbackMapper implements Mapper<GroupFeedback, GroupFeedbackEntity>{

	@Override
	public GroupFeedbackEntity getEntity(GroupFeedback groupFeedback, boolean fetchChild) {
		GroupFeedbackEntity groupFeedbackEntity = new GroupFeedbackEntity();
		groupFeedbackEntity.setVote(groupFeedback.getVote());
		UserMapper userMapper = new UserMapper();
		//We don't need user child
		groupFeedbackEntity.setGivenByUser(userMapper.getEntity(groupFeedback.getGivenByUser(), false));
		return groupFeedbackEntity;
	}

	@Override
	public GroupFeedbackEntity getEntityChild(GroupFeedback groupFeedback, GroupFeedbackEntity groupFeedbackEntity) {
		return groupFeedbackEntity;
	}

	@Override
	public GroupFeedback getDomainModel(GroupFeedbackEntity groupFeedbackEntity, boolean fetchChild) {
		GroupFeedback groupFeedback = new GroupFeedback();
		groupFeedback.setVote(groupFeedbackEntity.getVote());
		UserMapper userMapper = new UserMapper();
		//We don't need user child
		groupFeedback.setGivenByUser(userMapper.getDomainModel(groupFeedbackEntity.getGivenByUser(), false));
		return groupFeedback;
}

	@Override
	public GroupFeedback getDomainModelChild(GroupFeedback groupFeedback, GroupFeedbackEntity groupFeedbackEntity) {
		return groupFeedback;
	}

	@Override
	public Collection<GroupFeedback> getDomainModels(Collection<GroupFeedback> groupFeedbacks,
			Collection<GroupFeedbackEntity> groupFeedbackEntities, boolean fetchChild) {
		for (GroupFeedbackEntity groupFeedbackEntity : groupFeedbackEntities) {
			GroupFeedback groupFeedback = new GroupFeedback();
			groupFeedback = getDomainModel(groupFeedbackEntity, fetchChild);
			groupFeedbacks.add(groupFeedback);
		}
		return groupFeedbacks;
	}

	@Override
	public Collection<GroupFeedbackEntity> getEntities(Collection<GroupFeedbackEntity> groupFeedbackEntities,
			Collection<GroupFeedback> groupFeedbacks, boolean fetchChild) {
		for (GroupFeedback groupFeedback : groupFeedbacks) {
			GroupFeedbackEntity groupFeedbackEntity = new GroupFeedbackEntity();
			groupFeedbackEntity = getEntity(groupFeedback, fetchChild);
			groupFeedbackEntities.add(groupFeedbackEntity);
		}
		return groupFeedbackEntities;
	}

}
