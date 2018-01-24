package com.digitusrevolution.rideshare.model.user.data.core;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleCategoryEntity;
import com.digitusrevolution.rideshare.model.user.data.VehicleSubCategoryEntity;

@Entity
@Table(name="vehicle")
public class VehicleEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String registrationNumber;
	private String model;
	private int seatCapacity;
	private int smallLuggageCapacity;
	@ManyToOne
	private VehicleCategoryEntity vehicleCategory;
	@ManyToOne
	private VehicleSubCategoryEntity vehicleSubCategory;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public int getSeatCapacity() {
		return seatCapacity;
	}

	public void setSeatCapacity(int seatCapacity) {
		this.seatCapacity = seatCapacity;
	}

	public int getSmallLuggageCapacity() {
		return smallLuggageCapacity;
	}

	public void setSmallLuggageCapacity(int smallLuggageCapacity) {
		this.smallLuggageCapacity = smallLuggageCapacity;
	}

	public VehicleCategoryEntity getVehicleCategory() {
		return vehicleCategory;
	}

	public void setVehicleCategory(VehicleCategoryEntity vehicleCategory) {
		this.vehicleCategory = vehicleCategory;
	}

	public VehicleSubCategoryEntity getVehicleSubCategory() {
		return vehicleSubCategory;
	}

	public void setVehicleSubCategory(VehicleSubCategoryEntity vehicleSubCategory) {
		this.vehicleSubCategory = vehicleSubCategory;
	}

	public PhotoEntity getPhoto() {
		return photo;
	}

	public void setPhoto(PhotoEntity photo) {
		this.photo = photo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
		if (!(obj instanceof VehicleEntity)) {
			return false;
		}
		VehicleEntity other = (VehicleEntity) obj;
		if (id != other.id) {
			return false;
		}
		return true;
	}

}
