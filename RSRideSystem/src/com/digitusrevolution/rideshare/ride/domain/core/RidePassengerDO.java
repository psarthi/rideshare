package com.digitusrevolution.rideshare.ride.domain.core;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.ride.core.RidePassengerMapper;
import com.digitusrevolution.rideshare.model.ride.data.core.RidePassengerEntity;
import com.digitusrevolution.rideshare.model.ride.domain.core.RidePassenger;

public class RidePassengerDO implements DomainObjectPKInteger<RidePassenger>{
	
	private RidePassenger ridePassenger;
	private RidePassengerEntity ridePassengerEntity;
	private RidePassengerMapper ridePassengerMapper;
	private GenericDAO<RidePassengerEntity, Integer> genericDAO;
	
	public RidePassengerDO() {
		ridePassenger = new RidePassenger();
		ridePassengerEntity = new RidePassengerEntity();
		ridePassengerMapper = new RidePassengerMapper();
		genericDAO = new GenericDAOImpl<>(RidePassengerEntity.class);
	}

	public void setRidePassenger(RidePassenger ridePassenger) {
		this.ridePassenger = ridePassenger;
		ridePassengerEntity = ridePassengerMapper.getEntity(ridePassenger, true);
	}

	public void setRidePassengerEntity(RidePassengerEntity ridePassengerEntity) {
		this.ridePassengerEntity = ridePassengerEntity;
		ridePassenger = ridePassengerMapper.getDomainModel(ridePassengerEntity, false);
	}

	@Override
	public List<RidePassenger> getAll() {
		List<RidePassenger> ridePassengers = new ArrayList<>();
		List<RidePassengerEntity> ridePassengerEntities = genericDAO.getAll();
		for (RidePassengerEntity ridePassengerEntity : ridePassengerEntities) {
			setRidePassengerEntity(ridePassengerEntity);
			ridePassengers.add(ridePassenger);
		}
		return ridePassengers;
	}

	@Override
	public void update(RidePassenger ridePassenger) {
		if (ridePassenger.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+ridePassenger.getId());
		}
		setRidePassenger(ridePassenger);
		genericDAO.update(ridePassengerEntity);		
	}

	@Override
	public void fetchChild() {
		ridePassenger = ridePassengerMapper.getDomainModelChild(ridePassenger, ridePassengerEntity);		
	}

	@Override
	public int create(RidePassenger ridePassenger) {
		setRidePassenger(ridePassenger);
		int id = genericDAO.create(ridePassengerEntity);
		return id;
	}

	@Override
	public RidePassenger get(int id) {
		ridePassengerEntity = genericDAO.get(id);
		if (ridePassengerEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setRidePassengerEntity(ridePassengerEntity);
		return ridePassenger;
	}

	@Override
	public RidePassenger getAllData(int id) {
		get(id);
		fetchChild();
		return ridePassenger;
	}

	@Override
	public void delete(int id) {
		ridePassenger = get(id);
		setRidePassenger(ridePassenger);
		genericDAO.delete(ridePassengerEntity);		
	}

}
