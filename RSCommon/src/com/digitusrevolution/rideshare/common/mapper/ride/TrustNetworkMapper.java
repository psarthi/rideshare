package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkMapper implements Mapper<TrustNetwork, TrustNetworkEntity>{

	@Override
	public TrustNetworkEntity getEntityWithOnlyPK(TrustNetwork trustNetwork) {
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity.setId(trustNetwork.getId());
		return trustNetworkEntity;
	}

	@Override
	public TrustNetworkEntity getEntity(TrustNetwork trustNetwork) {
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity = getEntityWithOnlyPK(trustNetwork);

		trustNetworkEntity = getEntityChild(trustNetwork, trustNetworkEntity);

		return trustNetworkEntity;
	}

	@Override
	public TrustNetwork getDomainModelWithOnlyPK(TrustNetworkEntity trustNetworkEntity) {
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork.setId(trustNetworkEntity.getId());
		return trustNetwork;
	}

	@Override
	public TrustNetwork getDomainModel(TrustNetworkEntity trustNetworkEntity) {
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork = getDomainModelWithOnlyPK(trustNetworkEntity);
		return trustNetwork;
	}

	@Override
	public TrustNetworkEntity getEntityChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {
		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		trustNetworkEntity.setTrustCategories(trustCategoryMapper.getEntities(trustNetworkEntity.getTrustCategories(), 
				trustNetwork.getTrustCategories()));

		UserMapper userMapper = new UserMapper();
		trustNetworkEntity.setFriends(userMapper.getEntities(trustNetworkEntity.getFriends(), trustNetwork.getFriends()));

		return trustNetworkEntity;
	}

	@Override
	public TrustNetwork getDomainModelChild(TrustNetwork trustNetwork, TrustNetworkEntity trustNetworkEntity) {

		TrustCategoryMapper trustCategoryMapper = new TrustCategoryMapper();
		trustNetwork.setTrustCategories(trustCategoryMapper.getDomainModels(trustNetwork.getTrustCategories(), 
				trustNetworkEntity.getTrustCategories()));

		UserMapper userMapper = new UserMapper();
		trustNetwork.setFriends(userMapper.getDomainModels(trustNetwork.getFriends(), 
				trustNetworkEntity.getFriends()));

		return trustNetwork;
	}

	@Override
	public Collection<TrustNetwork> getDomainModelsWithOnlyPK(Collection<TrustNetwork> models,
			Collection<TrustNetworkEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TrustNetwork> getDomainModels(Collection<TrustNetwork> models,
			Collection<TrustNetworkEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TrustNetworkEntity> getEntitiesWithOnlyPK(Collection<TrustNetworkEntity> entities,
			Collection<TrustNetwork> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<TrustNetworkEntity> getEntities(Collection<TrustNetworkEntity> entities,
			Collection<TrustNetwork> model) {
		// TODO Auto-generated method stub
		return null;
	}


}
