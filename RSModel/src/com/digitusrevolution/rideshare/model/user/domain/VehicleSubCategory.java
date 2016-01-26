package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;

public class VehicleSubCategory{
	
	private VehicleSubCategoryEntity entity = new VehicleSubCategoryEntity();
	private int id;
	private String name;
	private Boolean airConditioner;
	private int averageMileage;
	private FuelType fuelType;
	
	public int getId() {
		id = entity.getId();
		return id; 
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	public String getName() {
		name = entity.getName();
		return name;
	}
	public void setName(String name) {
		this.name = name;
		entity.setName(name);
	}
	public Boolean getAirConditioner() {
		airConditioner = entity.getAirConditioner();
		return airConditioner;
	}
	public void setAirConditioner(Boolean airConditioner) {
		this.airConditioner = airConditioner;
		entity.setAirConditioner(airConditioner);
	}
	public int getAverageMileage() {
		averageMileage = entity.getAverageMileage();
		return averageMileage;
	}
	public void setAverageMileage(int averageMileage) {
		this.averageMileage = averageMileage;
		entity.setAverageMileage(averageMileage);
	}
	public FuelType getFuelType() {
		fuelType = entity.getFuelType();
		return fuelType;
	}
	public void setFuelType(FuelType fuelType) {
		this.fuelType = fuelType;
		entity.setFuelType(fuelType);
	}

	public VehicleSubCategoryEntity getEntity() {
		return entity;
	}

	public void setEntity(VehicleSubCategoryEntity entity) {
		this.entity = entity;
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
}
