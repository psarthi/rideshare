package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.AppInfoEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.HelpQuestionAnswerEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.AppInfo;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.HelpQuestionAnswer;

public class AppInfoMapper implements Mapper<AppInfo, AppInfoEntity>{

	@Override
	public AppInfoEntity getEntity(AppInfo appInfo, boolean fetchChild) {
		AppInfoEntity appInfoEntity = new AppInfoEntity();
		appInfoEntity.setId(appInfo.getId());
		appInfoEntity.setAppUrl(appInfo.getAppUrl());
		appInfoEntity.setHomePageMsg(appInfo.getHomePageMsg());
		appInfoEntity.setMinAppVersionCode(appInfo.getMinAppVersionCode());
		appInfoEntity.setShareMsg(appInfo.getShareMsg());
		return appInfoEntity;
	}

	@Override
	public AppInfoEntity getEntityChild(AppInfo appInfo, AppInfoEntity appInfoEntity) {
		// TODO Auto-generated method stub
		return appInfoEntity;
	}

	@Override
	public AppInfo getDomainModel(AppInfoEntity appInfoEntity, boolean fetchChild) {
		AppInfo appInfo = new AppInfo();
		appInfo.setId(appInfoEntity.getId());
		appInfo.setAppUrl(appInfoEntity.getAppUrl());
		appInfo.setHomePageMsg(appInfoEntity.getHomePageMsg());
		appInfo.setMinAppVersionCode(appInfoEntity.getMinAppVersionCode());
		appInfo.setShareMsg(appInfoEntity.getShareMsg());
		return appInfo;	
	}

	@Override
	public AppInfo getDomainModelChild(AppInfo appInfo, AppInfoEntity appInfoEntity) {
		// TODO Auto-generated method stub
		return appInfo;
	}

	@Override
	public Collection<AppInfo> getDomainModels(Collection<AppInfo> appInfos, Collection<AppInfoEntity> appInfoEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return appInfos;
	}

	@Override
	public Collection<AppInfoEntity> getEntities(Collection<AppInfoEntity> appInfoEntities, Collection<AppInfo> appInfos,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return appInfoEntities;
	}


}
