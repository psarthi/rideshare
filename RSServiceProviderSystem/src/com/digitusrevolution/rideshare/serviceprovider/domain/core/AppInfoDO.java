package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.AppInfoMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.AppInfoEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.AppInfo;

public class AppInfoDO implements DomainObjectPKInteger<AppInfo>{

	private AppInfo appInfo;
	private AppInfoEntity appInfoEntity;
	private AppInfoMapper appInfoMapper;
	private final GenericDAO<AppInfoEntity, Integer> genericDAO;
	
	public AppInfoDO() {
		appInfo = new AppInfo();
		appInfoEntity = new AppInfoEntity();
		appInfoMapper = new AppInfoMapper();
		genericDAO = new GenericDAOImpl<>(AppInfoEntity.class);
	}

	public void setAppInfo(AppInfo appInfo) {
		this.appInfo = appInfo;
		appInfoEntity = appInfoMapper.getEntity(appInfo, true);
	}

	public void setAppInfoEntity(AppInfoEntity appInfoEntity) {
		this.appInfoEntity = appInfoEntity;
		appInfo = appInfoMapper.getDomainModel(appInfoEntity, false);
	}

	@Override
	public List<AppInfo> getAll() {
		List<AppInfo> appInfos = new ArrayList<>();
		List<AppInfoEntity> appInfoEntities = genericDAO.getAll();
		for (AppInfoEntity appInfoEntity : appInfoEntities) {
			setAppInfoEntity(appInfoEntity);
			appInfos.add(appInfo);
		}
		return appInfos;
	}

	@Override
	public void update(AppInfo appInfo) {
		if (appInfo.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+appInfo.getId());
		}
		setAppInfo(appInfo);
		genericDAO.update(appInfoEntity);				
	}

	@Override
	public void fetchChild() {
		appInfo = appInfoMapper.getDomainModelChild(appInfo, appInfoEntity);
	}

	@Override
	public int create(AppInfo appInfo) {
		setAppInfo(appInfo);
		int id = genericDAO.create(appInfoEntity);
		return id;
	}

	@Override
	public AppInfo get(int id) {
		appInfoEntity = genericDAO.get(id);
		if (appInfoEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setAppInfoEntity(appInfoEntity);
		return appInfo;
	}

	@Override
	public AppInfo getAllData(int id) {
		get(id);
		fetchChild();
		return appInfo;
	}

	@Override
	public void delete(int id) {
		appInfo = get(id);
		setAppInfo(appInfo);
		genericDAO.delete(appInfoEntity);			
	}

}
