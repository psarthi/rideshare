package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.FuelEntity;

public class Fuel implements DomainModel{
	
	private FuelEntity entity = new FuelEntity();
	private FuelType type;
	private float price;
	
	public FuelType getType() {
		return entity.getType();
	}
	public void setType(FuelType type) {
		this.type = type;
		entity.setType(type);
	}
	public float getPrice() {
		return entity.getPrice();
	}
	public void setPrice(float price) {
		this.price = price;
		entity.setPrice(price);
	}
	public FuelEntity getEntity() {
		return entity;
	}
	public void setEntity(FuelEntity entity) {
		this.entity = entity;
	}
	@Override
	public int hashCode() {
		setUniqueInstanceVariable();
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		setUniqueInstanceVariable();
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Fuel)) {
			return false;
		}
		Fuel other = (Fuel) obj;
		if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price)) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	@Override
	public void setUniqueInstanceVariable() {
		price = getPrice();
		type = getType();
	}

	
}
