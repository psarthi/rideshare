package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.FuelEntity;

public class Fuel implements DomainModel{
	
	private FuelEntity entity = new FuelEntity();
	private FuelType type;
	private float price;
	
	public FuelType getType() {
		return type;
	}
	public void setType(FuelType type) {
		this.type = type;
		entity.setType(type);
	}
	public float getPrice() {
		return price;
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
		setDomainModelPrimitiveVariable();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(price);
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
	public void setDomainModelPrimitiveVariable() {
		price = entity.getPrice();
		type = entity.getType();
	}
	
}
