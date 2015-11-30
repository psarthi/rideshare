package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.PointMapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class PointDO implements DomainObject{
	
	private Point point;
	private PointEntity pointEntity;
	private PointMapper pointMapper;
	
	public PointDO() {
		point = new Point();
		pointEntity = new PointEntity();
		pointMapper = new PointMapper();
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		pointEntity = pointMapper.getEntity(point);
	}

	public PointEntity getPointEntity() {
		return pointEntity;
	}

	public void setPointEntity(PointEntity pointEntity) {
		this.pointEntity = pointEntity;
		point = pointMapper.getDomainModel(pointEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

}
