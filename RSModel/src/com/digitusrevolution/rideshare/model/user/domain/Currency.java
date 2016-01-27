package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.CurrencyEntity;

public class Currency implements DomainModel{

	private CurrencyEntity entity = new CurrencyEntity();
	private int id;
	private String name;
	private float conversionRate;
	
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
	public CurrencyEntity getEntity() {
		return entity;
	}
	public void setEntity(CurrencyEntity entity) {
		this.entity = entity;
		setDomainModelPrimitiveVariable();
	}
	public float getConversionRate() {
		return conversionRate;
	}
	public void setConversionRate(float conversionRate) {
		this.conversionRate = conversionRate;
		entity.setConversionRate(conversionRate);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		if (!(obj instanceof Currency)) {
			return false;
		}
		Currency other = (Currency) obj;
		if (id != other.id) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		return true;
	}
	@Override
	public void setDomainModelPrimitiveVariable() {
		id = entity.getId();
		name = entity.getName();
		conversionRate = entity.getConversionRate();
	}
}
