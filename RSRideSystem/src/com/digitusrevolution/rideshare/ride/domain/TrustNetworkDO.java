package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkDO implements DomainObject{

	private TrustNetwork trustNetwork;
	private TrustNetworkEntity trustNetworkEntity;
	private TrustNetworkMapper trustNetworkMapper;

	public TrustNetworkDO() {
		trustNetwork = new TrustNetwork();
		trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkMapper = new TrustNetworkMapper();
	}

	public TrustNetwork getTrustNetwork() {
		return trustNetwork;
	}

	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		trustNetworkEntity = trustNetworkMapper.getEntity(trustNetwork);
	}

	public TrustNetworkEntity getTrustNetworkEntity() {
		return trustNetworkEntity;
	}

	public void setTrustNetworkEntity(TrustNetworkEntity trustNetworkEntity) {
		this.trustNetworkEntity = trustNetworkEntity;
		trustNetwork = trustNetworkMapper.getDomainModel(trustNetworkEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}
}
