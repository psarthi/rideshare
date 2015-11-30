package com.digitusrevolution.rideshare.common.mapper.user.core;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.core.DriverEntity;
import com.digitusrevolution.rideshare.model.user.domain.core.Driver;

public class DriverMapper implements Mapper<Driver, DriverEntity>{

	@Override
	public DriverEntity getEntity(Driver model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DriverEntity getEntityChild(Driver model, DriverEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Driver getDomainModel(DriverEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Driver getDomainModelChild(Driver model, DriverEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Driver> getDomainModels(Collection<DriverEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<DriverEntity> getEntities(Collection<Driver> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
