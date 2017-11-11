package com.digitusrevolution.rideshare.model.user.data;

import java.time.ZonedDateTime;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="otp")
public class OTPEntity {
	
	@Id
	private String mobileNumber;
	private String OTP;
	private ZonedDateTime expirationTime;
	
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getOTP() {
		return OTP;
	}
	public void setOTP(String oTP) {
		OTP = oTP;
	}
	public ZonedDateTime getExpirationTime() {
		return expirationTime;
	}
	public void setExpirationTime(ZonedDateTime expirationTime) {
		this.expirationTime = expirationTime;
	}
	
	
}
