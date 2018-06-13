package com.digitusrevolution.rideshare.model.serviceprovider.data.core;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.JoinColumn;

import com.digitusrevolution.rideshare.model.billing.data.core.AccountEntity;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Offer;
import com.digitusrevolution.rideshare.model.serviceprovider.domain.core.Partner;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.data.CurrencyEntity;
import com.digitusrevolution.rideshare.model.user.data.StateEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;
import com.digitusrevolution.rideshare.model.user.domain.Country;
import com.digitusrevolution.rideshare.model.user.domain.State;

@Entity
@Table(name="company")
public class CompanyEntity {
	
	@Id
	@GeneratedValue
	private int id;
	private String name;
	private String address;
	@OneToOne
	private CountryEntity country;
	@OneToOne
	private StateEntity state;
	/**
	 * Use inversJoinColumns to change the column name of other entity primary key
	 * @JoinTable(name="company_account",joinColumns=@JoinColumn(name="company_id"),inverseJoinColumns=@JoinColumn(name="account_number"))
	 */
	@OneToMany
	@JoinTable(name="company_account",joinColumns=@JoinColumn(name="company_id"))
	private Collection<AccountEntity> accounts = new HashSet<AccountEntity>();
	@OneToOne
	private CurrencyEntity currency;
	private float serviceChargePercentage;
	private float cgstPercentage;
	private float sgstPercentage;
	private float igstPercentage;
	private float tcsPercentage;
	private String gstNumber;
	private String gstCode; 
	private String pan;
	@OneToMany
	@JoinTable(name="company_operating_city",joinColumns=@JoinColumn(name="company_id"),inverseJoinColumns=@JoinColumn(name="city_id"))
	private Collection<CityEntity> operatingCities = new HashSet<CityEntity>();
	@OneToMany
	@JoinTable(name="company_partner",joinColumns=@JoinColumn(name="company_id"),inverseJoinColumns=@JoinColumn(name="partner_id"))
	private Collection<PartnerEntity> partners = new HashSet<PartnerEntity>();
	@OneToMany
	@JoinTable(name="company_offer",joinColumns=@JoinColumn(name="company_id"),inverseJoinColumns=@JoinColumn(name="offer_id"))
	private Collection<OfferEntity> offers = new HashSet<OfferEntity>();

	
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
	public Collection<AccountEntity> getAccounts() {
		return accounts;
	}
	public void setAccounts(Collection<AccountEntity> accounts) {
		this.accounts = accounts;
	}
	public CurrencyEntity getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEntity currency) {
		this.currency = currency;
	}
	public float getServiceChargePercentage() {
		return serviceChargePercentage;
	}
	public void setServiceChargePercentage(float serviceChargePercentage) {
		this.serviceChargePercentage = serviceChargePercentage;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
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
	public StateEntity getState() {
		return state;
	}
	public void setState(StateEntity state) {
		this.state = state;
	}
	public CountryEntity getCountry() {
		return country;
	}
	public void setCountry(CountryEntity country) {
		this.country = country;
	}
	public Collection<CityEntity> getOperatingCities() {
		return operatingCities;
	}
	public void setOperatingCities(Collection<CityEntity> operatingCities) {
		this.operatingCities = operatingCities;
	}
	public Collection<PartnerEntity> getPartners() {
		return partners;
	}
	public void setPartners(Collection<PartnerEntity> partners) {
		this.partners = partners;
	}
	public Collection<OfferEntity> getOffers() {
		return offers;
	}
	public void setOffers(Collection<OfferEntity> offers) {
		this.offers = offers;
	}
	

}
