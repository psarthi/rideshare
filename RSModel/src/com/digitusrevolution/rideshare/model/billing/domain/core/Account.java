package com.digitusrevolution.rideshare.model.billing.domain.core;

public class Account {
	
	private int number;
	private float balance;
	
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
		return true;
	}
	
}
