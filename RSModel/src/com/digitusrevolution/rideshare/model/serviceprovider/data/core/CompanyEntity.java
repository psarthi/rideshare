package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.core.AccountEntity;

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
	@OneToMany
	@JoinTable(name="company_account",joinColumns=@JoinColumn(name="company_id"))
	private List<AccountEntity> accounts;
	
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
	public List<AccountEntity> getAccounts() {
		return accounts;
	}
	public void setAccounts(List<AccountEntity> accounts) {
		this.accounts = accounts;
	}

}