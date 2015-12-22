package com.digitusrevolution.rideshare.ride.domain.core;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RideMapper;
import com.digitusrevolution.rideshare.common.util.RESTClientUtil;
import com.digitusrevolution.rideshare.model.ride.data.core.RideEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.Ride;
import com.digitusrevolution.rideshare.model.ride.domain.core.RideRequest;
import com.digitusrevolution.rideshare.model.user.domain.Role;
import com.digitusrevolution.rideshare.ride.data.RideDAO;

public class RideDO implements DomainObjectPKInteger<Ride>{
	
	private static final Logger logger = LogManager.getLogger(RideDO.class.getName());
	private Ride ride;
	private RideEntity rideEntity;
	private RideMapper rideMapper;
	private final RideDAO rideDAO;
	
	public RideDO() {
		ride = new Ride();
		rideEntity = new RideEntity();
		rideMapper = new RideMapper();
		rideDAO = new RideDAO();
	}
	
	public void setRide(Ride ride) {
		this.ride = ride;
		rideEntity = rideMapper.getEntity(ride);
	}

	private void setRideEntity(RideEntity rideEntity) {
		this.rideEntity = rideEntity;
		ride = rideMapper.getDomainModel(rideEntity);
	}

	@Override
	public void fetchChild() {
		ride = rideMapper.getDomainModelChild(ride, rideEntity);
	}

	@Override
	public List<Ride> getAll() {
		List<Ride> rides = new ArrayList<>();
		List<RideEntity> rideEntities = rideDAO.getAll();
		for (RideEntity rideEntity : rideEntities) {
			setRideEntity(rideEntity);
			rides.add(ride);
		}
		return rides;

	}

	@Override
	public void update(Ride ride) {
		setRide(ride);
		rideDAO.update(rideEntity);
	}

	@Override
	public void delete(Ride ride) {
		setRide(ride);
		rideDAO.delete(rideEntity);
	}

	@Override
	public int create(Ride ride) {
		logger.entry();
		setRide(ride);
		int id = rideDAO.create(rideEntity);
		logger.exit();		
		return id;
	}

	@Override
	public Ride get(int id) {
		rideEntity = rideDAO.get(id);
		if (rideEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRideEntity(rideEntity);
		return ride;
	}

	@Override
	public Ride getChild(int id) {
		get(id);
		fetchChild();
		return ride;
	}
	
	public List<Ride> searchRides(RideRequest rideRequest){

		return null;
	}

	public List<Ride> getUpcomingRides(int userId){
		
		return null;
	}
	
	public int offerRide(Ride ride){
		int userId = ride.getDriver().getId();
		Collection<Role> roles = RESTClientUtil.getRoles(userId);
		int id = 0;
		for (Role role : roles) {
			if (role.getName().equals("Driver")){
				ride.setStatus("planned");
				ZonedDateTime startTimeUTC = ride.getStartTime().withZoneSameInstant(ZoneOffset.UTC);
				ride.setStartTime(startTimeUTC);
				id = create(ride);
				return id;
			} 
		}
		throw new NotAuthorizedException("Can't offer ride unless you are a Driver");
	}
}
