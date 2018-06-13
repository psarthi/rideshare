package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

public class RewardCouponTransaction extends RewardTransaction {

	private String couponCode;
	private CouponStatus status;
	
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
	
	
}
