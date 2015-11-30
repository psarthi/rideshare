package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.core.PassengerEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Passenger;

public class PassengerMapper implements Mapper<Passenger, PassengerEntity>{

	@Override
	public PassengerEntity getEntity(Passenger model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PassengerEntity getEntityChild(Passenger model, PassengerEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Passenger getDomainModel(PassengerEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Passenger getDomainModelChild(Passenger passenger, PassengerEntity passengerEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Passenger> getDomainModels(Collection<PassengerEntity> passengerEntities) {
		return null;
	}

	@Override
	public Collection<PassengerEntity> getEntities(Collection<Passenger> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
