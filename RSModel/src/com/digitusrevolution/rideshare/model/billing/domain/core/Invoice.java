package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.time.ZonedDateTime;

public class Invoice {
	
	private long number;
	private ZonedDateTime date;
	private float totalAmountEarned;
	private float serviceCharge;
	private float cgst;
	private float sgst;
	private float igst;
	private float tcs;
	private InvoiceStatus status;
	
	public long getNumber() {
		return number;
	}
	public void setNumber(long number) {
		this.number = number;
	}
	public ZonedDateTime getDate() {
		return date;
	}
	public void setDate(ZonedDateTime date) {
		this.date = date;
	}
	public float getTotalAmountEarned() {
		return totalAmountEarned;
	}
	public void setTotalAmountEarned(float totalAmountEarned) {
		this.totalAmountEarned = totalAmountEarned;
	}
	public float getServiceCharge() {
		return serviceCharge;
	}
	public void setServiceCharge(float serviceCharge) {
		this.serviceCharge = serviceCharge;
	}
	public float getCgst() {
		return cgst;
	}
	public void setCgst(float cgst) {
		this.cgst = cgst;
	}
	public float getSgst() {
		return sgst;
	}
	public void setSgst(float sgst) {
		this.sgst = sgst;
	}
	public float getIgst() {
		return igst;
	}
	public void setIgst(float igst) {
		this.igst = igst;
	}
	public float getTcs() {
		return tcs;
	}
	public void setTcs(float tcs) {
		this.tcs = tcs;
	}
	public InvoiceStatus getStatus() {
		return status;
	}
	public void setStatus(InvoiceStatus status) {
		this.status = status;
	}
	
	
}
