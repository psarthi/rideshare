package com.digitusrevolution.rideshare.model.billing.domain.core;

import java.time.Instant;
import java.time.ZonedDateTime;

public class Transaction implements Comparable<Transaction>{
	
	private long id;
	private ZonedDateTime dateTime;
	private TransactionType type;
	private float amount;
	private Remark remark;
	private Account account;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public ZonedDateTime getDateTime() {
		return dateTime;
	}
	public void setDateTime(ZonedDateTime dateTime) {
		this.dateTime = dateTime;
	}
	public TransactionType getType() {
		return type;
	}
	public void setType(TransactionType type) {
		this.type = type;
	}
	public float getAmount() {
		return amount;
	}
	public void setAmount(float amount) {
		this.amount = amount;
	}
	public Remark getRemark() {
		return remark;
	}
	public void setRemark(Remark remark) {
		this.remark = remark;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof Transaction)) {
			return false;
		}
		Transaction other = (Transaction) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}
	@Override
	public int compareTo(Transaction transaction) {
		//Negative number is desc order, positive is asc order
		//return transaction.getDateTime().getNano() - this.getDateTime().getNano();
		//Unable to compare with date somehow so comparing based on Id which should be changed to datetime
		return Long.compare(transaction.getId(),this.getId());
	}
	public Account getAccount() {
		return account;
	}
	public void setAccount(Account account) {
		this.account = account;
	}

}
