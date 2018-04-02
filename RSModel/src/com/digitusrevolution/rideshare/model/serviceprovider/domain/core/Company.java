package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.Currency;
import com.digitusrevolution.rideshare.model.user.domain.State;

public class Company {
	
	private int id;
	private String name;
	private String address;
	private Country country;
	private State state;
	private Collection<Account> accounts = new HashSet<Account>();
	private Currency currency;
	private float serviceChargePercentage; 
	private float cgstPercentage;
	private float sgstPercentage;
	private float igstPercentage;
	private float tcsPercentage;
	private String gstNumber;
	private String gstCode; 
	private String pan;
	
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
	public Collection<Account> getAccounts() {
		return accounts;
	}
	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}
	public Currency getCurrency() {
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
	public float getServiceChargePercentage() {
		return serviceChargePercentage;
	}
	public void setServiceChargePercentage(float serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}
	
	public Account getAccount(AccountType accountType){
		Collection<Account> accounts = getAccounts();
		for (Account account : accounts) {
			if (account.getType().equals(accountType)){
				return account;
			}
		}
		throw new RuntimeException("No account found for the type:"+accountType); 
	}
	public float getCgstPercentage() {
		return cgstPercentage;
	}
	public void setCgstPercentage(float cgstPercentage) {
		this.cgstPercentage = cgstPercentage;
	}
	public float getSgstPercentage() {
		return sgstPercentage;
	}
	public void setSgstPercentage(float sgstPercentage) {
		this.sgstPercentage = sgstPercentage;
	}
	public float getIgstPercentage() {
		return igstPercentage;
	}
	public void setIgstPercentage(float igstPercentage) {
		this.igstPercentage = igstPercentage;
	}
	public float getTcsPercentage() {
		return tcsPercentage;
	}
	public void setTcsPercentage(float tcsPercentage) {
		this.tcsPercentage = tcsPercentage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getGstNumber() {
		return gstNumber;
	}
	public void setGstNumber(String gstNumber) {
		this.gstNumber = gstNumber;
	}
	public String getGstCode() {
		return gstCode;
	}
	public void setGstCode(String gstCode) {
		this.gstCode = gstCode;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Country getCountry() {
		return country;
	}
	public void setCountry(Country country) {
		this.country = country;
	}

	
}
