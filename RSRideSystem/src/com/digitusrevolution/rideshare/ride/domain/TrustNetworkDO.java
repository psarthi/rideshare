package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustNetworkMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustNetworkEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustNetwork;

public class TrustNetworkDO implements DomainObjectPKLong<TrustNetwork>{

	private TrustNetwork trustNetwork;
	private TrustNetworkEntity trustNetworkEntity;
	private TrustNetworkMapper trustNetworkMapper;
	private final GenericDAO<TrustNetworkEntity, Long> genericDAO;
	private static final Logger logger = LogManager.getLogger(TrustNetworkDO.class.getName());


	public TrustNetworkDO() {
		trustNetwork = new TrustNetwork();
		trustNetworkEntity = new TrustNetworkEntity();
		trustNetworkMapper = new TrustNetworkMapper();
		genericDAO = new GenericDAOImpl<>(TrustNetworkEntity.class);
	}

	public void setTrustNetwork(TrustNetwork trustNetwork) {
		this.trustNetwork = trustNetwork;
		trustNetworkEntity = trustNetworkMapper.getEntity(trustNetwork, true);
	}

	private void setTrustNetworkEntity(TrustNetworkEntity trustNetworkEntity) {
		this.trustNetworkEntity = trustNetworkEntity;
		trustNetwork = trustNetworkMapper.getDomainModel(trustNetworkEntity, false);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TrustNetwork> getAll() {
		List<TrustNetwork> trustNetworks = new ArrayList<>();
		List<TrustNetworkEntity> trustNetworkEntities = genericDAO.getAll();
		for (TrustNetworkEntity trustNetworkEntity : trustNetworkEntities) {
			setTrustNetworkEntity(trustNetworkEntity);
			trustNetworks.add(trustNetwork);
		}
		return trustNetworks;
	}

	@Override
	public void update(TrustNetwork trustNetwork) {
		if (trustNetwork.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+trustNetwork.getId());
		}
		setTrustNetwork(trustNetwork);
		genericDAO.update(trustNetworkEntity);		
	}

	@Override
	public void delete(long id) {
		trustNetwork = get(id);
		setTrustNetwork(trustNetwork);
		genericDAO.delete(trustNetworkEntity);		
	}

	@Override
	public long create(TrustNetwork trustNetwork) {
		logger.entry();
		setTrustNetwork(trustNetwork);
		long id = genericDAO.create(trustNetworkEntity);
		logger.exit();
		return id;
	}

	@Override
	public TrustNetwork get(long id) {
		trustNetworkEntity = genericDAO.get(id);
		if (trustNetworkEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setTrustNetworkEntity(trustNetworkEntity);
		return trustNetwork;
	}

	@Override
	public TrustNetwork getAllData(long id) {
		get(id);
		fetchChild();
		return trustNetwork;
	}
}
