package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategoryName;

public class TrustCategoryDO implements DomainObjectPKString<TrustCategory>{
	
	private TrustCategory trustCategory;
	private final GenericDAO<TrustCategoryEntity, TrustCategoryName> genericDAO = new GenericDAOImpl<>(TrustCategoryEntity.class);
	private static final Logger logger = LogManager.getLogger(TrustCategoryDO.class.getName());

	
	@Override
	public List<TrustCategory> getAll() {
		List<TrustCategory> trustCategories = new ArrayList<>();
		List<TrustCategoryEntity> trustCategoryEntities = genericDAO.getAll();
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			TrustCategory trustCategory = new TrustCategory();
			trustCategory.setEntity(trustCategoryEntity);
			trustCategories.add(trustCategory);
		}
		return trustCategories;
	}

	@Override
	public void update(TrustCategory trustCategory) {
		if (trustCategory.getName().toString().isEmpty()){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+trustCategory.getName());
		}
		genericDAO.update(trustCategory.getEntity());		
	}

	@Override
	public void delete(String name) {
		trustCategory = get(name);
		genericDAO.delete(trustCategory.getEntity());		
	}

	@Override
	public String create(TrustCategory trustCategory) {
		logger.entry();
		String name = genericDAO.create(trustCategory.getEntity()).toString();
		logger.exit();
		return name;
	}

	@Override
	public TrustCategory get(String name) {
		trustCategory = new TrustCategory();
		TrustCategoryEntity trustCategoryEntity = genericDAO.get(TrustCategoryName.valueOf(name));
		if (trustCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+name);
		}
		trustCategory.setEntity(trustCategoryEntity);
		return trustCategory;
	}
}
