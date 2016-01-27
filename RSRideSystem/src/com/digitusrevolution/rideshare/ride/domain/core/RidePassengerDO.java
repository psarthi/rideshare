package com.digitusrevolution.rideshare.ride.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;

public class RidePassengerDO implements DomainObjectPKInteger<RidePassenger>{
	
	private RidePassenger ridePassenger;
	private GenericDAO<RidePassengerEntity, Integer> genericDAO = new GenericDAOImpl<>(RidePassengerEntity.class);
	
	@Override
	public List<RidePassenger> getAll() {
		List<RidePassenger> ridePassengers = new ArrayList<>();
		List<RidePassengerEntity> ridePassengerEntities = genericDAO.getAll();
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			RidePassenger ridePassenger = new RidePassenger();
			ridePassenger.setEntity(ridePassengerEntity);
			ridePassengers.add(ridePassenger);
		}
		return ridePassengers;
	}

	@Override
	public void update(RidePassenger ridePassenger) {
		if (ridePassenger.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+ridePassenger.getId());
		}
		genericDAO.update(ridePassenger.getEntity());		
	}

	@Override
	public int create(RidePassenger ridePassenger) {
		int id = genericDAO.create(ridePassenger.getEntity());
		return id;
	}

	@Override
	public RidePassenger get(int id) {
		ridePassenger = new RidePassenger();
		RidePassengerEntity ridePassengerEntity = genericDAO.get(id);
		if (ridePassengerEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		ridePassenger.setEntity(ridePassengerEntity);
		return ridePassenger;
	}

	@Override
	public void delete(int id) {
		ridePassenger = get(id);
		genericDAO.delete(ridePassenger.getEntity());		
	}

}
