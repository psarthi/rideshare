package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

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
		trustNetworkEntity.setId(trustNetwork.getId());

	}

	@Override
	public void mapDataModelToDomainModel() {
		trustNetwork.setId(trustNetworkEntity.getId());

	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub

	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub

	}

}
