package com.digitusrevolution.rideshare.common.mapper.billing.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;

public class TransactionMapper implements Mapper<Transaction, TransactionEntity>{

	@Override
	public TransactionEntity getEntity(Transaction transaction, boolean fetchChild) {
		TransactionEntity transactionEntity = new TransactionEntity();
		transactionEntity.setId(transaction.getId());
		transactionEntity.setDateTime(transaction.getDateTime());
		transactionEntity.setAmount(transaction.getAmount());
		transactionEntity.setType(transaction.getType());
		RemarkMapper remarkMapper = new RemarkMapper();
		transactionEntity.setRemark(remarkMapper.getEntity(transaction.getRemark(), fetchChild));
		AccountMapper accountMapper = new AccountMapper();
		//IMP - Don't fetch child as transaction has account and account has transaction
		transactionEntity.setAccount(accountMapper.getEntity(transaction.getAccount(), false));
		return transactionEntity;
	}

	@Override
	public TransactionEntity getEntityChild(Transaction transaction, TransactionEntity transactionEntity) {
		return transactionEntity;
	}

	@Override
	public Transaction getDomainModel(TransactionEntity transactionEntity, boolean fetchChild) {
		Transaction transaction = new Transaction();
		transaction.setId(transactionEntity.getId());
		transaction.setDateTime(transactionEntity.getDateTime());
		transaction.setAmount(transactionEntity.getAmount());
		transaction.setType(transactionEntity.getType());
		RemarkMapper remarkMapper = new RemarkMapper();
		transaction.setRemark(remarkMapper.getDomainModel(transactionEntity.getRemark(), fetchChild));
		AccountMapper accountMapper = new AccountMapper();
		//IMP - Don't fetch child as transaction has account and account has transaction
		transaction.setAccount(accountMapper.getDomainModel(transactionEntity.getAccount(), false));
		return transaction;
	}

	@Override
	public Transaction getDomainModelChild(Transaction transaction, TransactionEntity transactionEntity) {
		return transaction;
	}

	@Override
	public Collection<Transaction> getDomainModels(Collection<Transaction> transactions,
			Collection<TransactionEntity> transactionEntities, boolean fetchChild) {
	
		for (TransactionEntity transactionEntity : transactionEntities) {
			Transaction transaction = new Transaction();
			transaction = getDomainModel(transactionEntity, fetchChild);
			transactions.add(transaction);
		}
		return transactions;
	}

	@Override
	public Collection<TransactionEntity> getEntities(Collection<TransactionEntity> transactionEntities,
			Collection<Transaction> transactions, boolean fetchChild) {

		for (Transaction transaction : transactions) {
			TransactionEntity transactionEntity = new TransactionEntity();
			transactionEntity = getEntity(transaction, fetchChild);
			transactionEntities.add(transactionEntity);
		}
		return transactionEntities;
	}

}
