package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.CouponStatus;

@Entity
@Table(name="reward_coupon_transaction")
public class RewardCouponTransactionEntity extends RewardTransactionEntity {

	private String couponCode;
	@Column
	@Enumerated(EnumType.STRING)
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
