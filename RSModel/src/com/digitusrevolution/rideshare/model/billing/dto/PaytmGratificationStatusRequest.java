package com.digitusrevolution.rideshare.model.billing.dto;

public class PaytmGratificationStatusRequest {
	
	private Request request = new Request();
	private String platformName;
	private String operationType;
	
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}
	
	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}

	public String getOperationType() {
		return operationType;
	}

	public void setOperationType(String operationType) {
		this.operationType = operationType;
	}

	public class Request {
		
		private String requestType;
		private String txnType;
		private String txnId;
		private String merchantGuid;
		
		public String getRequestType() {
			return requestType;
		}
		public void setRequestType(String requestType) {
			this.requestType = requestType;
		}
		public String getMerchantGuid() {
			return merchantGuid;
		}
		public void setMerchantGuid(String merchantGuid) {
			this.merchantGuid = merchantGuid;
		}
		public String getTxnType() {
			return txnType;
		}
		public void setTxnType(String txnType) {
			this.txnType = txnType;
		}
		public String getTxnId() {
			return txnId;
		}
		public void setTxnId(String txnId) {
			this.txnId = txnId;
		}
		
	}

}
