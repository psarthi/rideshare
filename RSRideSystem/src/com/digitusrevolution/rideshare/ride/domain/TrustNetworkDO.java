package com.digitusrevolution.rideshare.ride.domain;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustCategoryMapper;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;

public class TrustNetworkDO implements DomainObject{

	private TrustNetwork trustNetwork;
	private TrustNetworkEntity trustNetworkEntity;

	public TrustNetworkDO() {
		trustNetwork = new TrustNetwork();
		trustNetworkEntity = new TrustNetworkEntity();
	}

	public TrustNetwork getTrustNetwork() {
		return trustNetwork;
	}



	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		mapDomainModelToDataModel();
		mapChildDomainModelToDataModel();
	}



	public TrustNetworkEntity getTrustNetworkEntity() {
		return trustNetworkEntity;
	}



	public void setTrustNetworkEntity(TrustNetworkEntity trustNetworkEntity) {
		this.trustNetworkEntity = trustNetworkEntity;
		mapDataModelToDomainModel();
	}

	@Override
	public void mapDomainModelToDataModel() {
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		trustNetworkEntity = trustNetworkMapper.getTrustNetworkEntity(trustNetwork);
	}

	@Override
	public void mapDataModelToDomainModel() {
		TrustNetworkMapper trustNetworkMapper = new TrustNetworkMapper();
		trustNetwork = trustNetworkMapper.getTrustNetwork(trustNetworkEntity);
	}

	@Override
	public void mapChildDomainModelToDataModel() {

			TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
			Collection<TrustCategory> trustCategories = trustNetwork.getTrustCategories();
			trustNetworkEntity.setTrustCategories(trustCategoryMapper.getTrustCategoryEntities(trustCategories));
			
			UserMapper userMapper = new UserMapper();
			Collection<User> users = trustNetwork.getFriends();
			trustNetworkEntity.setFriends(userMapper.getuserEntities(users));
	
	}

	@Override
	public void mapChildDataModelToDomainModel() {

		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		Collection<TrustCategoryEntity> trustCategoryEntities = trustNetworkEntity.getTrustCategories();
		trustNetwork.setTrustCategories(trustCategoryMapper.getTrustCategories(trustCategoryEntities));
		
		UserMapper userMapper = new UserMapper();
		Collection<UserEntity> userEntities = trustNetworkEntity.getFriends();
		trustNetwork.setFriends(userMapper.getUsers(userEntities));
		
	}


}
