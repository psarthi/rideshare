package com.digitusrevolution.rideshare.common.mapper.serviceprovider.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.common.mapper.user.PhotoMapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardCouponTransactionEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.RewardReimbursementTransactionEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardCouponTransaction;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.RewardReimbursementTransaction;

public class RewardReimbursementTransactionMapper implements Mapper<RewardReimbursementTransaction, RewardReimbursementTransactionEntity> {

	@Override
	public RewardReimbursementTransactionEntity getEntity(RewardReimbursementTransaction reimbursementTransaction, boolean fetchChild) {
		RewardReimbursementTransactionEntity reimbursementTransactionEntity = new RewardReimbursementTransactionEntity();
		reimbursementTransactionEntity.setId(reimbursementTransaction.getId());
		reimbursementTransactionEntity.setRewardTransactionDateTime(reimbursementTransaction.getRewardTransactionDateTime());
		OfferMapper offerMapper = new OfferMapper();
		reimbursementTransactionEntity.setOffer(offerMapper.getEntity(reimbursementTransaction.getOffer(), fetchChild));
		PhotoMapper photoMapper = new PhotoMapper();
		reimbursementTransactionEntity.setPhotos(photoMapper.getEntities(reimbursementTransactionEntity.getPhotos(), reimbursementTransaction.getPhotos(), fetchChild));
		reimbursementTransactionEntity.setStatus(reimbursementTransaction.getStatus());
		reimbursementTransactionEntity.setApprovedAmount(reimbursementTransaction.getApprovedAmount());
		reimbursementTransactionEntity.setRemarks(reimbursementTransaction.getRemarks());
		TransactionMapper transactionMapper = new TransactionMapper();
		if (reimbursementTransaction.getTransaction()!=null) reimbursementTransactionEntity.setTransaction(transactionMapper.getEntity(reimbursementTransaction.getTransaction(), fetchChild));
		
		if (fetchChild) {
			reimbursementTransactionEntity = getEntityChild(reimbursementTransaction, reimbursementTransactionEntity);
		}
		
		return reimbursementTransactionEntity;
	}

	@Override
	public RewardReimbursementTransactionEntity getEntityChild(RewardReimbursementTransaction reimbursementTransaction,
			RewardReimbursementTransactionEntity reimbursementTransactionEntity) {
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as Reward has user and user has reward
		reimbursementTransactionEntity.setUser(userMapper.getEntity(reimbursementTransaction.getUser(), false));

		return reimbursementTransactionEntity;
	}

	@Override
	public RewardReimbursementTransaction getDomainModel(RewardReimbursementTransactionEntity reimbursementTransactionEntity,
			boolean fetchChild) {
		RewardReimbursementTransaction reimbursementTransaction = new RewardReimbursementTransaction();
		reimbursementTransaction.setId(reimbursementTransactionEntity.getId());
		reimbursementTransaction.setRewardTransactionDateTime(reimbursementTransactionEntity.getRewardTransactionDateTime());
		OfferMapper offerMapper = new OfferMapper();
		reimbursementTransaction.setOffer(offerMapper.getDomainModel(reimbursementTransactionEntity.getOffer(), fetchChild));
		PhotoMapper photoMapper = new PhotoMapper();
		reimbursementTransaction.setPhotos(photoMapper.getDomainModels(reimbursementTransaction.getPhotos(), reimbursementTransactionEntity.getPhotos(), fetchChild));
		reimbursementTransaction.setStatus(reimbursementTransactionEntity.getStatus());
		reimbursementTransaction.setApprovedAmount(reimbursementTransactionEntity.getApprovedAmount());
		reimbursementTransaction.setRemarks(reimbursementTransactionEntity.getRemarks());
		TransactionMapper transactionMapper = new TransactionMapper();
		if (reimbursementTransactionEntity.getTransaction()!=null) reimbursementTransaction.setTransaction(transactionMapper.getDomainModel(reimbursementTransactionEntity.getTransaction(), fetchChild));
		
		if (fetchChild) {
			reimbursementTransaction = getDomainModelChild(reimbursementTransaction, reimbursementTransactionEntity);
		}
		
		return reimbursementTransaction;
		
	}

	@Override
	public RewardReimbursementTransaction getDomainModelChild(RewardReimbursementTransaction reimbursementTransaction,
			RewardReimbursementTransactionEntity reimbursementTransactionEntity) {
		UserMapper userMapper = new UserMapper();
		//Don't fetch child as Reward has user and user has reward
		reimbursementTransaction.setUser(userMapper.getDomainModel(reimbursementTransactionEntity.getUser(), false));
		return reimbursementTransaction;
	}

	@Override
	public Collection<RewardReimbursementTransaction> getDomainModels(Collection<RewardReimbursementTransaction> reimbursementTransactions,
			Collection<RewardReimbursementTransactionEntity> reimbursementTransactionEntities, boolean fetchChild) {
		for (RewardReimbursementTransactionEntity reimbursementTransactionEntity: reimbursementTransactionEntities) {
			RewardReimbursementTransaction reimbursementTransaction = new RewardReimbursementTransaction();
			reimbursementTransaction = getDomainModel(reimbursementTransactionEntity, fetchChild);
			reimbursementTransactions.add(reimbursementTransaction);
		}
		return reimbursementTransactions;
	}

	@Override
	public Collection<RewardReimbursementTransactionEntity> getEntities(
			Collection<RewardReimbursementTransactionEntity> reimbursementTransactionEntities,
			Collection<RewardReimbursementTransaction> reimbursementTransactions, boolean fetchChild) {
		for (RewardReimbursementTransaction reimbursementTransaction: reimbursementTransactions) {
			RewardReimbursementTransactionEntity reimbursementTransactionEntity = new RewardReimbursementTransactionEntity();
			reimbursementTransactionEntity = getEntity(reimbursementTransaction, fetchChild);
			reimbursementTransactionEntities.add(reimbursementTransactionEntity);
		}
		return reimbursementTransactionEntities;
	}

}
