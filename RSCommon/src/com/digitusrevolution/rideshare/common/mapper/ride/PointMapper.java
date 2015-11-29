package com.digitusrevolution.rideshare.common.mapper.ride;

import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class PointMapper {
	
	public Point getPoint(PointEntity pointEntity){
		Point point = new Point();
		point.setId(pointEntity.getId());
		point.setLattitude(pointEntity.getLattitude());
		point.setLongitude(pointEntity.getLongitude());
		return point;
	}
	
	public PointEntity getPointEntity(Point point){
		PointEntity pointEntity = new PointEntity();
		pointEntity.setId(point.getId());
		pointEntity.setLattitude(point.getLattitude());
		pointEntity.setLongitude(point.getLongitude());
		return pointEntity;
	}

}
