package com.digitusrevolution.rideshare.user.domain;

import java.util.ArrayList;
import java.util.List;

import javax.management.openmbean.InvalidKeyException;
import javax.ws.rs.NotFoundException;

import com.digitusrevolution.rideshare.common.db.GenericDAOImpl;
import com.digitusrevolution.rideshare.common.inf.DomainObjectPKInteger;
import com.digitusrevolution.rideshare.common.inf.GenericDAO;
import com.digitusrevolution.rideshare.model.user.data.CurrencyEntity;
import com.digitusrevolution.rideshare.model.user.domain.Currency;

public class CurrencyDO implements DomainObjectPKInteger<Currency>{
	
	private Currency currency;
	private final GenericDAO<CurrencyEntity, Integer> genericDAO = new GenericDAOImpl<>(CurrencyEntity.class);
	
	@Override
	public List<Currency> getAll() {
		List<Currency> currencies = new ArrayList<>();
		List<CurrencyEntity> currencyEntities = genericDAO.getAll();
		for (CurrencyEntity currencyEntity : currencyEntities) {
			Currency currency = new Currency();
			currency.setEntity(currencyEntity);
			currencies.add(currency);
		}
		return currencies;
	}

	@Override
	public void update(Currency currency) {
		if (currency.getId()==0){
			throw new InvalidKeyException("Updated failed due to Invalid key: "+currency.getId());
		}
		genericDAO.update(currency.getEntity());				
	}

	@Override
	public int create(Currency currency) {
		int id = genericDAO.create(currency.getEntity());
		return id;
	}

	@Override
	public Currency get(int id) {
		currency = new Currency();
		CurrencyEntity currencyEntity = genericDAO.get(id);
		if (currencyEntity == null){
			throw new NotFoundException("No Data found with id: "+id);
		}
		currency.setEntity(currencyEntity);
		return currency;
	}

	@Override
	public void delete(int id) {
		currency = get(id);
		genericDAO.delete(currency.getEntity());			
	}

	@Override
	public Currency getWithEagerFetch(int id) {
		currency = get(id);
		currency.fetchReferenceVariable();
		return currency;
	}

}
