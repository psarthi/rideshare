package com.digitusrevolution.rideshare.mode.serviceprovider.domain.core;

import java.util.List;

import com.digitusrevolution.rideshare.model.user.domain.core.Account;

public class Company {
	
	private int id;
	private String name;
	private List<Account> accounts;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<Account> accounts) {
		this.accounts = accounts;
	}

}
