package com.digitusrevolution.rideshare.common.mapper.user;

import java.util.Collection;

import com.digitusrevolution.rideshare.common.inf.Mapper;
import com.digitusrevolution.rideshare.model.user.data.CurrencyEntity;
import com.digitusrevolution.rideshare.model.user.domain.Currency;

public class CurrencyMapper implements Mapper<Currency, CurrencyEntity>{

	@Override
	public CurrencyEntity getEntity(Currency currency, boolean fetchChild) {
		CurrencyEntity currencyEntity = new CurrencyEntity();
		currencyEntity.setId(currency.getId());
		currencyEntity.setName(currency.getName());
		currencyEntity.setConversionRate(currency.getConversionRate());
		return currencyEntity;
	}

	@Override
	public CurrencyEntity getEntityChild(Currency model, CurrencyEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Currency getDomainModel(CurrencyEntity currencyEntity, boolean fetchChild) {
		Currency currency = new Currency();
		currency.setId(currencyEntity.getId());
		currency.setName(currencyEntity.getName());
		currency.setConversionRate(currencyEntity.getConversionRate());
		return currency;
	}

	@Override
	public Currency getDomainModelChild(Currency model, CurrencyEntity entity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<Currency> getDomainModels(Collection<Currency> models, Collection<CurrencyEntity> entities,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<CurrencyEntity> getEntities(Collection<CurrencyEntity> entities, Collection<Currency> model,
			boolean fetchChild) {
		// TODO Auto-generated method stub
		return null;
	}

}
