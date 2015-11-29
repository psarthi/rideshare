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
	private int id;
	@ManyToOne
	private VehicleCategoryEntity vehicleCategory;
	@ManyToOne
	private VehicleSubCategoryEntity vehicleSubCategory;
	@OneToOne(cascade=CascadeType.ALL)
	private PhotoEntity photo;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

}
