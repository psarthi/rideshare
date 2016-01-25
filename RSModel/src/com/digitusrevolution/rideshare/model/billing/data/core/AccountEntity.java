package com.digitusrevolution.rideshare.model.billing.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;

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
	@Column
	@Enumerated(EnumType.STRING)
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
		if (!(obj instanceof AccountEntity)) {
			return false;
		}
		AccountEntity other = (AccountEntity) obj;
		if (number != other.number) {
			return false;
		}
		if (type != other.type) {
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
	public AccountType getType() {
		return type;
	}
	public void setType(AccountType type) {
		this.type = type;
	}
	
}
