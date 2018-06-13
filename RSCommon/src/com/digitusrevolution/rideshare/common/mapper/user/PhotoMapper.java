package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.common.util.PropertyReader;
import com.digitusrevolution.rideshare.model.user.data.PhotoEntity;
import com.digitusrevolution.rideshare.model.user.domain.Photo;

public class PhotoMapper implements Mapper<Photo, PhotoEntity>{

	@Override
	public PhotoEntity getEntity(Photo photo, boolean fetchChild) {
		PhotoEntity photoEntity = new PhotoEntity();
		photoEntity.setId(photo.getId());
		photoEntity.setImageLocation(photo.getImageLocation());
		return photoEntity;
	}

	@Override
	public PhotoEntity getEntityChild(Photo photo, PhotoEntity photoEntity) {
		return photoEntity;
	}

	@Override
	public Photo getDomainModel(PhotoEntity photoEntity, boolean fetchChild) {
		Photo photo = new Photo();
		photo.setId(photoEntity.getId());
		photo.setImageLocation(photoEntity.getImageLocation());
		return photo;
	}

	@Override
	public Photo getDomainModelChild(Photo photo, PhotoEntity photoEntity) {
		return photo;
	}

	@Override
	public Collection<Photo> getDomainModels(Collection<Photo> photos, Collection<PhotoEntity> photoEntities,
			boolean fetchChild) {
		for (PhotoEntity photoEntity: photoEntities) {
			Photo photo = new Photo();
			photo = getDomainModel(photoEntity, fetchChild);
			photos.add(photo);
		}
		return photos;
	}

	@Override
	public Collection<PhotoEntity> getEntities(Collection<PhotoEntity> photoEntities, Collection<Photo> photos,
			boolean fetchChild) {
		for (Photo photo: photos) {
			PhotoEntity photoEntity = new PhotoEntity();
			photoEntity = getEntity(photo, fetchChild);
			photoEntities.add(photoEntity);
		}
		return photoEntities;
	}

}
