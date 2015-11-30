package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityMapper implements Mapper<City, CityEntity>{
	
	@Override
	public CityEntity getEntity(City city) {
		CityEntity cityEntity = new CityEntity();
		cityEntity.setId(city.getId());
		cityEntity.setName(city.getName());
		return cityEntity;
	}

	@Override
	public City getDomainModel(CityEntity cityEntity) {
		City city = new City();
		city.setId(cityEntity.getId());
		city.setName(cityEntity.getName());
		return city;
	}

	@Override
	public CityEntity getEntityChild(City model, CityEntity entity) {
		return null;
	}

	@Override
	public City getDomainModelChild(City model, CityEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<City> getDomainModels(Collection<CityEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CityEntity> getEntities(Collection<City> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
