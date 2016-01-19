package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkMapper implements Mapper<TrustNetwork, TrustNetworkEntity>{

	@Override
	public TrustNetworkEntity getEntity(TrustNetwork trustNetwork, boolean fetchChild) {
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity.setId(trustNetwork.getId());

		if (fetchChild){
			trustNetworkEntity = getEntityChild(trustNetwork, trustNetworkEntity);
		}

		return trustNetworkEntity;
	}

	@Override
	public TrustNetwork getDomainModel(TrustNetworkEntity trustNetworkEntity, boolean fetchChild) {
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork.setId(trustNetworkEntity.getId());
		
		if (fetchChild){
			trustNetwork = getDomainModelChild(trustNetwork, trustNetworkEntity);		
		}
		
		return trustNetwork;
	}

	@Override
	public TrustNetworkEntity getEntityChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		trustNetworkEntity.setTrustCategories(trustCategoryMapper.getEntities(trustNetworkEntity.getTrustCategories(), 
				trustNetwork.getTrustCategories(), true));

		UserMapper userMapper = new UserMapper();
		trustNetworkEntity.setFriends(userMapper.getEntities(trustNetworkEntity.getFriends(), trustNetwork.getFriends(), true));

		return trustNetworkEntity;
	}

	@Override
	public TrustNetwork getDomainModelChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {

		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		trustNetwork.setTrustCategories(trustCategoryMapper.getDomainModels(trustNetwork.getTrustCategories(), 
				trustNetworkEntity.getTrustCategories(), true));

		UserMapper userMapper = new UserMapper();
		trustNetwork.setFriends(userMapper.getDomainModels(trustNetwork.getFriends(), 
				trustNetworkEntity.getFriends(), true));

		return trustNetwork;
	}

	@Override
	public Collection<TrustNetwork> getDomainModels(Collection<TrustNetwork> models,
			Collection<TrustNetworkEntity> entities, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TrustNetworkEntity> getEntities(Collection<TrustNetworkEntity> entities,
			Collection<TrustNetwork> model, boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}


}
