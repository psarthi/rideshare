package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityDO implements DomainObjectPKInteger<City>{
	
	private City city;
	private final GenericDAO<CityEntity, Integer> genericDAO = new GenericDAOImpl<>(CityEntity.class);
	
	@Override
	public int create(City city) {
		int id = genericDAO.create(city.getEntity());
		return id;
	}

	@Override
	public City get(int id) {
		city = new City();
		CityEntity cityEntity = genericDAO.get(id);
		if (cityEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		city.setEntity(cityEntity);
		return city;
	}

	@Override
	public List<City> getAll() {
		List<City> cities = new ArrayList<>();
		List<CityEntity> cityEntities = genericDAO.getAll();
		for (CityEntity cityEntity : cityEntities) {
			City city = new City();
			city.setEntity(cityEntity);
			cities.add(city);
		}
		return cities;
	}

	@Override
	public void update(City city) {
		if (city.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+city.getId());
		}
		genericDAO.update(city.getEntity());				
	}

	@Override
	public void delete(int id) {
		city = get(id);
		genericDAO.delete(city.getEntity());			
	}

}
