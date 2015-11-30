package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.inf.DomainObject;
import com.digitusrevolution.rideshare.common.mapper.user.CityMapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityDO implements DomainObject{
	
	private City city;
	private CityEntity cityEntity;
	private CityMapper cityMapper;
	
	public CityDO() {
		city = new City();
		cityEntity = new CityEntity();
		cityMapper = new CityMapper();
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
		cityEntity = cityMapper.getEntity(city);
		
	}

	public CityEntity getCityEntity() {
		return cityEntity;
	}

	public void setCityEntity(CityEntity cityEntity) {
		this.cityEntity = cityEntity;
		city = cityMapper.getDomainModel(cityEntity);
	}

	@Override
	public void fetchChild() {
		// TODO Auto-generated method stub
		
	}


}
