package com.digitusrevolution.rideshare.ride.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainService;
import com.digitusrevolution.rideshare.model.ride.data.core.RideRequestEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.ride.data.RideRequestDAO;

public class RideRequestDomainService implements DomainService<RideRequest>{

	private static final Logger logger = LogManager.getLogger(RideRequestDomainService.class.getName());
	private final RideRequestDAO rideRequestDAO; 
	
	public RideRequestDomainService() {
		rideRequestDAO = new RideRequestDAO();
	}
	
	public List<RideRequest> searchRideRequests(Ride ride){

		return null;
	}

	@Override
	public int create(RideRequest rideRequest) {
		logger.entry();
		RideRequestDO rideRequestDO = new RideRequestDO();
		rideRequestDO.setRideRequest(rideRequest);
		int id = rideRequestDAO.create(rideRequestDO.getRideRequestEntity());
		logger.exit();
		return id;
	}

	@Override
	public RideRequest get(int id) {
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity = rideRequestDAO.get(id);
		if (rideRequestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		rideRequestDO.setRideRequestEntity(rideRequestEntity);
		return rideRequestDO.getRideRequest();
	}

	@Override
	public RideRequest getChild(int id) {
		RideRequestDO rideRequestDO = new RideRequestDO();
		RideRequestEntity rideRequestEntity = new RideRequestEntity();
		rideRequestEntity = rideRequestDAO.get(id);
		if (rideRequestEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		rideRequestDO.setRideRequestEntity(rideRequestEntity);
		rideRequestDO.fetchChild();
		return rideRequestDO.getRideRequest();	}

	@Override
	public List<RideRequest> getAll() {
		List<RideRequestEntity> rideRequestEntities = new ArrayList<>();
		List<RideRequest> rideRequests = new ArrayList<>();
		rideRequestEntities = rideRequestDAO.getAll();
		for (RideRequestEntity rideRequestEntity : rideRequestEntities) {
			RideRequestDO rideRequestDO = new RideRequestDO();
			rideRequestDO.setRideRequestEntity(rideRequestEntity);
			rideRequests.add(rideRequestDO.getRideRequest());
		}
		return rideRequests;
	}

	@Override
	public void update(RideRequest rideRequest) {
		RideRequestDO rideRequestDO = new RideRequestDO();
		rideRequestDO.setRideRequest(rideRequest);
		rideRequestDAO.update(rideRequestDO.getRideRequestEntity());
	}

	@Override
	public void delete(RideRequest rideRequest) {
		RideRequestDO rideRequestDO = new RideRequestDO();
		rideRequestDO.setRideRequest(rideRequest);
		rideRequestDAO.delete(rideRequestDO.getRideRequestEntity());	}

}
