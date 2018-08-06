package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

public class AppInfo {
	
	private int id;
	private int minAppVersionCode;
	private String appUrl;
	private String shareMsg;
	private String homePageMsg;
	private boolean defaultRecurringRideOption;

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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isDefaultRecurringRideOption() {
		return defaultRecurringRideOption;
	}

	public void setDefaultRecurringRideOption(boolean defaultRecurringRideOption) {
		this.defaultRecurringRideOption = defaultRecurringRideOption;
	}
	
	

}
