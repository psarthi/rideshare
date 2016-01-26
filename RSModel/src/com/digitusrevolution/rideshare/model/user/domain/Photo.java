package com.digitusrevolution.rideshare.model.user.domain;

import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;

public class Photo{

	private PhotoEntity entity = new PhotoEntity();
	private int id;
	private String imageLocation;
	
	public int getId() {
		id = entity.getId();
		return id;
	}
	public void setId(int id) {
		this.id = id;
		entity.setId(id);
	}
	
	public String getImageLocation() {
		imageLocation = entity.getImageLocation();
		return imageLocation;
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
}
