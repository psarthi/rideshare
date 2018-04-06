package com.digitusrevolution.rideshare.model.billing.dto;

public class PaytmGratificationRequest {
	
	private Request request = new Request();
	private String metadata;
	private String ipAddress;
	private String platformName;
	private String operationType;
	
	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
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
		private String merchantGuid;
		private String merchantOrderId;
		private String salesWalletName;
		private String salesWalletGuid;
		private String payeeEmailId;
		private String payeePhoneNumber;
		private String payeeSsoId;
		private String appliedToNewUsers;
		private String amount;
		private String currencyCode;
		
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
		public String getMerchantOrderId() {
			return merchantOrderId;
		}
		public void setMerchantOrderId(String merchantOrderId) {
			this.merchantOrderId = merchantOrderId;
		}
		public String getSalesWalletName() {
			return salesWalletName;
		}
		public void setSalesWalletName(String salesWalletName) {
			this.salesWalletName = salesWalletName;
		}
		public String getSalesWalletGuid() {
			return salesWalletGuid;
		}
		public void setSalesWalletGuid(String salesWalletGuid) {
			this.salesWalletGuid = salesWalletGuid;
		}
		public String getPayeeEmailId() {
			return payeeEmailId;
		}
		public void setPayeeEmailId(String payeeEmailId) {
			this.payeeEmailId = payeeEmailId;
		}
		public String getPayeePhoneNumber() {
			return payeePhoneNumber;
		}
		public void setPayeePhoneNumber(String payeePhoneNumber) {
			this.payeePhoneNumber = payeePhoneNumber;
		}
		public String getPayeeSsoId() {
			return payeeSsoId;
		}
		public void setPayeeSsoId(String payeeSsoId) {
			this.payeeSsoId = payeeSsoId;
		}
		public String getAppliedToNewUsers() {
			return appliedToNewUsers;
		}
		public void setAppliedToNewUsers(String appliedToNewUsers) {
			this.appliedToNewUsers = appliedToNewUsers;
		}
		public String getAmount() {
			return amount;
		}
		public void setAmount(String amount) {
			this.amount = amount;
		}
		public String getCurrencyCode() {
			return currencyCode;
		}
		public void setCurrencyCode(String currencyCode) {
			this.currencyCode = currencyCode;
		}
	}

}
