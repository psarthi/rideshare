package com.digitusrevolution.rideshare.model.user.data;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="country")
public class CountryEntity {

	@Id
	private String name;
	@OneToMany
	private List<StateEntity> states;
	@Embedded
	@ElementCollection
	@JoinTable(name="country_fuel")
	private List<FuelEntity> fuels;
	@ManyToOne
	private CurrencyEntity currency;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<StateEntity> getStates() {
		return states;
	}
	public void setStates(List<StateEntity> states) {
		this.states = states;
	}
	public List<FuelEntity> getFuels() {
		return fuels;
	}
	public void setFuels(List<FuelEntity> fuels) {
		this.fuels = fuels;
	}
	public CurrencyEntity getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEntity currency) {
		this.currency = currency;
	}
	
}
