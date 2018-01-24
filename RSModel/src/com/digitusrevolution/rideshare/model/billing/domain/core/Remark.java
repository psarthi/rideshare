package com.digitusrevolution.rideshare.model.billing.domain.core;

public class Remark {
	
	private Purpose purpose;
	private String message;
	private String paidBy;
	private String paidTo;
	private long billNumber;
	private long rideId;
	private long rideRequestId;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getPaidBy() {
		return paidBy;
	}
	public void setPaidBy(String paidBy) {
		this.paidBy = paidBy;
	}
	public String getPaidTo() {
		return paidTo;
	}
	public void setPaidTo(String paidTo) {
		this.paidTo = paidTo;
	}
	public Purpose getPurpose() {
		return purpose;
	}
	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}
	public long getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(long billNumber) {
		this.billNumber = billNumber;
	}
	public long getRideId() {
		return rideId;
	}
	public void setRideId(long rideId) {
		this.rideId = rideId;
	}
	public long getRideRequestId() {
		return rideRequestId;
	}
	public void setRideRequestId(long rideRequestId) {
		this.rideRequestId = rideRequestId;
	}
	
	

}
