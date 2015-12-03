package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.ride.PointMapper;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;


public class PointDO implements DomainObjectPKInteger<Point>{
	
	private Point point;
	private PointEntity pointEntity;
	private PointMapper pointMapper;
	private final GenericDAO<PointEntity, Integer> genericDAO;
	private static final Logger logger = LogManager.getLogger(PointDO.class.getName());

	
	public PointDO() {
		point = new Point();
		pointEntity = new PointEntity();
		pointMapper = new PointMapper();
		genericDAO = new GenericDAOImpl<>(PointEntity.class);
	}

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
		pointEntity = pointMapper.getEntity(point);
	}

	private void setPointEntity(PointEntity pointEntity) {
		this.pointEntity = pointEntity;
		point = pointMapper.getDomainModel(pointEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Point> getAll() {
		List<Point> points = new ArrayList<>();
		List<PointEntity> pointEntities = genericDAO.getAll();
		for (PointEntity pointEntity : pointEntities) {
			setPointEntity(pointEntity);
			points.add(point);
		}
		return points;
	}

	@Override
	public void update(Point point) {
		setPoint(point);
		genericDAO.update(pointEntity);		
	}

	@Override
	public void delete(Point point) {
		setPoint(point);
		genericDAO.delete(pointEntity);				
	}

	@Override
	public int create(Point point) {
		logger.entry();
		setPoint(point);
		int id = genericDAO.create(pointEntity);
		logger.exit();
		return id;
	}

	@Override
	public Point get(int id) {
		pointEntity = genericDAO.get(id);
		if (pointEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setPointEntity(pointEntity);
		return point;
	}

	@Override
	public Point getChild(int id) {
		get(id);
		fetchChild();
		return point;
	}

}
