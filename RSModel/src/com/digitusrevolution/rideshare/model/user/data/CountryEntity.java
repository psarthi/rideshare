package com.digitusrevolution.rideshare.model.user.data;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.ride.domain.core.RideMode;

@Entity
@Table(name="country")
public class CountryEntity {

	@Id
	private String name;
	@OneToMany (cascade=CascadeType.ALL)
	@JoinTable(name="country_state",joinColumns=@JoinColumn(name="country_name"))
	private Collection<StateEntity> states = new HashSet<StateEntity>();
	@Embedded
	@ElementCollection
	@JoinTable(name="country_fuel",joinColumns=@JoinColumn(name="country_name"))
	private Collection<FuelEntity> fuels = new HashSet<FuelEntity>();
	@ManyToOne (cascade=CascadeType.ALL)
	private CurrencyEntity currency;
	private String code;
	@Enumerated(EnumType.STRING)
	private RideMode rideMode;
	
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public RideMode getRideMode() {
		return rideMode;
	}
	public void setRideMode(RideMode rideMode) {
		this.rideMode = rideMode;
	}

	
}
