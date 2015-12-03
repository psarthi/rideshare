package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.ride.TrustCategoryMapper;
import com.digitusrevolution.rideshare.model.ride.data.TrustCategoryEntity;
import com.digitusrevolution.rideshare.model.ride.domain.TrustCategory;

public class TrustCategoryDO implements DomainObjectPKString<TrustCategory>{
	
	private TrustCategory trustCategory;
	private TrustCategoryEntity trustCategoryEntity;
	private TrustCategoryMapper trustCategoryMapper;
	private final GenericDAO<TrustCategoryEntity, String> genericDAO;
	private static final Logger logger = LogManager.getLogger(TrustCategoryDO.class.getName());

	
	public TrustCategoryDO() {
		trustCategory = new TrustCategory();
		trustCategoryEntity = new TrustCategoryEntity();
		trustCategoryMapper = new TrustCategoryMapper();
		genericDAO = new GenericDAOImpl<>(TrustCategoryEntity.class);
	}
	
	public TrustCategory getTrustCategory() {
		return trustCategory;
	}

	public void setTrustCategory(TrustCategory trustCategory) {
		this.trustCategory = trustCategory;
		trustCategoryEntity = trustCategoryMapper.getEntity(trustCategory);
	}

	public TrustCategoryEntity getTrustCategoryEntity() {
		return trustCategoryEntity;
	}

	public void setTrustCategoryEntity(TrustCategoryEntity trustCategoryEntity) {
		this.trustCategoryEntity = trustCategoryEntity;
		trustCategory = trustCategoryMapper.getDomainModel(trustCategoryEntity);
	}

	@Override
	public List<TrustCategory> getAll() {
		List<TrustCategory> trustCategories = new ArrayList<>();
		List<TrustCategoryEntity> trustCategoryEntities = genericDAO.getAll();
		for (TrustCategoryEntity trustCategoryEntity : trustCategoryEntities) {
			setTrustCategoryEntity(trustCategoryEntity);
			trustCategories.add(trustCategory);
		}
		return trustCategories;
	}

	@Override
	public void update(TrustCategory trustCategory) {
		setTrustCategory(trustCategory);
		genericDAO.update(trustCategoryEntity);		
	}

	@Override
	public void delete(TrustCategory trustCategory) {
		setTrustCategory(trustCategory);
		genericDAO.delete(trustCategoryEntity);		
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String create(TrustCategory trustCategory) {
		logger.entry();
		setTrustCategory(trustCategory);
		String name = genericDAO.create(trustCategoryEntity);
		logger.exit();
		return name;
	}

	@Override
	public TrustCategory get(String name) {
		trustCategoryEntity = genericDAO.get(name);
		if (trustCategoryEntity == null){
			throw new NotFoundException("No Data found with id: "+name);
		}
		setTrustCategoryEntity(trustCategoryEntity);
		return trustCategory;
	}

	@Override
	public TrustCategory getChild(String name) {
		get(name);
		fetchChild();
		return trustCategory;
	}

}
