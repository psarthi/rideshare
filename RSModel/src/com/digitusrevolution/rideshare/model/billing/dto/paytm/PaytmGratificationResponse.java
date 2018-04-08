package com.digitusrevolution.rideshare.model.billing.dto.paytm;

public class PaytmGratificationResponse {
	
	private Response response;
	private String type;
	private String requestGuid;
	private String orderId;
	private String status;
	private String statusCode;
	private String statusMessage;
	private String metadata;
	
	public Response getResponse() {
		return response;
	}

	public void setResponse(Response response) {
		this.response = response;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getRequestGuid() {
		return requestGuid;
	}

	public void setRequestGuid(String requestGuid) {
		this.requestGuid = requestGuid;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusMessage() {
		return statusMessage;
	}

	public void setStatusMessage(String statusMessage) {
		this.statusMessage = statusMessage;
	}

	public String getMetadata() {
		return metadata;
	}

	public void setMetadata(String metadata) {
		this.metadata = metadata;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public class Response {
		
		private String walletSysTransactionId;

		public String getWalletSysTransactionId() {
			return walletSysTransactionId;
		}

		public void setWalletSysTransactionId(String walletSysTransactionId) {
			this.walletSysTransactionId = walletSysTransactionId;
		}
		
		
	}
}
