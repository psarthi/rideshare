package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.digitusrevolution.rideshare.model.user.domain.FuelType;

@Embeddable
public class FuelEntity {
	
	@Column
	@Enumerated(EnumType.STRING)
	private FuelType type;
	private int price;
	
	public FuelType getType() {
		return type;
	}
	public void setType(FuelType type) {
		this.type = type;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + price;
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
		if (!(obj instanceof FuelEntity)) {
			return false;
		}
		FuelEntity other = (FuelEntity) obj;
		if (price != other.price) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
}
