package com.digitusrevolution.rideshare.common.mapper.ride;

import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkMapper {

	
	public TrustNetworkEntity getTrustNetworkEntity(TrustNetwork trustNetwork) {
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity.setId(trustNetwork.getId());
		return trustNetworkEntity;
	}


	public TrustNetwork getTrustNetwork(TrustNetworkEntity trustNetworkEntity) {
		TrustNetwork trustNetwork = new TrustNetwork();
		trustNetwork.setId(trustNetworkEntity.getId());
		return trustNetwork;
	}

}
