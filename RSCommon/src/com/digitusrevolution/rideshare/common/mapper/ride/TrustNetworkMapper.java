package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetworkMapper implements Mapper<TrustNetwork, TrustNetworkEntity>{

	@Override
	public TrustNetworkEntity getEntity(TrustNetwork trustNetwork) {
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity.setId(trustNetwork.getId());
		return trustNetworkEntity;
	}


	@Override
	public TrustNetwork getDomainModel(TrustNetworkEntity trustNetworkEntity) {
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork.setId(trustNetworkEntity.getId());
		return trustNetwork;
	}

	@Override
	public TrustNetworkEntity getEntityChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		Collection<TrustCategory> trustCategories = trustNetwork.getTrustCategories();
		trustNetworkEntity.setTrustCategories(trustCategoryMapper.getEntities(trustCategories));
		
		UserMapper userMapper = new UserMapper();
		Collection<User> users = trustNetwork.getFriends();
		trustNetworkEntity.setFriends(userMapper.getEntities(users));
		
		return trustNetworkEntity;
	}


	@Override
	public TrustNetwork getDomainModelChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {
		
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		Collection<TrustCategoryEntity> trustCategoryEntities = trustNetworkEntity.getTrustCategories();
		trustNetwork.setTrustCategories(trustCategoryMapper.getDomainModels(trustCategoryEntities));
		
		UserMapper userMapper = new UserMapper();
		Collection<UserEntity> userEntities = trustNetworkEntity.getFriends();
		trustNetwork.setFriends(userMapper.getDomainModels(userEntities));
	
		return trustNetwork;
	}


	@Override
	public Collection<TrustNetwork> getDomainModels(Collection<TrustNetworkEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collection<TrustNetworkEntity> getEntities(Collection<TrustNetwork> model) {
		// TODO Auto-generated method stub
		return null;
	}



}
