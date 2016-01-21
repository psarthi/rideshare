package com.digitusrevolution.rideshare.model.user.domain;

public class Fuel {
	
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
		if (!(obj instanceof Fuel)) {
			return false;
		}
		Fuel other = (Fuel) obj;
		if (price != other.price) {
			return false;
		}
		if (type != other.type) {
			return false;
		}
		return true;
	}
	
}
