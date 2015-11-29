package com.digitusrevolution.rideshare.ride.domain;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.DomainService;
import com.digitusrevolution.rideshare.model.ride.domain.Point;


public class PointDomainService implements DomainService<Point>{
	
	private static final Logger logger = LogManager.getLogger(PointDomainService.class.getName());

	@Override
	public int create(Point point) {
		return 0;	
	}

	@Override
	public Point get(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getChild(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Point> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Point point) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void delete(Point point) {
		// TODO Auto-generated method stub
		
	}


}
