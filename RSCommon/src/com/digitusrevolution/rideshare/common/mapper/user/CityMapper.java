package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityMapper implements Mapper<City, CityEntity>{
	
	@Override
	public CityEntity getEntity(City city, boolean fetchChild) {
		CityEntity cityEntity = new CityEntity();
		cityEntity.setId(city.getId());
		cityEntity.setName(city.getName());
		return cityEntity;
	}

	@Override
	public City getDomainModel(CityEntity cityEntity,boolean fetchChild) {
		City city = new City();
		city.setId(cityEntity.getId());
		city.setName(cityEntity.getName());
		return city;
	}

	@Override
	public CityEntity getEntityChild(City city, CityEntity cityEntity) {
		return null;
	}

	@Override
	public City getDomainModelChild(City city, CityEntity cityEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<City> getDomainModels(Collection<City> cities, Collection<CityEntity> cityEntities, boolean fetchChild) {
		for (CityEntity cityEntity : cityEntities) {
			City city = new City();
			city = getDomainModel(cityEntity, fetchChild);
			cities.add(city);
		}
		return cities;
	}

	@Override
	public Collection<CityEntity> getEntities(Collection<CityEntity> cityEntities, Collection<City> cities, boolean fetchChild) {
		for (City city : cities) {
			CityEntity cityEntity = new CityEntity();
			cityEntity = getEntity(city, fetchChild);
			cityEntities.add(cityEntity);
		}
		return cityEntities;
	}

}
