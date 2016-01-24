package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.util.Collection;
import java.util.HashSet;

public class Account {
	
	private int number;
	private float balance;
	private Collection<Transaction> transactions = new HashSet<Transaction>();
	private AccountType type;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float getBalance() {
		return balance;
	}
	public void setBalance(float balance) {
		this.balance = balance;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(balance);
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
		if (Float.floatToIntBits(balance) != Float.floatToIntBits(other.balance)) {
			return false;
		}
		if (number != other.number) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	public Collection<Transaction> getTransactions() {
		return transactions;
	}
	public void setTransactions(Collection<Transaction> transactions) {
		this.transactions = transactions;
	}
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	
}
