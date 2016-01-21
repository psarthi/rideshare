package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.domain.FuelType;

@Entity
@Table(name="vehicle_sub_category")
public class VehicleSubCategoryEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	private String name;
	private Boolean airConditioner;
	private int averageMileage;
	@Column
	@Enumerated(EnumType.STRING)
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
		if (!(obj instanceof VehicleSubCategoryEntity)) {
			return false;
		}
		VehicleSubCategoryEntity other = (VehicleSubCategoryEntity) obj;
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
