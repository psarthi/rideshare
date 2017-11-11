package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.OTPEntity;
import com.digitusrevolution.rideshare.model.user.domain.OTP;

public class OTPMapper implements Mapper<OTP, OTPEntity>{
	
	@Override
	public OTPEntity getEntity(OTP otp, boolean fetchChild) {
		OTPEntity otpEntity = new OTPEntity();
		otpEntity.setMobileNumber(otp.getMobileNumber());
		otpEntity.setOTP(otp.getOTP());
		otpEntity.setExpirationTime(otp.getExpirationTime());
		return otpEntity;
	}

	@Override
	public OTP getDomainModel(OTPEntity otpEntity,boolean fetchChild) {
		OTP otp = new OTP();
		otp.setMobileNumber(otpEntity.getMobileNumber());
		otp.setOTP(otpEntity.getOTP());
		otp.setExpirationTime(otpEntity.getExpirationTime());
		return otp;
	}

	@Override
	public OTPEntity getEntityChild(OTP otp, OTPEntity otpEntity) {
		return otpEntity;
	}

	@Override
	public OTP getDomainModelChild(OTP otp, OTPEntity otpEntity) {
		return otp;
	}

	@Override
	public Collection<OTP> getDomainModels(Collection<OTP> otps, Collection<OTPEntity> otpEntities, boolean fetchChild) {
		for (OTPEntity otpEntity : otpEntities) {
			OTP otp = new OTP();
			otp = getDomainModel(otpEntity, fetchChild);
			otps.add(otp);
		}
		return otps;
	}

	@Override
	public Collection<OTPEntity> getEntities(Collection<OTPEntity> otpEntities, Collection<OTP> otps, boolean fetchChild) {
		for (OTP otp : otps) {
			OTPEntity otpEntity = new OTPEntity();
			otpEntity = getEntity(otp, fetchChild);
			otpEntities.add(otpEntity);
		}
		return otpEntities;
	}

}
