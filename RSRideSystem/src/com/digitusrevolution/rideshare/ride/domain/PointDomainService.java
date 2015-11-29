package com.digitusrevolution.rideshare.ride.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainService;
import com.digitusrevolution.rideshare.model.ride.data.PointEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.ride.data.PointDAO;


public class PointDomainService implements DomainService<Point>{

	private static final Logger logger = LogManager.getLogger(PointDomainService.class.getName());
	private PointDAO pointDAO;

	public PointDomainService() {
		pointDAO = new PointDAO();
	}

	@Override
	public int create(Point point) {
		logger.entry();
		PointDO pointDO = new PointDO();
		pointDO.setPoint(point);
		int id = pointDAO.create(pointDO.getPointEntity());
		logger.exit();
		return id;
	}

	@Override
	public Point get(int id) {
		PointDO pointDO = new PointDO();
		PointEntity pointEntity = new PointEntity();
		pointEntity = pointDAO.get(id);
		if (pointEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		pointDO.setPointEntity(pointEntity);
		return pointDO.getPoint();
	}

	@Override
	public Point getChild(int id) {
	
		// Don't try to call getUser to avoid duplicate code, else you would loose persistent entity object which is required for lazy fetch
		
		PointDO pointDO = new PointDO();
		PointEntity pointEntity = new PointEntity();
		pointEntity = pointDAO.get(id);
		if (pointEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		pointDO.setPointEntity(pointEntity);
		pointDO.mapChildDataModelToDomainModel();
		return pointDO.getPoint();

	}

	@Override
	public List<Point> getAll() {
		List<PointEntity> pointEntities = new ArrayList<>();
		List<Point> points = new ArrayList<>();
		pointEntities = pointDAO.getAll();
		for (PointEntity pointEntity : pointEntities) {
			PointDO pointDO = new PointDO();
			pointDO.setPointEntity(pointEntity);
			points.add(pointDO.getPoint());
		}
		return points;
	}

	@Override
	public void update(Point point) {
		PointDO pointDO = new PointDO();
		pointDO.setPoint(point);
		pointDAO.update(pointDO.getPointEntity());
	}

	@Override
	public void delete(Point point) {
		PointDO pointDO = new PointDO();
		pointDO.setPoint(point);
		pointDAO.delete(pointDO.getPointEntity());
	}


}
