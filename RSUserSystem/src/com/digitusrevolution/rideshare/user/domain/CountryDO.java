package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKString;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.common.mapper.user.CountryMapper;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.domain.Country;

public class CountryDO implements DomainObjectPKString<Country>{

	private Country country;
	private CountryEntity countryEntity;
	private CountryMapper countryMapper;
	private final GenericDAO<CountryEntity, String> genericDAO;
	
	public CountryDO() {
		country = new Country();
		countryEntity = new CountryEntity();
		countryMapper = new CountryMapper();
		genericDAO = new GenericDAOImpl<>(CountryEntity.class);
	}
	
	public void setCountry(Country country) {
		this.country = country;
		countryEntity = countryMapper.getEntity(country, true);
	}

	public void setCountryEntity(CountryEntity countryEntity) {
		this.countryEntity = countryEntity;
		country = countryMapper.getDomainModel(countryEntity, false);
	}

	@Override
	public void fetchChild() {
		country = countryMapper.getDomainModelChild(country, countryEntity);
	}

	@Override
	public List<Country> getAll() {
		List<Country> countries = new ArrayList<>();
		List<CountryEntity> countryEntities = genericDAO.getAll();
		for (CountryEntity countryEntity : countryEntities) {
			setCountryEntity(countryEntity);
			countries.add(country);
		}
		return countries;
	}

	@Override
	public void update(Country country) {
		if (country.getName().isEmpty()){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+country.getName());
		}
		setCountry(country);
		genericDAO.update(countryEntity);				
	}

	@Override
	public String create(Country country) {
		setCountry(country);
		String name = genericDAO.create(countryEntity);
		return name;
	}

	@Override
	public Country get(String name) {
		countryEntity = genericDAO.get(name);
		if (countryEntity == null){
			throw new NotFoundException("No Data found with id: "+name);
		}
		setCountryEntity(countryEntity);
		return country;
	}

	@Override
	public Country getChild(String name) {
		get(name);
		fetchChild();
		return country;
	}

	@Override
	public void delete(String name) {
		country = get(name);
		setCountry(country);
		genericDAO.delete(countryEntity);				
	}


}
