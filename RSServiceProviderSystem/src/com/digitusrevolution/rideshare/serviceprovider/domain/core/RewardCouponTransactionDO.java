package com.digitusrevolution.rideshare.serviceprovider.domain.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.serviceprovider.core.RewardCouponTransactionMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.common.util.DateTimeUtil;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardCouponTransactionEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.CouponStatus;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.user.data.core.UserEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.User;
import com.digitusrevolution.rideshare.serviceprovider.data.RewardCouponTransactionDAO;

public class RewardCouponTransactionDO implements DomainObjectPKInteger<RewardCouponTransaction>{
	
	private RewardCouponTransaction rewardCouponTransaction;
	private RewardCouponTransactionEntity rewardCouponTransactionEntity;
	private RewardCouponTransactionMapper rewardCouponTransactionMapper;
	private final RewardCouponTransactionDAO rewardCouponTransactionDAO;
	
	public RewardCouponTransactionDO() {
		rewardCouponTransaction = new RewardCouponTransaction();
		rewardCouponTransactionEntity = new RewardCouponTransactionEntity();
		rewardCouponTransactionMapper = new RewardCouponTransactionMapper();
		rewardCouponTransactionDAO = new RewardCouponTransactionDAO();
	}

	public void setRewardCouponTransaction(RewardCouponTransaction rewardCouponTransaction) {
		this.rewardCouponTransaction = rewardCouponTransaction;
		rewardCouponTransactionEntity = rewardCouponTransactionMapper.getEntity(rewardCouponTransaction, true);
		
	}

	private void setRewardCouponTransactionEntity(RewardCouponTransactionEntity rewardCouponTransactionEntity) {
		this.rewardCouponTransactionEntity = rewardCouponTransactionEntity;
		rewardCouponTransaction = rewardCouponTransactionMapper.getDomainModel(rewardCouponTransactionEntity, false);
	}

	@Override
	public void fetchChild() {
		rewardCouponTransaction = rewardCouponTransactionMapper.getDomainModelChild(rewardCouponTransaction, rewardCouponTransactionEntity);
		
	}

	@Override
	public int create(RewardCouponTransaction rewardCouponTransaction) {
		setRewardCouponTransaction(rewardCouponTransaction);
		int id = rewardCouponTransactionDAO.create(rewardCouponTransactionEntity);
		return id;
	}

	@Override
	public RewardCouponTransaction get(int id) {
		rewardCouponTransactionEntity = rewardCouponTransactionDAO.get(id);
		if (rewardCouponTransactionEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRewardCouponTransactionEntity(rewardCouponTransactionEntity);
		return rewardCouponTransaction;
	}

	@Override
	public RewardCouponTransaction getAllData(int id) {
		get(id);
		fetchChild();
		return rewardCouponTransaction;
	}

	@Override
	public List<RewardCouponTransaction> getAll() {
		List<RewardCouponTransaction> cities = new ArrayList<>();
		List<RewardCouponTransactionEntity> rewardCouponTransactionEntities = rewardCouponTransactionDAO.getAll();
		for (RewardCouponTransactionEntity rewardCouponTransactionEntity : rewardCouponTransactionEntities) {
			setRewardCouponTransactionEntity(rewardCouponTransactionEntity);
			cities.add(rewardCouponTransaction);
		}
		return cities;
	}

	@Override
	public void update(RewardCouponTransaction rewardCouponTransaction) {
		if (rewardCouponTransaction.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+rewardCouponTransaction.getId());
		}
		setRewardCouponTransaction(rewardCouponTransaction);
		rewardCouponTransactionDAO.update(rewardCouponTransactionEntity);				
	}

	@Override
	public void delete(int id) {
		rewardCouponTransaction = get(id);
		setRewardCouponTransaction(rewardCouponTransaction);
		rewardCouponTransactionDAO.delete(rewardCouponTransactionEntity);			
	}
	
	public List<RewardCouponTransaction> getCouponTransactions(long userId, int page){
		User user = RESTClientUtil.getBasicUser(userId);
		UserMapper userMapper = new UserMapper();
		UserEntity userEntity = userMapper.getEntity(user, false);
		//This will help in calculating the index for the result - 0 to 9, 10 to 19, 20 to 29 etc.
		int itemsCount = 10;
		int startIndex = page*itemsCount;
		List<RewardCouponTransactionEntity> couponTransactionEntities = rewardCouponTransactionDAO.getCouponTransactions(userEntity, startIndex);
		List<RewardCouponTransaction> couponTransactions = new LinkedList<>();
		couponTransactions = (List<RewardCouponTransaction>) rewardCouponTransactionMapper.getDomainModels(couponTransactions, couponTransactionEntities, false);
		Collections.sort(couponTransactions);
		return couponTransactions;
	}
	
	public RewardCouponTransaction generateCoupon(long userId, int offerId) {
		String couponCode = null;
		//Check User Eligibility for offers
		//If eligible, generate 6 digit coupon code
		//TODO - For the time being lets not do this double check, if required we will do it later
		OfferDO offerDO = new OfferDO();
		couponCode = generateRandomChars(PropertyReader.getInstance().getProperty("ALPHABETS_NUMBER_STRING"), 
				Integer.parseInt(PropertyReader.getInstance().getProperty("COUPON_CODE_LENGTH")));
		//create coupon transaction in the system
		RewardCouponTransaction couponTransaction = new RewardCouponTransaction();
		couponTransaction.setCouponCode(couponCode);
		couponTransaction.setOffer(offerDO.get(offerId));
		couponTransaction.setUser(RESTClientUtil.getBasicUser(userId));
		couponTransaction.setRewardTransactionDateTime(DateTimeUtil.getCurrentTimeInUTC());
		couponTransaction.setStatus(CouponStatus.Active);
		couponTransaction.setExpiryDateTime(DateTimeUtil.getCurrentTimeInUTC()
				.plusDays(Long.parseLong(PropertyReader.getInstance().getProperty("COUPON_EXPIRY_DAYS"))));
		int id = create(couponTransaction);
		couponTransaction.setId(id);
		return couponTransaction;
	}
	
	public String generateRandomChars(String candidateChars, int length) {
	    StringBuilder sb = new StringBuilder();
	    Random	 random = new Random();
	    for (int i = 0; i < length; i++) {
	        sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                .length())));
	    }

	    return sb.toString();
	}
	
	public void redeemCoupon(int id) {
		rewardCouponTransaction = getAllData(id);
		rewardCouponTransaction.setRedemptionDateTime(DateTimeUtil.getCurrentTimeInUTC());
		rewardCouponTransaction.setStatus(CouponStatus.Redeemed);
		update(rewardCouponTransaction);
	}

}















