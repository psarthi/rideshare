package com.digitusrevolution.rideshare.user.domain;

import com.digitusrevolution.rideshare.common.DomainDataMapper;
import com.digitusrevolution.rideshare.model.user.data.CityEntity;
import com.digitusrevolution.rideshare.model.user.domain.City;

public class CityDO implements DomainDataMapper{
	
	City city;
	CityEntity cityEntity;
	
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapDataModelToDomainModel() {
		// TODO Auto-generated method stub
		
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
