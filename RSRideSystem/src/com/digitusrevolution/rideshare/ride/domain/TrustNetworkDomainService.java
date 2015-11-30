package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;
import com.digitusrevolution.rideshare.ride.data.TrustNetworkDAO;

public class TrustNetworkDomainService implements DomainService<TrustNetwork>{
	
	private static final Logger logger = LogManager.getLogger(TrustNetworkDomainService.class.getName());
	private final TrustNetworkDAO trustNetworkDAO;

	public TrustNetworkDomainService() {
		trustNetworkDAO = new TrustNetworkDAO();
	}

	@Override
	public int create(TrustNetwork trustNetwork) {
		logger.entry();
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		trustNetworkDO.setTrustNetwork(trustNetwork);
		int id = trustNetworkDAO.create(trustNetworkDO.getTrustNetworkEntity());
		logger.exit();
		return id;
	}

	@Override
	public TrustNetwork get(int id) {
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity = trustNetworkDAO.get(id);
		if (trustNetworkEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		trustNetworkDO.setTrustNetworkEntity(trustNetworkEntity);
		return trustNetworkDO.getTrustNetwork();

	}

	@Override
	public TrustNetwork getChild(int id) {

		// Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch

		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		TrustNetworkEntity trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkEntity = trustNetworkDAO.get(id);
		if (trustNetworkEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		trustNetworkDO.setTrustNetworkEntity(trustNetworkEntity);
		trustNetworkDO.fetchChild();
		return trustNetworkDO.getTrustNetwork();

	}

	@Override
	public List<TrustNetwork> getAll() {
		List<TrustNetworkEntity> trustNetworkEntities = new ArrayList<>();
		List<TrustNetwork> trustNetworks = new ArrayList<>();
		trustNetworkEntities = trustNetworkDAO.getAll();
		for (TrustNetworkEntity trustNetworkEntity : trustNetworkEntities) {
			TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
			trustNetworkDO.setTrustNetworkEntity(trustNetworkEntity);;
			trustNetworks.add(trustNetworkDO.getTrustNetwork());
		}
		return trustNetworks;

	}

	@Override
	public void update(TrustNetwork trustNetwork) {
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		trustNetworkDO.setTrustNetwork(trustNetwork);
		trustNetworkDAO.update(trustNetworkDO.getTrustNetworkEntity());		
	}

	@Override
	public void delete(TrustNetwork trustNetwork) {
		TrustNetworkDO trustNetworkDO = new TrustNetworkDO();
		trustNetworkDO.setTrustNetwork(trustNetwork);
		trustNetworkDAO.delete(trustNetworkDO.getTrustNetworkEntity());		
	
	}

}
