package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.DomainObject;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityDO implements DomainObject{
	
	private City city;
	private CityEntity cityEntity;
	
	public CityDO() {
		city = new City();
		cityEntity = new CityEntity();		
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
		mapDomainModelToDataModel();
	}

	public CityEntity getCityEntity() {
		return cityEntity;
	}

	public void setCityEntity(CityEntity cityEntity) {
		this.cityEntity = cityEntity;
		mapDataModelToDomainModel();
	}

	@Override
	public void mapDomainModelToDataModel() {
		cityEntity.setId(city.getId());
		cityEntity.setName(city.getName());
	}

	@Override
	public void mapDataModelToDomainModel() {
		city.setId(cityEntity.getId());
		city.setName(cityEntity.getName());
	}

	@Override
	public void mapChildDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapChildDomainModelToDataModel() {
		// TODO Auto-generated method stub
		
	}
	

}
