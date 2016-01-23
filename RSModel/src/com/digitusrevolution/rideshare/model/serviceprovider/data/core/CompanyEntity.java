package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.user.data.CurrencyEntity;

@Entity
@Table(name="company")
public class CompanyEntity {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	/**
	 * Use inversJoinColumns to change the column name of other entity primary key
	 * @JoinTable(name="company_account",joinColumns=@JoinColumn(name="company_id"),inverseJoinColumns=@JoinColumn(name="account_number"))
	 */
	@OneToOne
	private AccountEntity account;
	@OneToOne
	private CurrencyEntity currency;
	
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
	public AccountEntity getAccount() {
		return account;
	}
	public void setAccount(AccountEntity account) {
		this.account = account;
	}
	public CurrencyEntity getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEntity currency) {
		this.currency = currency;
	}

}
