package com.digitusrevolution.rideshare.model.billing.data.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="account")
public class AccountEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int number;
	private int balance;
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getBalance() {
		return balance;
	}
	public void setBalance(int balance) {
		this.balance = balance;
	}
}
