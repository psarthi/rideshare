package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.time.ZonedDateTime;

public class RewardCouponTransaction extends RewardTransaction {

	private String couponCode;
	private CouponStatus status;
	private ZonedDateTime expiryDateTime;
	private ZonedDateTime redemptionDateTime;
	
	public String getCouponCode() {
		return couponCode;
	}
	public void setCouponCode(String couponCode) {
		this.couponCode = couponCode;
	}
	public CouponStatus getStatus() {
		return status;
	}
	public void setStatus(CouponStatus status) {
		this.status = status;
	}
	public ZonedDateTime getRedemptionDateTime() {
		return redemptionDateTime;
	}
	public void setRedemptionDateTime(ZonedDateTime redemptionDateTime) {
		this.redemptionDateTime = redemptionDateTime;
	}
	public ZonedDateTime getExpiryDateTime() {
		return expiryDateTime;
	}
	public void setExpiryDateTime(ZonedDateTime expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}
	
	
}
