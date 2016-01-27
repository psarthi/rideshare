package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.billing.domain.core.Account;
import com.digitusrevolution.rideshare.model.billing.domain.core.AccountType;
import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.serviceprovider.data.core.CompanyEntity;
import com.digitusrevolution.rideshare.model.user.domain.Currency;

public class Company implements DomainModel{

	private CompanyEntity entity = new CompanyEntity();
	private int id;
	private String name;
	private Collection<Account> accounts = new HashSet<Account>();
	private Currency currency = new Currency();
	private float serviceChargePercentage; 

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}
	public Collection<Account> getAccounts() {
		if (accounts.isEmpty()){
			Collection<AccountEntity> accountEntities = entity.getAccounts();
			for (AccountEntity accountEntity : accountEntities) {
				Account account = new Account();
				account.setEntity(accountEntity);
				accounts.add(account);
			}
		}
		return accounts;
	}
	public void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
		entity.getAccounts().clear();
		for (Account account : accounts) {
			entity.getAccounts().add(account.getEntity());
		}
	}
	
	public void addAccount(Account account){
		accounts.add(account);
		entity.getAccounts().add(account.getEntity());
	}
	
	public Currency getCurrency() {
		currency.setEntity(entity.getCurrency());
		return currency;
	}
	public void setCurrency(Currency currency) {
		this.currency = currency;
		entity.setCurrency(currency.getEntity());
	}
	public float getServiceChargePercentage() {
		return serviceChargePercentage;
	}
	public void setServiceChargePercentage(float serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
		entity.setServiceChargePercentage(serviceChargePercentage);
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
	public CompanyEntity getEntity() {
		return entity;
	}
	public void setEntity(CompanyEntity entity) {
		this.entity = entity;
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		id = entity.getId();
		name = entity.getName();		
		serviceChargePercentage = entity.getServiceChargePercentage();
	}
	@Override
	public void fetchReferenceVariable() {
		// TODO Auto-generated method stub
		
	}

}
