package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.domain.Country;

public class CountryDO implements DomainObjectPKString<Country>{

	private Country country;
	private final GenericDAO<CountryEntity, String> genericDAO;
	
	public CountryDO() {
		country = new Country();
		genericDAO = new GenericDAOImpl<>(CountryEntity.class);
	}
	
	public void setCountry(Country country) {
		this.country = country;
	}

	public Country getCountry() {
		return country;
	}

	@Override
	public List<Country> getAll() {
		List<Country> countries = new ArrayList<>();
		List<CountryEntity> countryEntities = genericDAO.getAll();
		for (CountryEntity countryEntity : countryEntities) {
			country.setEntity(countryEntity);
			countries.add(country);
		}
		return countries;
	}

	@Override
	public void update(Country country) {
		if (country.getName().isEmpty()){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+country.getName());
		}
		genericDAO.update(country.getEntity());				
	}

	@Override
	public String create(Country country) {
		String name = genericDAO.create(country.getEntity());
		return name;
	}

	@Override
	public Country get(String name) {
		CountryEntity countryEntity = genericDAO.get(name);
		if (countryEntity == null){
			throw new NotFoundException("No Data found with id: "+name);
		}
		country.setEntity(countryEntity);
		return country;
	}
	
	@Override
	public void delete(String name) {
		country = get(name);
		genericDAO.delete(country.getEntity());				
	}


}
