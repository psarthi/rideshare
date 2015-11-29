package com.digitusrevolution.rideshare.common.mapper.user;

import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityMapper {
	
	public CityEntity getCityEntity(City city) {
		CityEntity cityEntity = new CityEntity();
		cityEntity.setId(city.getId());
		cityEntity.setName(city.getName());
		return cityEntity;
	}

	public City getCity(CityEntity cityEntity) {
		City city = new City();
		city.setId(cityEntity.getId());
		city.setName(cityEntity.getName());
		return city;
	}

}
