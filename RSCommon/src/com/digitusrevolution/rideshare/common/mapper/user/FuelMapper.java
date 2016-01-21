package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.FuelEntity;
import com.digitusrevolution.rideshare.model.user.domain.Fuel;

public class FuelMapper implements Mapper<Fuel, FuelEntity>{

	@Override
	public FuelEntity getEntity(Fuel fuel, boolean fetchChild) {
		FuelEntity fuelEntity = new FuelEntity();
		fuelEntity.setType(fuel.getType());
		fuelEntity.setPrice(fuel.getPrice());
		return fuelEntity;
	}

	@Override
	public FuelEntity getEntityChild(Fuel model, FuelEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Fuel getDomainModel(FuelEntity fuelEntity, boolean fetchChild) {
		Fuel fuel = new Fuel();
		fuel.setType(fuelEntity.getType());
		fuel.setPrice(fuelEntity.getPrice());
		return fuel;
	}

	@Override
	public Fuel getDomainModelChild(Fuel model, FuelEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Fuel> getDomainModels(Collection<Fuel> fuels, Collection<FuelEntity> fuelEntities,
			boolean fetchChild) {
		for (FuelEntity fuelEntity : fuelEntities) {
			Fuel fuel = new Fuel();
			fuel = getDomainModel(fuelEntity, fetchChild);
			fuels.add(fuel);
		}
		return fuels;
	}

	@Override
	public Collection<FuelEntity> getEntities(Collection<FuelEntity> fuelEntities, Collection<Fuel> fuels,
			boolean fetchChild) {
		for (Fuel fuel : fuels) {
			FuelEntity fuelEntity = new FuelEntity();
			fuelEntity = getEntity(fuel, fetchChild);
			fuelEntities.add(fuelEntity);
		}
		return fuelEntities;
	}

}
