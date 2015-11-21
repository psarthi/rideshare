package com.digitusrevolution.rideshare.model.user.data;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="vehicle_category")
public class VehicleCategoryEntity {
	@Id
	@GeneratedValue
	private int id;
	private String name;
	@OneToMany
	@JoinTable(name="vehicle_category_subCategory",joinColumns=@JoinColumn(name="vehicle_category_id"))
	private List<VehicleSubCategoryEntity> subCategories;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<VehicleSubCategoryEntity> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<VehicleSubCategoryEntity> subCategories) {
		this.subCategories = subCategories;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

}