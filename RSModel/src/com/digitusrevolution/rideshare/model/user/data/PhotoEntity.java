package com.digitusrevolution.rideshare.model.user.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="photo")
public class PhotoEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private String imageLocation;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getImageLocation() {
		return imageLocation;
	}
	public void setImageLocation(String imageLocation) {
		this.imageLocation = imageLocation;
	}
	
}
