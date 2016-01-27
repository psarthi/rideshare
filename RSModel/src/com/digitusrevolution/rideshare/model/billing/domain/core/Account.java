package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.data.core.TransactionEntity;
import com.digitusrevolution.rideshare.model.inf.DomainModel;

public class Account implements DomainModel{

	private AccountEntity entity = new AccountEntity();
	private int number;
	private float balance;
	private Collection<Transaction> transactions = new HashSet<Transaction>();
	private AccountType type;

	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
		entity.setNumber(number);
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
		entity.setBalance(balance);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Account)) {
			return false;
		}
		Account other = (Account) obj;
		if (number != other.number) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	public Collection<Transaction> getTransactions() {
		if (transactions.isEmpty()){
			Collection<TransactionEntity> transactionEntities = entity.getTransactions();
			for (TransactionEntity transactionEntity : transactionEntities) {
				Transaction transaction = new Transaction();
				transaction.setEntity(transactionEntity);
				transactions.add(transaction);
			}
		}
		return transactions;
	}
	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
		entity.getTransactions().clear();
		for (Transaction transaction : transactions) {
			entity.getTransactions().add(transaction.getEntity());
		}
	}
	public void addTransaction(Transaction transaction) {
		transactions.add(transaction);
		entity.getTransactions().add(transaction.getEntity());
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
		entity.setType(type);
	}
	public AccountEntity getEntity() {
		return entity;
	}
	public void setEntity(AccountEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		number = entity.getNumber();
		balance = entity.getBalance();
		type = entity.getType();
	}
	@Override
	public void fetchReferenceVariable() {
		// TODO Auto-generated method stub
		
	}

}
