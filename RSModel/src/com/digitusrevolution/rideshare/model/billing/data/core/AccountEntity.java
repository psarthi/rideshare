package com.digitusrevolution.rideshare.model.billing.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class AccountEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int number;
	private float balance;
	@OneToMany (cascade=CascadeType.ALL)
	@JoinTable(name="account_transaction",joinColumns=@JoinColumn(name="account_number"))
	private Collection<TransactionEntity> transactions = new HashSet<TransactionEntity>();
	
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
		if (!(obj instanceof AccountEntity)) {
			return false;
		}
		AccountEntity other = (AccountEntity) obj;
		if (Float.floatToIntBits(balance) != Float.floatToIntBits(other.balance)) {
			return false;
		}
		if (number != other.number) {
			return false;
		}
		return true;
	}
	public Collection<TransactionEntity> getTransactions() {
		return transactions;
	}
	public void setTransactions(Collection<TransactionEntity> transactions) {
		this.transactions = transactions;
	}
	
}
