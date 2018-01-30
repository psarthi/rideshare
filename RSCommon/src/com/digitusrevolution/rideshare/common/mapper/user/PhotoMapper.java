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
		//Note - We are only modifying the url while fetching the model and not while setting the entity 
		//as we want to store only uri in the dabase but while fetching we need full url
		if (photoEntity.getImageLocation()!=null) {
			if (photoEntity.getImageLocation().contains("http")) {
				photo.setImageLocation(photoEntity.getImageLocation());
			} else {
				//This is case for group photo where we are not storing hotname 
				//so that we can customize the url at any point of time
				String photoRootUrl = PropertyReader.getInstance().getProperty("PHOTO_WEB_URL");
				String photoFullUrl = photoRootUrl + photoEntity.getImageLocation();
				photo.setImageLocation(photoFullUrl);
			}			
		}
		return photo;
	}

	@Override
	public Photo getDomainModelChild(Photo photo, PhotoEntity photoEntity) {
		return photo;
	}

	@Override
	public Collection<Photo> getDomainModels(Collection<Photo> photos, Collection<PhotoEntity> photoEntities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PhotoEntity> getEntities(Collection<PhotoEntity> photoEntities, Collection<Photo> photos,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
