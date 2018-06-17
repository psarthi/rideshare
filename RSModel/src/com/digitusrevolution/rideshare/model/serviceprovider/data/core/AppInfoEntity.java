package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="app")
public class AppInfoEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private int minAppVersionCode;
	private String appUrl;
	private String shareMsg;
	private String homePageMsg;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMinAppVersionCode() {
		return minAppVersionCode;
	}

	public void setMinAppVersionCode(int minAppVersionCode) {
		this.minAppVersionCode = minAppVersionCode;
	}

	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	public String getShareMsg() {
		return shareMsg;
	}

	public void setShareMsg(String shareMsg) {
		this.shareMsg = shareMsg;
	}

	public String getHomePageMsg() {
		return homePageMsg;
	}

	public void setHomePageMsg(String homePageMsg) {
		this.homePageMsg = homePageMsg;
	}
	
	

}
