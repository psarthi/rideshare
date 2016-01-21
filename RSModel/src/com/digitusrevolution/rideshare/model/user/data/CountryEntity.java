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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((states == null) ? 0 : states.hashCode());
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
		if (!(obj instanceof CountryEntity)) {
			return false;
		}
		CountryEntity other = (CountryEntity) obj;
		if (currency == null) {
			if (other.currency != null) {
				return false;
			}
		} else if (!currency.equals(other.currency)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (states == null) {
			if (other.states != null) {
				return false;
			}
		} else if (!states.equals(other.states)) {
			return false;
		}
		return true;
	}
	
}
