package com.digitusrevolution.rideshare.ride.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.ride.PointMapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;

public class PointDO implements DomainObject{
	
	private Point point;
	private PointEntity pointEntity;
	
	public PointDO() {
		point = new Point();
		pointEntity = new PointEntity();
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		mapDomainModelToDataModel();
	}

	public PointEntity getPointEntity() {
		return pointEntity;
	}

	public void setPointEntity(PointEntity pointEntity) {
		this.pointEntity = pointEntity;
		mapDataModelToDomainModel();
	}

	@Override
	public void mapDomainModelToDataModel() {
		PointMapper pointMapper = new PointMapper();
		pointEntity = pointMapper.getPointEntity(point);	
	}

	@Override
	public void mapDataModelToDomainModel() {
		PointMapper pointMapper = new PointMapper();
		point = pointMapper.getPoint(pointEntity);		
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}

}
