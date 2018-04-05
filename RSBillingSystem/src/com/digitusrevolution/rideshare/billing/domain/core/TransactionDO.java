package com.digitusrevolution.rideshare.billing.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKLong;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.billing.core.TransactionMapper;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Transaction;

public class TransactionDO implements DomainObjectPKLong<Transaction>{
	
	private Transaction transaction;
	private TransactionEntity transactionEntity;
	private final GenericDAO<TransactionEntity, Long> genericDAO;
	private TransactionMapper transactionMapper;
	private static final Logger logger = LogManager.getLogger(TransactionDO.class.getName());


	public TransactionDO() {
		transaction = new Transaction();
		transactionEntity = new TransactionEntity();
		genericDAO = new GenericDAOImpl<>(TransactionEntity.class);
		transactionMapper = new TransactionMapper();
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
		transactionEntity = transactionMapper.getEntity(transaction, true);
	}

	public void setTransactionEntity(TransactionEntity transactionEntity) {
		this.transactionEntity = transactionEntity;
		transaction = transactionMapper.getDomainModel(transactionEntity, false);
	}

	@Override
	public List<Transaction> getAll() {
		List<Transaction> transactions = new ArrayList<>();
		List<TransactionEntity> transactionEntities = genericDAO.getAll();
		for (TransactionEntity transactionEntity : transactionEntities) {
			setTransactionEntity(transactionEntity);
			transactions.add(transaction);
		}
		return transactions;
	}

	@Override
	public void update(Transaction transaction) {
		if (transaction.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+transaction.getId());
		}
		setTransaction(transaction);
		genericDAO.update(transactionEntity);
	}

	@Override
	public void fetchChild() {
		transaction = transactionMapper.getDomainModelChild(transaction, transactionEntity);
	}

	@Override
	public long create(Transaction transaction) {
		setTransaction(transaction);
		long id = genericDAO.create(transactionEntity);
		return id;
	}

	@Override
	public Transaction get(long number) {
		transactionEntity = genericDAO.get(number);
		if (transactionEntity == null){
			throw new NotFoundException("No Data found with number: "+number);
		}
		setTransactionEntity(transactionEntity);
		return transaction;
	}

	@Override
	public Transaction getAllData(long number) {
		get(number);
		fetchChild();
		return transaction;
	}

	@Override
	public void delete(long number) {
		transaction = get(number);
		setTransaction(transaction);
		genericDAO.delete(transactionEntity);
	}

}
