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
	private final GenericDAO<CurrencyEntity, Integer> genericDAO;
	
	public CurrencyDO() {
		currency = new Currency();
		genericDAO = new GenericDAOImpl<>(CurrencyEntity.class);
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Currency getCurrency() {
		return currency;
	}

	@Override
	public List<Currency> getAll() {
		List<Currency> currencies = new ArrayList<>();
		List<CurrencyEntity> currencyEntities = genericDAO.getAll();
		for (CurrencyEntity currencyEntity : currencyEntities) {
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
		setCurrency(currency);
		genericDAO.update(currency.getEntity());				
	}

	@Override
	public int create(Currency currency) {
		setCurrency(currency);
		int id = genericDAO.create(currency.getEntity());
		return id;
	}

	@Override
	public Currency get(int id) {
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
		setCurrency(currency);
		genericDAO.delete(currency.getEntity());			
	}

}
