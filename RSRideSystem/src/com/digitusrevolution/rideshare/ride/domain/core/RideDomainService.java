package com.digitusrevolution.rideshare.ride.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.Point;
import com.digitusrevolution.rideshare.model.ride.domain.Route;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideDAO;

public class RideDomainService implements DomainService<Ride>{

	private static final Logger logger = LogManager.getLogger(RideDomainService.class.getName());
	private final RideDAO rideDAO;
	
	public RideDomainService() {
		rideDAO = new RideDAO();
	}
	
	public List<Ride> searchRides(RideRequest rideRequest){

		return null;
	}

	public List<Ride> getUpcomingRides(int userId){
		
		return null;
	}
			
	public List<Route> getRoutes(Point startPoint, Point endPoint){		

		return null;
	}

	@Override
	public int create(Ride ride) {
		logger.entry();
		RideDO rideDO = new RideDO();
		rideDO.setRide(ride);
		int id = rideDAO.create(rideDO.getRideEntity());
		logger.exit();
		return id;
	}

	@Override
	public Ride get(int id) {
		RideDO rideDO = new RideDO();
		RideEntity rideEntity = new RideEntity();
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		rideDO.setRideEntity(rideEntity);
		return rideDO.getRide();
	}

	@Override
	public Ride getChild(int id) {
		RideDO rideDO = new RideDO();
		RideEntity rideEntity = new RideEntity();
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		rideDO.setRideEntity(rideEntity);
		rideDO.fetchChild();
		return rideDO.getRide();	
	}

	@Override
	public List<Ride> getAll() {
		List<RideEntity> rideEntities = new ArrayList<>();
		List<Ride> rides = new ArrayList<>();
		rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			RideDO rideDO = new RideDO();
			rideDO.setRideEntity(rideEntity);
			rides.add(rideDO.getRide());
		}
		return rides;
	}

	@Override
	public void update(Ride ride) {
		RideDO rideDO = new RideDO();
		rideDO.setRide(ride);
		rideDAO.update(rideDO.getRideEntity());
	}

	@Override
	public void delete(Ride ride) {
		RideDO rideDO = new RideDO();
		rideDO.setRide(ride);
		rideDAO.delete(rideDO.getRideEntity());
	}
	
}

