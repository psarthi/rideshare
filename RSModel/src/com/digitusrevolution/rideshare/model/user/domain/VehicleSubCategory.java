package com.digitusrevolution.rideshare.model.user.domain;

public class VehicleSubCategory {
	
	private int id;
	private String name;
	private Boolean airConditioner;
	private int averageMileage;
	private FuelType fuelType;
	
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
	public Boolean getAirConditioner() {
		return airConditioner;
	}
	public void setAirConditioner(Boolean airConditioner) {
		this.airConditioner = airConditioner;
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
		if (!(obj instanceof VehicleSubCategory)) {
			return false;
		}
		VehicleSubCategory other = (VehicleSubCategory) obj;
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
	public int getAverageMileage() {
		return averageMileage;
	}
	public void setAverageMileage(int averageMileage) {
		this.averageMileage = averageMileage;
	}
	public FuelType getFuelType() {
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
	}

	
}
