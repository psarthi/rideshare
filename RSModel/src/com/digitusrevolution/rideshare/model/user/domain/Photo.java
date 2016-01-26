package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.inf.DomainModel;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

public class Photo implements DomainModel{

	private PhotoEntity entity = new PhotoEntity();
	@SuppressWarnings("unused")
	private int id;
	@SuppressWarnings("unused")
	private String imageLocation;
	
	public int getId() {
		return entity.getId();
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	
	public String getImageLocation() {
		return entity.getImageLocation();
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
		entity.setImageLocation(imageLocation);
	}
	public PhotoEntity getEntity() {
		return entity;
	}
	public void setEntity(PhotoEntity entity) {
		this.entity = entity;
	}	
	@Override
	public void setUniqueInstanceVariable() {
		// TODO Auto-generated method stub		
	}

}
