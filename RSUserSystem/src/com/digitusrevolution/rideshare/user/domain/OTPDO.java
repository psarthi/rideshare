package com.digitusrevolution.rideshare.user.domain;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.WebApplicationException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.auth.AuthService;
import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.OTPMapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.user.data.OTPEntity;
import com.digitusrevolution.rideshare.model.user.domain.OTP;
import com.digitusrevolution.rideshare.model.user.dto.OTPProviderResponse;

public class OTPDO implements DomainObjectPKString<OTP>{

	private OTP otp;
	private OTPEntity otpEntity;
	private OTPMapper otpMapper;
	private final GenericDAO<OTPEntity, String> genericDAO;
	private static final Logger logger = LogManager.getLogger(OTPDO.class.getName());

	public OTPDO() {
		otp = new OTP();
		otpEntity = new OTPEntity();
		otpMapper = new OTPMapper();
		genericDAO = new GenericDAOImpl<>(OTPEntity.class);
	}

	public void setOTP(OTP otp) {
		this.otp = otp;
		otpEntity = otpMapper.getEntity(otp, true);
	}

	private void setOTPEntity(OTPEntity otpEntity) {
		this.otpEntity = otpEntity;
		otp = otpMapper.getDomainModel(otpEntity, false);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
	}

	@Override
	public String create(OTP otp) {
		setOTP(otp);
		String mobileNumber = genericDAO.create(otpEntity);
		return mobileNumber;
	}

	@Override
	public OTP get(String mobileNumber) {
		otpEntity = genericDAO.get(mobileNumber);
		if (otpEntity == null){
			throw new NotFoundException("No Data found with id: "+mobileNumber);
		}
		setOTPEntity(otpEntity);
		return otp;
	}

	@Override
	public OTP getAllData(String mobileNumber) {
		get(mobileNumber);
		fetchChild();
		return otp;
	}

	@Override
	public List<OTP> getAll() {
		List<OTP> otps = new ArrayList<>();
		List<OTPEntity> otpEntities = genericDAO.getAll();
		for (OTPEntity otpEntity : otpEntities) {
			setOTPEntity(otpEntity);
			otps.add(otp);
		}
		return otps;
	}

	@Override
	public void update(OTP otp) {
		if (otp.getMobileNumber().isEmpty()){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+otp.getMobileNumber());
		}
		setOTP(otp);
		genericDAO.update(otpEntity);				
	}

	@Override
	public void delete(String mobileNumber) {
		otp = get(mobileNumber);
		setOTP(otp);
		genericDAO.delete(otpEntity);				
	}
	
	public void getOTP(String mobileNumber, boolean retry) {
		OTPProviderResponse otpResponse;
		if (retry) {
			otpResponse = RESTClientUtil.getOTPOnCall(mobileNumber);
		} else {
			String otpNumber = AuthService.getInstance().getVerificationCode();
			otp.setMobileNumber(mobileNumber);
			otp.setOTP(otpNumber);
			String otp_expiry_time = PropertyReader.getInstance().getProperty("OTP_EXPIRY_TIME_IN_MINS");
			otp.setExpirationTime(ZonedDateTime.now().plusMinutes(Long.parseLong(otp_expiry_time)));
			logger.debug("Mobile Number & OTP is:" + otp.getMobileNumber() +","+ otp.getOTP());
			logger.debug("Current Time is:" + ZonedDateTime.now());
			logger.debug("OTP Expiry Time is:" + otp.getExpirationTime());
			//Reason behind update and not create is: We want to have only one entry into the DB for each mobile number, 
			//so in case entry exist, then update is required else insert is required
			update(otp);
			otpResponse = RESTClientUtil.sendOTP(mobileNumber, otp.getOTP());
		}
		if (!otpResponse.getType().equals("success")) {
			throw new WebApplicationException("Unable to send OTP, please try again");
		}	
	}
	
	public boolean validateOTP(String mobileNumber, String otpNumber) {
		
		otp = get(mobileNumber);
		if (otp.getExpirationTime().isAfter(ZonedDateTime.now()) && otp.getOTP().equals(otpNumber)) {
			return true;
		} 
		return false;
	}
	
}
