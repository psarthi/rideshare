package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkDO implements DomainObjectPKInteger<TrustNetwork>{

	private TrustNetwork trustNetwork;
	private final GenericDAO<TrustNetworkEntity, Integer> genericDAO = new GenericDAOImpl<>(TrustNetworkEntity.class);
	private static final Logger logger = LogManager.getLogger(TrustNetworkDO.class.getName());


	@Override
	public List<TrustNetwork> getAll() {
		List<TrustNetwork> trustNetworks = new ArrayList<>();
		List<TrustNetworkEntity> trustNetworkEntities = genericDAO.getAll();
		for (TrustNetworkEntity trustNetworkEntity : trustNetworkEntities) {
			TrustNetwork trustNetwork = new TrustNetwork();
			trustNetwork.setEntity(trustNetworkEntity);
			trustNetworks.add(trustNetwork);
		}
		return trustNetworks;
	}

	@Override
	public void update(TrustNetwork trustNetwork) {
		if (trustNetwork.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+trustNetwork.getId());
		}
		genericDAO.update(trustNetwork.getEntity());		
	}

	@Override
	public void delete(int id) {
		trustNetwork = new TrustNetwork();
		trustNetwork = get(id);
		genericDAO.delete(trustNetwork.getEntity());		
	}

	@Override
	public int create(TrustNetwork trustNetwork) {
		logger.entry();
		int id = genericDAO.create(trustNetwork.getEntity());
		logger.exit();
		return id;
	}

	@Override
	public TrustNetwork get(int id) {
		TrustNetworkEntity trustNetworkEntity = genericDAO.get(id);
		if (trustNetworkEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		trustNetwork.setEntity(trustNetworkEntity);
		return trustNetwork;
	}
}
