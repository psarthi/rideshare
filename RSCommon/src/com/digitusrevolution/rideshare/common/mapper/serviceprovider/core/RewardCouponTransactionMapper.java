package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardCouponTransactionEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;

public class RewardCouponTransactionMapper implements Mapper<RewardCouponTransaction, RewardCouponTransactionEntity> {

	@Override
	public RewardCouponTransactionEntity getEntity(RewardCouponTransaction couponTransaction, boolean fetchChild) {
		RewardCouponTransactionEntity couponTransactionEntity = new RewardCouponTransactionEntity();
		couponTransactionEntity.setId(couponTransaction.getId());
		couponTransactionEntity.setRewardTransactionDateTime(couponTransaction.getRewardTransactionDateTime());
		OfferMapper offerMapper = new OfferMapper();
		couponTransactionEntity.setOffer(offerMapper.getEntity(couponTransaction.getOffer(), fetchChild));
		couponTransactionEntity.setCouponCode(couponTransaction.getCouponCode());
		couponTransactionEntity.setStatus(couponTransaction.getStatus());
		couponTransactionEntity.setRedemptionDateTime(couponTransaction.getRedemptionDateTime());
		couponTransactionEntity.setExpiryDateTime(couponTransaction.getExpiryDateTime());
		
		if (fetchChild) {
			couponTransactionEntity = getEntityChild(couponTransaction, couponTransactionEntity);
		}
		
		return couponTransactionEntity;
	}

	@Override
	public RewardCouponTransactionEntity getEntityChild(RewardCouponTransaction couponTransaction,
			RewardCouponTransactionEntity couponTransactionEntity) {
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as Reward has user and user has reward
		couponTransactionEntity.setUser(userMapper.getEntity(couponTransaction.getUser(), false));
		return couponTransactionEntity;
	}

	@Override
	public RewardCouponTransaction getDomainModel(RewardCouponTransactionEntity couponTransactionEntity, boolean fetchChild) {
		RewardCouponTransaction couponTransaction = new RewardCouponTransaction();
		couponTransaction.setId(couponTransactionEntity.getId());
		couponTransaction.setRewardTransactionDateTime(couponTransactionEntity.getRewardTransactionDateTime());
		OfferMapper offerMapper = new OfferMapper();
		couponTransaction.setOffer(offerMapper.getDomainModel(couponTransactionEntity.getOffer(), fetchChild));
		couponTransaction.setCouponCode(couponTransactionEntity.getCouponCode());
		couponTransaction.setStatus(couponTransactionEntity.getStatus());
		couponTransaction.setRedemptionDateTime(couponTransactionEntity.getRedemptionDateTime());
		couponTransaction.setExpiryDateTime(couponTransactionEntity.getExpiryDateTime());
		
		if (fetchChild) {
			couponTransaction = getDomainModelChild(couponTransaction, couponTransactionEntity);
		}

		return couponTransaction;
	}

	@Override
	public RewardCouponTransaction getDomainModelChild(RewardCouponTransaction couponTransaction,
			RewardCouponTransactionEntity couponTransactionEntity) {
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as Reward has user and user has reward
		couponTransaction.setUser(userMapper.getDomainModel(couponTransactionEntity.getUser(), false));
		return couponTransaction;
	}

	@Override
	public Collection<RewardCouponTransaction> getDomainModels(Collection<RewardCouponTransaction> couponTransactions,
			Collection<RewardCouponTransactionEntity> couponTransactionEntities, boolean fetchChild) {
		for (RewardCouponTransactionEntity couponTransactionEntity: couponTransactionEntities) {
			RewardCouponTransaction couponTransaction = new RewardCouponTransaction();
			couponTransaction = getDomainModel(couponTransactionEntity, fetchChild);
			couponTransactions.add(couponTransaction);
		}
		return couponTransactions;

	}

	@Override
	public Collection<RewardCouponTransactionEntity> getEntities(Collection<RewardCouponTransactionEntity> couponTransactionEntities,
			Collection<RewardCouponTransaction> couponTransactions, boolean fetchChild) {
		for (RewardCouponTransaction couponTransaction: couponTransactions) {
			RewardCouponTransactionEntity couponTransactionEntity = new RewardCouponTransactionEntity();
			couponTransactionEntity = getEntity(couponTransaction, fetchChild);
			couponTransactionEntities.add(couponTransactionEntity);
		}
		return couponTransactionEntities;
	}

}
