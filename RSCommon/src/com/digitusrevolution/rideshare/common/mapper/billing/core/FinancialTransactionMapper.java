package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.mapper.user.core.UserMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.FinancialTransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.FinancialTransaction;

public class FinancialTransactionMapper implements Mapper<FinancialTransaction, FinancialTransactionEntity>{

	@Override
	public FinancialTransactionEntity getEntity(FinancialTransaction transaction, boolean fetchChild) {
		FinancialTransactionEntity transactionEntity = new FinancialTransactionEntity();
		transactionEntity.setId(transaction.getId());
		transactionEntity.setAmount(transaction.getAmount());
		transactionEntity.setDateTime(transaction.getDateTime());
		transactionEntity.setPaymentGateway(transaction.getPaymentGateway());
		transactionEntity.setRemark(transaction.getRemark());
		transactionEntity.setStatus(transaction.getStatus());
		transactionEntity.setType(transaction.getType());
		TransactionMapper transactionMapper = new TransactionMapper();
		if (transaction.getWalletTransaction()!=null) {
			transactionEntity.setWalletTransaction(transactionMapper.getEntity(transaction.getWalletTransaction(), fetchChild));
		}
		UserMapper userMapper = new UserMapper();
		transactionEntity.setUser(userMapper.getEntity(transaction.getUser(), false));
		transactionEntity.setPgTransactionStatus(transaction.getPgTransactionStatus());
		transactionEntity.setPgResponseCode(transaction.getPgResponseCode());
		transactionEntity.setPgResponseMsg(transaction.getPgResponseMsg());
		return transactionEntity;
	}

	@Override
	public FinancialTransactionEntity getEntityChild(FinancialTransaction model, FinancialTransactionEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FinancialTransaction getDomainModel(FinancialTransactionEntity transactionEntity, boolean fetchChild) {
		FinancialTransaction transaction = new FinancialTransaction();
		transaction.setId(transactionEntity.getId());
		transaction.setAmount(transactionEntity.getAmount());
		transaction.setDateTime(transactionEntity.getDateTime());
		transaction.setPaymentGateway(transactionEntity.getPaymentGateway());
		transaction.setRemark(transactionEntity.getRemark());
		transaction.setStatus(transactionEntity.getStatus());
		transaction.setType(transactionEntity.getType());
		TransactionMapper transactionMapper = new TransactionMapper();
		if (transactionEntity.getWalletTransaction()!=null) {
			transaction.setWalletTransaction(transactionMapper.getDomainModel(transactionEntity.getWalletTransaction(), fetchChild));	
		}
		UserMapper userMapper = new UserMapper();
		transaction.setUser(userMapper.getDomainModel(transactionEntity.getUser(), false));
		transaction.setPgTransactionStatus(transactionEntity.getPgTransactionStatus());
		transaction.setPgResponseCode(transactionEntity.getPgResponseCode());
		transaction.setPgResponseMsg(transactionEntity.getPgResponseMsg());
		return transaction;
	}

	@Override
	public FinancialTransaction getDomainModelChild(FinancialTransaction model, FinancialTransactionEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<FinancialTransaction> getDomainModels(Collection<FinancialTransaction> transactions,
			Collection<FinancialTransactionEntity> transactionEntities, boolean fetchChild) {
		for (FinancialTransactionEntity transactionEntity : transactionEntities) {
			FinancialTransaction transaction = new FinancialTransaction();
			transaction = getDomainModel(transactionEntity, fetchChild);
			transactions.add(transaction);
		}
		return transactions;

	}

	@Override
	public Collection<FinancialTransactionEntity> getEntities(Collection<FinancialTransactionEntity> transactionEntities,
			Collection<FinancialTransaction> transactions, boolean fetchChild) {
		for (FinancialTransaction transaction : transactions) {
			FinancialTransactionEntity transactionEntity = new FinancialTransactionEntity();
			transactionEntity = getEntity(transaction, fetchChild);
			transactionEntities.add(transactionEntity);
		}
		return transactionEntities;	
	}

}
