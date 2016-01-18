package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityMapper implements Mapper<City, CityEntity>{
	
	@Override
	public CityEntity getEntityWithOnlyPK(City city) {
		CityEntity cityEntity = new CityEntity();
		cityEntity.setId(city.getId());
		return cityEntity;
	}

	@Override
	public CityEntity getEntity(City city) {
		CityEntity cityEntity = new CityEntity();
		cityEntity = getEntityWithOnlyPK(city);
		cityEntity.setName(city.getName());
		return cityEntity;
	}

	@Override
	public City getDomainModelWithOnlyPK(CityEntity cityEntity) {
		City city = new City();
		city.setId(cityEntity.getId());
		return city;
	}

	@Override
	public City getDomainModel(CityEntity cityEntity) {
		City city = new City();
		city = getDomainModelWithOnlyPK(cityEntity);
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
	public Collection<City> getDomainModelsWithOnlyPK(Collection<City> models, Collection<CityEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<City> getDomainModels(Collection<City> models, Collection<CityEntity> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CityEntity> getEntitiesWithOnlyPK(Collection<CityEntity> entities, Collection<City> model) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CityEntity> getEntities(Collection<CityEntity> entities, Collection<City> model) {
		// TODO Auto-generated method stub
		return null;
	}

}
