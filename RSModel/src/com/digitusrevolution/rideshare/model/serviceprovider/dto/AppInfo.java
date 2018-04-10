package com.digitusrevolution.rideshare.model.serviceprovider.dto;

public class AppInfo {
	
	private int minAppVersionCode;
	private String appUrl;
	private String shareMsg;

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
	
	

}
