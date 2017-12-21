package com.digitusrevolution.rideshare.model.billing.domain.core;

public class Remark {
	
	private Purpose purpose;
	private String message;
	private String paidBy;
	private String paidTo;
	private int billNumber;
	private int rideId;
	private int rideRequestId;
	
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
	public int getBillNumber() {
		return billNumber;
	}
	public void setBillNumber(int billNumber) {
		this.billNumber = billNumber;
	}
	public int getRideId() {
		return rideId;
	}
	public void setRideId(int rideId) {
		this.rideId = rideId;
	}
	public int getRideRequestId() {
		return rideRequestId;
	}
	public void setRideRequestId(int rideRequestId) {
		this.rideRequestId = rideRequestId;
	}
	public Purpose getPurpose() {
		return purpose;
	}
	public void setPurpose(Purpose purpose) {
		this.purpose = purpose;
	}
	
	

}
