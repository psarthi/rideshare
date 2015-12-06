package com.digitusrevolution.rideshare.common.mapper.ride;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class PointMapper implements Mapper<Point, PointEntity>{
	
	@Override
	public Point getDomainModel(PointEntity pointEntity){
		Point point = new Point();
		point.setId(pointEntity.getId());
		point.setLatitude(pointEntity.getLatitude());
		point.setLongitude(pointEntity.getLongitude());
		return point;
	}
	
	@Override
	public PointEntity getEntity(Point point){
		PointEntity pointEntity = new PointEntity();
		pointEntity.setId(point.getId());
		pointEntity.setLatitude(point.getLatitude());
		pointEntity.setLongitude(point.getLongitude());
		return pointEntity;
	}

	@Override
	public PointEntity getEntityChild(Point model, PointEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getDomainModelChild(Point model, PointEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Point> getDomainModels(Collection<PointEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<PointEntity> getEntities(Collection<Point> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
