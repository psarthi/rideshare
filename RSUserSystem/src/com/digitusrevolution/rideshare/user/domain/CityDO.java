package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityDO implements DomainObjectPKInteger<City>{
	
	private City city;
	private CityEntity cityEntity;
	private CityMapper cityMapper;
	private final GenericDAO<CityEntity, Integer> genericDAO;
	
	public CityDO() {
		city = new City();
		cityEntity = new CityEntity();
		cityMapper = new CityMapper();
		genericDAO = new GenericDAOImpl<>(CityEntity.class);
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
		cityEntity = cityMapper.getEntity(city);
		
	}

	private void setCityEntity(CityEntity cityEntity) {
		this.cityEntity = cityEntity;
		city = cityMapper.getDomainModel(cityEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int create(City city) {
		setCity(city);
		int id = genericDAO.create(cityEntity);
		return id;
	}

	@Override
	public City get(int id) {
		cityEntity = genericDAO.get(id);
		if (cityEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		setCityEntity(cityEntity);
		return city;
	}

	@Override
	public City getChild(int id) {
		get(id);
		fetchChild();
		return city;
	}

	@Override
	public List<City> getAll() {
		List<City> cities = new ArrayList<>();
		List<CityEntity> cityEntities = genericDAO.getAll();
		for (CityEntity cityEntity : cityEntities) {
			setCityEntity(cityEntity);
			cities.add(city);
		}
		return cities;
	}

	@Override
	public void update(City city) {
		setCity(city);
		genericDAO.update(cityEntity);				
	}

	@Override
	public void delete(City city) {
		setCity(city);
		genericDAO.delete(cityEntity);			
	}

}
