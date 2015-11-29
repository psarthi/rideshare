package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainServicePKString;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.ride.data.TrustCategoryDAO;

public class TrustCategoryDomainService implements DomainServicePKString<TrustCategory>{

	private static final Logger logger = LogManager.getLogger(TrustCategoryDomainService.class.getName());
	private final TrustCategoryDAO trustCategoryDAO;

	public TrustCategoryDomainService() {

		trustCategoryDAO = new TrustCategoryDAO();

	}

	@Override
	public String create(TrustCategory trustCategory) {
		logger.entry();
		TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
		trustCategoryDO.setTrustCategory(trustCategory);
		String id = trustCategoryDAO.create(trustCategoryDO.getTrustCategoryEntity());
		logger.exit();
		return id;
	}

	@Override
	public TrustCategory get(String id) {
		TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity = trustCategoryDAO.get(id);
		if (trustCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		trustCategoryDO.setTrustCategoryEntity(trustCategoryEntity);
		return trustCategoryDO.getTrustCategory();
	}

	@Override
	public TrustCategory getChild(String id) {
		// Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch
		TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
		TrustCategoryEntity trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryEntity = trustCategoryDAO.get(id);
		if (trustCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		trustCategoryDO.setTrustCategoryEntity(trustCategoryEntity);
		trustCategoryDO.mapChildDataModelToDomainModel();
		return trustCategoryDO.getTrustCategory();
	}

	@Override
	public List<TrustCategory> getAll() {
		List<TrustCategoryEntity> trustCategoryEntities = new ArrayList<>();
		List<TrustCategory> trustCategories = new ArrayList<>();
		trustCategoryEntities = trustCategoryDAO.getAll();
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
			trustCategoryDO.setTrustCategoryEntity(trustCategoryEntity);
			trustCategories.add(trustCategoryDO.getTrustCategory());
		}
		return trustCategories;
	}

	@Override
	public void update(TrustCategory trustCategory) {
		TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
		trustCategoryDO.setTrustCategory(trustCategory);;
		trustCategoryDAO.update(trustCategoryDO.getTrustCategoryEntity());

	}

	@Override
	public void delete(TrustCategory trustCategory) {
		TrustCategoryDO trustCategoryDO = new TrustCategoryDO();
		trustCategoryDO.setTrustCategory(trustCategory);;
		trustCategoryDAO.delete(trustCategoryDO.getTrustCategoryEntity());
		
	}

}
