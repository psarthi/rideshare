package com.digitusrevolution.rideshare.model.user.data;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
	@JoinTable(name="country_state",joinColumns=@JoinColumn(name="country_name"))
	private Collection<StateEntity> states = new HashSet<StateEntity>();
	@Embedded
	@ElementCollection
	@JoinTable(name="country_fuel",joinColumns=@JoinColumn(name="country_name"))
	private Collection<FuelEntity> fuels = new HashSet<FuelEntity>();
	@ManyToOne
	private CurrencyEntity currency;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public CurrencyEntity getCurrency() {
		return currency;
	}
	public void setCurrency(CurrencyEntity currency) {
		this.currency = currency;
	}
	public Collection<StateEntity> getStates() {
		return states;
	}
	public void setStates(Collection<StateEntity> states) {
		this.states = states;
	}
	public Collection<FuelEntity> getFuels() {
		return fuels;
	}
	public void setFuels(Collection<FuelEntity> fuels) {
		this.fuels = fuels;
	}
	
}
