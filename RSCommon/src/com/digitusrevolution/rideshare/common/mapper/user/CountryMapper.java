package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.CountryEntity;
import com.digitusrevolution.rideshare.model.user.domain.Country;

public class CountryMapper implements Mapper<Country, CountryEntity>{

	@Override
	public CountryEntity getEntity(Country country, boolean fetchChild) {
		CountryEntity countryEntity = new CountryEntity();
		countryEntity.setName(country.getName());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		countryEntity.setCurrency(currencyMapper.getEntity(country.getCurrency(), fetchChild));
		FuelMapper fuelMapper = new FuelMapper();
		countryEntity.setFuels(fuelMapper.getEntities(countryEntity.getFuels(), country.getFuels(), fetchChild));
		
		if (fetchChild){
			countryEntity = getEntityChild(country, countryEntity);			
		} 

		return countryEntity;
	}

	@Override
	public CountryEntity getEntityChild(Country country, CountryEntity countryEntity) {
		
		StateMapper stateMapper = new StateMapper();
		countryEntity.setStates(stateMapper.getEntities(countryEntity.getStates(), country.getStates(), true));
		
		return countryEntity;
	}

	@Override
	public Country getDomainModel(CountryEntity countryEntity, boolean fetchChild) {
		Country country = new Country();
		country.setName(countryEntity.getName());
		CurrencyMapper currencyMapper = new CurrencyMapper();
		country.setCurrency(currencyMapper.getDomainModel(countryEntity.getCurrency(), fetchChild));
		FuelMapper fuelMapper = new FuelMapper();
		country.setFuels(fuelMapper.getDomainModels(country.getFuels(), countryEntity.getFuels(), fetchChild));
		
		if (fetchChild){
			country = getDomainModelChild(country, countryEntity);			
		} 

		return country;
	}

	@Override
	public Country getDomainModelChild(Country country, CountryEntity countryEntity) {
		StateMapper stateMapper = new StateMapper();
		country.setStates(stateMapper.getDomainModels(country.getStates(), countryEntity.getStates(), true));

		return country;
	}

	@Override
	public Collection<Country> getDomainModels(Collection<Country> countries, Collection<CountryEntity> countryEntities,
			boolean fetchChild) {
		for (CountryEntity countryEntity : countryEntities) {
			Country country = new Country();
			country = getDomainModel(countryEntity, fetchChild);
			countries.add(country);
		}
		return countries;
	}

	@Override
	public Collection<CountryEntity> getEntities(Collection<CountryEntity> countryEntities, Collection<Country> countries,
			boolean fetchChild) {
		for (Country country : countries) {
			CountryEntity countryEntity = new CountryEntity();
			countryEntity = getEntity(country, fetchChild);
			countryEntities.add(countryEntity);
		}
		return countryEntities;
	}

}
