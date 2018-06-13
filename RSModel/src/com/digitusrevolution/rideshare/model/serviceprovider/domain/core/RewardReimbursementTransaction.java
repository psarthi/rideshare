package com.digitusrevolution.rideshare.model.serviceprovider.domain.core;

import java.util.Collection;
import java.util.HashSet;

import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class RewardReimbursementTransaction extends RewardTransaction {

	private Collection<Photo> photos = new HashSet<Photo>();
	private ReimbursementStatus status;
	
	public Collection<Photo> getPhotos() {
		return photos;
	}
	public void setPhotos(Collection<Photo> photos) {
		this.photos = photos;
	}
	public ReimbursementStatus getStatus() {
		return status;
	}
	public void setStatus(ReimbursementStatus status) {
		this.status = status;
	}
	
}
